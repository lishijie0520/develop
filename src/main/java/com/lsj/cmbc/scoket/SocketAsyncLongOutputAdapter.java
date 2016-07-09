package com.lsj.cmbc.scoket;

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
	private volatile boolean canRun = true;

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
				while (true) {
					try {
						refresh();
						int interval = NumberUtils.toInt((String) configParams.get("REFRESH_INTERVAL"), 60) * 1000;// 刷新间隔，单位：秒
						Thread.sleep(interval);
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
				while (true) {
					try {
						if (canRun) {
							int interval = NumberUtils.toInt((String) configParams.get("HEARTBEAT_INTERVAL"), 30) * 1000;// 心跳间隔，单位：毫秒
							checkConnect(interval);
							Thread.sleep(interval);
						} else {
							int interval = NumberUtils.toInt((String) configParams.get("RUN_CHECK_INTERVAL"), 3) * 60 * 1000;// 运行检测间隔，单位：分
							Thread.sleep(interval);
						}
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
				while (true) {
					try {
						if (canRun) {
							sendMessage();
						} else {
							int interval = NumberUtils.toInt((String) configParams.get("RUN_CHECK_INTERVAL"), 3) * 60 * 1000;// 运行检测间隔，单位：分
							Thread.sleep(interval);
						}
					} catch (Exception e) {
						logger.error(e.getLocalizedMessage(), e);
					}
				}
			}
		}).start();
		/**
		 * 运行报文接收线程
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						if (canRun) {
							receiveMessage();
						} else {
							int interval = NumberUtils.toInt((String) configParams.get("RUN_CHECK_INTERVAL"), 3) * 60 * 1000;// 运行检测间隔，单位：分
							Thread.sleep(interval);
						}
					} catch (Exception e) {
						logger.error(e.getLocalizedMessage(), e);
					}
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
		// TODO
		// 读取参数功能自行实现
		
		
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
	 * 连接
	 * 
	 * @return
	 */
	private synchronized void connect() {
		if (!canRun) {
			return;
		}

		long retryInterval = NumberUtils.toLong((String) configParams.get("CONNET_RETRY_INTERVAL"), 10);// 连接重试间隔，单位：秒
		int retryCount = NumberUtils.toInt((String) configParams.get("CONNET_RETRY_COUNT"), 3);// 连接重试次数
		String hostName = StringUtils.trimToNull((String) configParams.get("HOST_NAME"));// 主机名称
		String hostAddress = StringUtils.trimToNull((String) configParams.get("HOST_ADDRESS"));// 主机名称
		int hostPort = NumberUtils.toInt((String) configParams.get("HOST_PORT"), 9008);// 主机端口
		int sendBufferSize = NumberUtils.toInt((String) configParams.get("SEND_BUFFER_SIZE"), 200 * 1024);// 发送缓冲区容量，单位：字节
		int receiveBufferSize = NumberUtils.toInt((String) configParams.get("RECEIVE_BUFFER_SIZE"), 200 * 1024);// 接收缓冲区容量，单位：字节
		int connetTimeout = NumberUtils.toInt((String) configParams.get("CONNET_TIMEOUT"), 30 * 1000);// 连接超时，单位：毫秒
		for (int i = 0; i < retryCount; i++) {
			try {
				Socket socket = socketHelper.getSocket();
				if (socket != null) {
					if (socket.isConnected()) {
						return;
					} else {
						this.close();
					}
				}

				socket = new Socket();
				socket.setSendBufferSize(sendBufferSize);
				socket.setReceiveBufferSize(receiveBufferSize);
				socket.setKeepAlive(true);
				socket.setTcpNoDelay(true);
				// socket.setSoTimeout(timeout);// 不能设置读超时
				socket.connect(new InetSocketAddress(hostAddress, hostPort), connetTimeout);
				logger.info("对端[{}-{}:{}]连接成功，本地端口[{}]", new Object[] { hostName, hostAddress, hostPort, socket.getLocalPort() });
				socketHelper.setSocket(socket);
				socketHelper.setSocketKey("127.0.0.1:" + socket.getLocalPort());
				socketHelper.setLastActiveTime(new Date());
				socketHelper.setReceivedBytes(null);
				return;
			} catch (Exception e) {
				logger.error("对端[{}-{}:{}]连接失败", new Object[] { hostName, hostAddress, hostPort });
				logger.error(e.getMessage(), e);
				this.close();
			}

			try {
				logger.info("对端[{}-{}:{}]连接失败，[{}]秒后重连", new Object[] { hostName, hostAddress, hostPort, retryInterval });
				Thread.sleep(retryInterval * 1000);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		logger.error("对端[{}-{}:{}]连接连续失败达到阀值[{}]，进入休眠", new Object[] { hostName, hostAddress, hostPort, retryCount });
		canRun = false;
		configParams.put("CAN_RUN", String.valueOf(canRun));
		// TODO
		// 是否允许运行参数入库的话，则会一直睡眠，直到人工修改才可唤醒
		// 若不入库，重新刷新之后，就会唤醒
		return;
	}

	/**
	 * 关闭
	 */
	private void close() {
		socketHelper.setReceivedBytes(null);// 清除已经保存的粘包块
		Socket socket = socketHelper.getSocket();
		if (socket != null) {
			try {
				socket.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		socketHelper.setSocket(null);
		socketHelper.setLastActiveTime(null);
	}

	/**
	 * 检测链接
	 * 
	 * @param heartbeatInterval
	 *            心跳间隔
	 */
	private void checkConnect(int heartbeatInterval) {
		while (socketHelper.getSocket() == null) {
			if (!canRun) {
				return;
			}
			this.connect();
		}

		try {
			Socket socket = socketHelper.getSocket();
			logger.info(DateFormatUtils.format(socketHelper.getLastActiveTime(), "yyyy-MM-dd HH:mm:ss"));
			if (((new Date()).getTime() - socketHelper.getLastActiveTime().getTime()) > heartbeatInterval) {
				String heartbeatMessage = StringUtils.trimToNull((String) configParams.get("HEARTBEAT_MESSAGE"));// 心跳报文
				String charset = StringUtils.trimToNull((String) configParams.get("CHARSET"));// 字符集
				socketHelper.setLastActiveTime(new Date());
				OutputStream output = socket.getOutputStream();
				output.write(heartbeatMessage.getBytes(charset));
				output.flush();
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			this.close();
		}
	}

	/**
	 * 发送报文
	 * 
	 * @param socketHelper
	 */
	private void sendMessage() {
		while (socketHelper.getSocket() == null) {
			if (!canRun) {
				return;
			}
			this.connect();
		}

		String hostName = StringUtils.trimToNull((String) configParams.get("HOST_NAME"));// 主机名称
		String hostAddress = StringUtils.trimToNull((String) configParams.get("HOST_ADDRESS"));// 主机名称
		int hostPort = NumberUtils.toInt((String) configParams.get("HOST_PORT"), 9008);// 主机端口
		String charset = StringUtils.trimToNull((String) configParams.get("CHARSET"));// 字符集
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
			Socket socket = socketHelper.getSocket();
			if (socket == null) {
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
			this.close();

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
		while (socketHelper.getSocket() == null) {
			if (!canRun) {
				return;
			}
			this.connect();
		}
		Socket socket = socketHelper.getSocket();

		String hostName = StringUtils.trimToNull((String) configParams.get("HOST_NAME"));// 主机名称
		String hostAddress = StringUtils.trimToNull((String) configParams.get("HOST_ADDRESS"));// 主机名称
		int hostPort = NumberUtils.toInt((String) configParams.get("HOST_PORT"), 9008);// 主机端口
		String charset = StringUtils.trimToNull((String) configParams.get("CHARSET"));// 字符集
		int headLength = NumberUtils.toInt((String) configParams.get("HEAD_LENGTH"), 6);// 报文头长度位数
		int messageCodeLength = NumberUtils.toInt((String) configParams.get("MESSAGE_CODE_LENGTH"), 15);// 报文码长度
		int macLength = NumberUtils.toInt((String) configParams.get("MAC_LENGTH"), 32);// MAC长度
		int maxSingleLength = NumberUtils.toInt((String) configParams.get("MAX_SINGLE_LENGTH"), 200 * 1024);// 单个报文最大长度，单位：字节
		String socketKey = socketHelper.getSocketKey();
		try {
			byte[] bytes = socketHelper.getReceivedBytes();
			if (bytes == null) {
				bytes = new byte[0];
			}
			InputStream input = socket.getInputStream();
			/**
			 * 1、读取报文头
			 */
			if (bytes.length < headLength) {
				byte[] headBytes = new byte[headLength - bytes.length];
				int couter = input.read(headBytes);
				if (couter < 0) {
					logger.error("连接[{} --> {}-{}:{}]已关闭", new Object[] { socketKey, hostName, hostAddress, hostPort });
					this.close();
					return;
				}
				bytes = ArrayUtils.addAll(bytes, ArrayUtils.subarray(headBytes, 0, couter));
				if (couter < headBytes.length) {// 未满足长度位数，可能是粘包造成，保存读取到的
					socketHelper.setReceivedBytes(bytes);
					return;
				}
			}
			String headMsg = new String(ArrayUtils.subarray(bytes, 0, headLength), charset);
			int xmlLength = NumberUtils.toInt(headMsg);
			if (xmlLength <= 0 || xmlLength > maxSingleLength * 1024) {
				logger.error("连接[{} --> {}-{}:{}]出现账数据，自动断链：{}", new Object[] { socketKey, hostName, hostAddress, hostPort, headMsg });
				this.close();
				return;
			}
			/**
			 * 2、读取报文体
			 */
			int bodyLength = messageCodeLength + xmlLength + macLength;
			if (bytes.length < headLength + bodyLength) {
				byte[] bodyBytes = new byte[headLength + bodyLength - bytes.length];
				int couter = input.read(bodyBytes);
				if (couter < 0) {
					logger.error("连接[{} --> {}-{}:{}]已关闭", new Object[] { socketKey, hostName, hostAddress, hostPort });
					this.close();
					return;
				}
				bytes = ArrayUtils.addAll(bytes, ArrayUtils.subarray(bodyBytes, 0, couter));
				if (couter < bodyBytes.length) {// 未满足长度位数，可能是粘包造成，保存读取到的
					socketHelper.setReceivedBytes(bytes);
					return;
				}
			}
			byte[] bodyBytes = ArrayUtils.subarray(bytes, headLength, headLength + bodyLength);
			String bodyMsg = new String(bodyBytes, charset);
			logger.info("本地 <<=== 对端[{}-{}:{}] ## {}", new Object[] { hostName, hostAddress, hostPort, bodyMsg });
			receiveQueue.put(bodyBytes);

			bytes = ArrayUtils.subarray(bytes, headLength + bodyLength, bytes.length);
			socketHelper.setReceivedBytes(bytes);
		} catch (Exception e) {
			logger.error("从对端[{}-{}:{}]接收报文出现异常", new Object[] { hostName, hostAddress, hostPort });
			logger.error(e.getLocalizedMessage(), e);
			this.close();
		}
	}
}
