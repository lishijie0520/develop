package com.lsj.cmbc.scoket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <strong>Title : SocketAsyncLongOutputAdapter</strong><br>
 * <strong>Description : TCP异步长连接输出适配器</strong><br>
 * <strong>Create on : 2015-9-29</strong><br>
 * 
 * @author linda1@cmbc.com.cn<br>
 */
public class SocketAsyncLongOutputAdapter {

	/**
	 * 日志对象
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 发送队列
	 */
	private LinkedBlockingQueue<byte[]> sendQueue;

	/**
	 * 接收队列
	 */
	private LinkedBlockingQueue<byte[]> receiveQueue;

	/**
	 * 配置参数集合
	 */
	private final Map<String, Object> configParams = new HashMap<String, Object>();

	/**
	 * 允许运行标识
	 */
	private boolean canRun = true;

	/**
	 * socket客户端对象
	 */
	private final SocketHelper socketHelper = new SocketHelper();

	/**
	 * @param sendQueue
	 *            the sendQueue to set
	 */
	public void setSendQueue(LinkedBlockingQueue<byte[]> sendQueue) {
		this.sendQueue = sendQueue;
	}

	/**
	 * @param receiveQueue
	 *            the receiveQueue to set
	 */
	public void setReceiveQueue(LinkedBlockingQueue<byte[]> receiveQueue) {
		this.receiveQueue = receiveQueue;
	}

	/**
	 * 启动
	 */
	public boolean start() {
		// 加载参数
		refresh();
		/**
		 * 运行内存刷新线程
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (canRun) {
					try {
						refresh();
						int refreshInterval = NumberUtils.toInt((String) configParams.get("REFRESH_INTERVAL"), 60 * 1000);// 刷新间隔，单位：毫秒
						Thread.sleep(refreshInterval);
					} catch (Exception e) {
						logger.error(e.getLocalizedMessage(), e);
					}
				}
			}
		}).start();
		/**
		 * 运行链接检测线程
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (canRun) {
					try {
						int heartbeatInterval = NumberUtils.toInt((String) configParams.get("HEARTBEAT_INTERVAL"), 30 * 1000);// 心跳间隔，单位：毫秒
						checkConnect(heartbeatInterval);
						Thread.sleep(heartbeatInterval);
					} catch (Exception e) {
						logger.error(e.getLocalizedMessage(), e);
					}
				}
			}
		}).start();
		/**
		 * 运行报文发送线程
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (canRun) {
					sendMessage();
				}
			}
		}).start();
		/**
		 * 运行报文接收线程
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (canRun) {
					receiveMessage();
				}
			}
		}).start();
		return true;
	}

	/**
	 * 停止
	 */
	public boolean stop() {
		canRun = false;
		return true;
	}

	/**
	 * 刷新参数
	 */
	private void refresh() {
		String canRunStr = (String) configParams.get("CAN_RUN");// 是否允许运行标识
		if ("false".equalsIgnoreCase(canRunStr)) {
			canRun = false;
		} else {
			canRun = true;
		}
		String hostName = StringUtils.trimToNull((String) configParams.get("HOST_NAME"));// 主机名称
		if (hostName == null) {
			hostName = "未知";
			configParams.put("HOST_NAME", hostName);
		}
		String hostAddress = StringUtils.trimToNull((String) configParams.get("HOST_ADDRESS"));// 主机名称
		if (hostAddress == null) {
			hostAddress = "127.0.0.1";
			configParams.put("HOST_ADDRESS", hostAddress);
		}
		String charset = StringUtils.trimToNull((String) configParams.get("CHARSET"));// 字符集
		if (charset == null) {
			charset = "UTF-8";
			configParams.put("CHARSET", charset);
		}
		int headLength = NumberUtils.toInt((String) configParams.get("HEAD_LENGTH"), 6);// 报文头长度位数
		configParams.put("HEAD_LENGTH", String.valueOf(headLength));
		int messageCodeLength = NumberUtils.toInt((String) configParams.get("MESSAGE_CODE_LENGTH"), 15);// 报文码长度
		configParams.put("MESSAGE_CODE_LENGTH", String.valueOf(messageCodeLength));
		int macLength = NumberUtils.toInt((String) configParams.get("MAC_LENGTH"), 32);// MAC长度
		configParams.put("MAC_LENGTH", String.valueOf(macLength));
		String heartbeatMessage = StringUtils.trimToNull((String) configParams.get("HEARTBEAT_MESSAGE"));// 心跳报文
		if (heartbeatMessage == null) {
			heartbeatMessage = StringUtils.leftPad("", headLength, "0");
			configParams.put("HEARTBEAT_MESSAGE", heartbeatMessage);
		}
	}

	/**
	 * 检测链接
	 * 
	 * @param heartbeatInterval
	 *            心跳间隔
	 */
	private void checkConnect(int heartbeatInterval) {
		try {
			Socket socket = socketHelper.getSocket();
			if (socket == null) {
				socket = this.connect();
				if (socket == null) {
					return;
				}
			}
			if (!socket.isConnected()) {
				String hostName = StringUtils.trimToNull((String) configParams.get("HOST_NAME"));// 主机名称
				String hostAddress = StringUtils.trimToNull((String) configParams.get("HOST_ADDRESS"));// 主机名称
				String hostPort = StringUtils.trimToNull((String) configParams.get("HOST_PORT"));// 主机端口
				logger.error("连接[{}-->{}-{}:{}]已断开", new Object[] { socketHelper.getSocketKey(), hostName, hostAddress, hostPort });
				try {
					socket.close();
				} catch (Exception e) {
				}
				socket = this.connect();
				if (socket == null) {
					return;
				}
			}
			logger.info(DateFormatUtils.format(socketHelper.getLastActiveTime(), "yyyy-MM-dd HH:mm:ss"));
			if (socket != null && socket.isConnected() && ((new Date()).getTime() - socketHelper.getLastActiveTime().getTime()) > heartbeatInterval) {
				String heartbeatMessage = StringUtils.trimToNull((String) configParams.get("HEARTBEAT_MESSAGE"));// 心跳报文
				String charset = StringUtils.trimToNull((String) configParams.get("CHARSET"));// 字符集
				socketHelper.setLastActiveTime(new Date());
				OutputStream output = socket.getOutputStream();
				output.write(heartbeatMessage.getBytes(charset));
				output.flush();
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @return
	 */
	private Socket connect() {
		String hostName = StringUtils.trimToNull((String) configParams.get("HOST_NAME"));// 主机名称
		String hostAddress = StringUtils.trimToNull((String) configParams.get("HOST_ADDRESS"));// 主机名称
		int hostPort = NumberUtils.toInt((String) configParams.get("HOST_PORT"), 9008);// 主机端口
		int sendBufferSize = NumberUtils.toInt((String) configParams.get("SEND_BUFFER_SIZE"), 200 * 1024);// 发送缓冲区容量，单位：字节
		int receiveBufferSize = NumberUtils.toInt((String) configParams.get("RECEIVE_BUFFER_SIZE"), 200 * 1024);// 接收缓冲区容量，单位：字节
		int connetTimeout = NumberUtils.toInt((String) configParams.get("CONNET_TIMEOUT"), 30 * 1000);// 连接超时，单位：毫秒
		Socket socket = null;
		try {
			socket = new Socket();
			socket.setSendBufferSize(sendBufferSize);
			socket.setReceiveBufferSize(receiveBufferSize);
			socket.setKeepAlive(true);
			socket.setTcpNoDelay(true);
			// socket.setSoTimeout(timeout);// 不能设置读超时
			socket.connect(new InetSocketAddress(hostAddress, hostPort), connetTimeout);
			logger.info("对端[{}-{}:{}]连接成功，本地端口[{}]", new Object[] { hostName, hostAddress, hostPort, socket.getLocalPort() });
			socketHelper.setSocketKey("127.0.0.1:" + socket.getLocalPort());
		} catch (Exception e) {
			logger.error("对端[{}-{}:{}]连接失败", new Object[] { hostName, hostAddress, hostPort });
			logger.error(e.getLocalizedMessage(), e);
			socket = null;
		}
		socketHelper.setSocket(socket);
		socketHelper.setLastActiveTime(new Date());
		return socket;
	}

	/**
	 * 发送报文
	 * 
	 * @param socketHelper
	 */
	private void sendMessage() {
		String hostName = StringUtils.trimToNull((String) configParams.get("HOST_NAME"));// 主机名称
		String hostAddress = StringUtils.trimToNull((String) configParams.get("HOST_ADDRESS"));// 主机名称
		int hostPort = NumberUtils.toInt((String) configParams.get("HOST_PORT"), 9008);// 主机端口
		String charset = StringUtils.trimToNull((String) configParams.get("CHARSET"));// 字符集
		Socket socket = socketHelper.getSocket();
		byte[] bytes = null;
		try {
			bytes = sendQueue.take();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return;
		}
		if (bytes == null || bytes.length < 1) {
			return;
		}
		try {
			if (socket == null || !socket.isConnected()) {
				sendQueue.put(bytes);
				return;
			}
			logger.info("本地 ===>> 对端[{}-{}:{}] ## {}", new Object[] { hostName, hostAddress, hostPort, new String(bytes, charset) });
			OutputStream os = socket.getOutputStream();
			os.write(bytes);
			os.flush();
			socketHelper.setLastActiveTime(new Date());
		} catch (Exception e) {
			logger.error("向对端[{}-{}:{}]发送报文出现异常", new Object[] { hostName, hostAddress, hostPort });
			logger.error(e.getLocalizedMessage(), e);
			socketHelper.setReceivedBytes(null);// 清除已经保存的粘包块
			try {
				socket.close();
			} catch (IOException e1) {
				logger.error(e1.getLocalizedMessage(), e1);
			}
			socket = null;
			socketHelper.setSocket(null);
			try {
				sendQueue.put(bytes);
			} catch (Exception e1) {
				logger.error(e1.getLocalizedMessage(), e1);
			}
		}
	}

	/**
	 * 接收报文
	 * 
	 * @param socketHelper
	 */
	private void receiveMessage() {
		String hostName = StringUtils.trimToNull((String) configParams.get("HOST_NAME"));// 主机名称
		String hostAddress = StringUtils.trimToNull((String) configParams.get("HOST_ADDRESS"));// 主机名称
		int hostPort = NumberUtils.toInt((String) configParams.get("HOST_PORT"), 9008);// 主机端口
		String charset = StringUtils.trimToNull((String) configParams.get("CHARSET"));// 字符集
		int headLength = NumberUtils.toInt((String) configParams.get("HEAD_LENGTH"), 6);// 报文头长度位数
		int messageCodeLength = NumberUtils.toInt((String) configParams.get("MESSAGE_CODE_LENGTH"), 15);// 报文码长度
		int macLength = NumberUtils.toInt((String) configParams.get("MAC_LENGTH"), 32);// MAC长度
		int maxSingleLength = NumberUtils.toInt((String) configParams.get("MAX_SINGLE_LENGTH"), 200 * 1024);// 单个报文最大长度，单位：字节
		int heartbeatInterval = NumberUtils.toInt((String) configParams.get("HEARTBEAT_INTERVAL"), 30 * 1000);// 心跳间隔，单位：毫秒
		Socket socket = socketHelper.getSocket();
		String socketKey = socketHelper.getSocketKey();
		try {
			if (socket == null || !socket.isConnected()) {
				return;
			}
			byte[] bytes = socketHelper.getReceivedBytes();
			InputStream input = socket.getInputStream();
			/**
			 * 1、读取报文头
			 */
			byte[] headBytes = new byte[headLength];
			int couter = input.read(headBytes);
			if (couter < 0) {
				logger.error("连接[{} --> {}-{}:{}]已关闭", new Object[] { socketKey, hostName, hostAddress, hostPort });
				socketHelper.setReceivedBytes(null);// 清除已经保存的粘包块
				socket.close();
				socket = null;
				socketHelper.setSocket(null);
				Thread.sleep(heartbeatInterval);
				return;
			}
			bytes = ArrayUtils.addAll(bytes, headBytes);
			if (bytes.length < headLength) {// 未满足长度位数，可能是粘包造成，保存读取到的
				socketHelper.setReceivedBytes(bytes);
				return;
			}
			String headMsg = new String(ArrayUtils.subarray(bytes, 0, headLength), charset);
			int xmlLength = NumberUtils.toInt(headMsg);
			if (xmlLength <= 0 || xmlLength > maxSingleLength * 1024) {
				logger.error("连接[{} --> {}-{}:{}]出现账数据，自动断链：{}", new Object[] { socketKey, hostName, hostAddress, hostPort, headMsg });
				socketHelper.setReceivedBytes(null);// 清除已经保存的粘包块
				socket.close();
				socket = null;
				socketHelper.setSocket(null);
				return;
			}
			bytes = ArrayUtils.subarray(bytes, headLength, bytes.length);
			/**
			 * 3、读取报文体
			 */
			int bodyLength = messageCodeLength + xmlLength + macLength;
			byte[] bodyBytes = new byte[bodyLength];
			couter = input.read(bodyBytes);
			if (couter < 0) {
				logger.error("连接[{} --> {}-{}:{}]已关闭", new Object[] { socketKey, hostName, hostAddress, hostPort });
				socketHelper.setReceivedBytes(null);// 清除已经保存的粘包块
				socket.close();
				socket = null;
				socketHelper.setSocket(null);
				return;
			}
			bytes = ArrayUtils.addAll(bytes, bodyBytes);
			if (bytes.length < bodyLength) {// 未满足长度位数，可能是粘包造成，保存读取到的
				socketHelper.setReceivedBytes(bytes);
				return;
			}
			String bodyMsg = new String(ArrayUtils.subarray(bytes, 0, bodyLength), charset);
			logger.info("本地 <<=== 对端[{}-{}:{}] ## {}", new Object[] { hostName, hostAddress, hostPort, bodyMsg });
			receiveQueue.put(bodyBytes);

			bytes = ArrayUtils.subarray(bytes, bodyLength, bytes.length);
			socketHelper.setReceivedBytes(bytes);
		} catch (Exception e) {
			logger.error("从对端[{}-{}:{}]接收报文出现异常", new Object[] { hostName, hostAddress, hostPort });
			logger.error(e.getLocalizedMessage(), e);
			socketHelper.setReceivedBytes(null);// 清除已经保存的粘包块
			try {
				socket.close();
			} catch (Exception e1) {
			}
			socket = null;
			socketHelper.setSocket(null);
		}
	}
}
