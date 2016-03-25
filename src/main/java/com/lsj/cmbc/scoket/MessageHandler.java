package com.lsj.cmbc.scoket;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <strong>Title : MessageHandler</strong><br>
 * <strong>Description : 报文处理器</strong><br>
 * <strong>Create on : 2015-9-30</strong><br>
 * 
 * @author linda1@cmbc.com.cn<br>
 */
public class MessageHandler {

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
	 * 
	 * @return
	 */
	public boolean start() {
		// 加载参数
		refresh();
		try {
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

			int poolSize = NumberUtils.toInt((String) configParams.get("POOL_SIZE"), 1);// 线程池
			// 初始化线程池
			ExecutorService executors = Executors.newFixedThreadPool(poolSize);
			for (int i = 0; i < poolSize; i++) {
				executors.execute(new Runnable() {
					@Override
					public void run() {
						while (canRun) {
							try {
								byte[] bytes = pack();
								if (bytes != null) {
									sendQueue.put(bytes);
								}
								Thread.sleep(3 * 1000);
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
						}
					}
				});
			}
			executors.shutdown();

			// 初始化线程池
			executors = Executors.newFixedThreadPool(poolSize);
			for (int i = 0; i < poolSize; i++) {
				executors.execute(new Runnable() {
					@Override
					public void run() {
						while (canRun) {
							try {
								byte[] bytes = receiveQueue.take();
								unpack(bytes);
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
							}
						}
					}
				});
			}
			executors.shutdown();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			this.stop();
			return false;
		}
		return true;
	}

	/**
	 * 停止
	 * 
	 * @return
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
		String charset = StringUtils.trimToNull((String) configParams.get("CHARSET"));// 字符集
		if (charset == null) {
			charset = "UTF-8";
			configParams.put("CHARSET", charset);
		}
		int headLength = NumberUtils.toInt((String) configParams.get("HEAD_LENGTH"), 6);// 报文头长度
		configParams.put("HEAD_LENGTH", String.valueOf(headLength));
		int messageCodeLength = NumberUtils.toInt((String) configParams.get("MESSAGE_CODE_LENGTH"), 15);// 报文码长度
		configParams.put("MESSAGE_CODE_LENGTH", String.valueOf(messageCodeLength));
		String tranMessageCode = StringUtils.trimToNull((String) configParams.get("MESSAGE_CODE_TRAN"));// 报文码:代付交易
		if (tranMessageCode == null) {
			tranMessageCode = "1002";
			configParams.put("MESSAGE_CODE_TRAN", tranMessageCode);
		}
		String queryMessageCode = StringUtils.trimToNull((String) configParams.get("MESSAGE_CODE_QUERY"));// 报文码:查询交易
		if (queryMessageCode == null) {
			queryMessageCode = "3002";
			configParams.put("MESSAGE_CODE_QUERY", queryMessageCode);
		}
		String messagekey = StringUtils.trimToNull((String) configParams.get("MESSAGE_KEY"));// 报文密钥
		if (messagekey == null) {
			messagekey = "12345678901234567890123456789012";
			configParams.put("MESSAGE_KEY", messagekey);
		}
	}

	/**
	 * 打包
	 * 
	 * @return
	 */
	private byte[] pack() {
		byte[] bytes = null;
		try {
			String charset = StringUtils.trimToNull((String) configParams.get("CHARSET"));// 字符集
			int headLength = NumberUtils.toInt((String) configParams.get("HEAD_LENGTH"), 6);// 报文头长度
			int messageCodeLength = NumberUtils.toInt((String) configParams.get("MESSAGE_CODE_LENGTH"), 15);// 报文码长度
			String tranMessageCode = StringUtils.trimToNull((String) configParams.get("MESSAGE_CODE_TRAN"));// 报文码:代付交易
			String messagekey = StringUtils.trimToNull((String) configParams.get("MESSAGE_KEY"));// 报文密钥

			StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><TRAN_REQ>");
			buffer.append("<COMPANY_ID>").append("CS").append("</COMPANY_ID>");
			buffer.append("<MCHNT_CD>").append("").append("</MCHNT_CD>");
			buffer.append("<TRAN_DATE>").append(DateFormatUtils.format(new Date(), "yyyyMMdd")).append("</TRAN_DATE>");
			buffer.append("<TRAN_TIME>").append(DateFormatUtils.format(new Date(), "HHmmss")).append("</TRAN_TIME>");
			buffer.append("<TRAN_ID>").append(UUID.randomUUID().toString()).append("</TRAN_ID>");
			buffer.append("<CURRENCY>RMB</CURRENCY>");
			buffer.append("<ACC_NO>").append("6226222912345678").append("</ACC_NO>");
			buffer.append("<ACC_NAME>").append("张三").append("</ACC_NAME>");
			buffer.append("<BANK_TYPE>").append("").append("</BANK_TYPE>");
			buffer.append("<BANK_NAME>").append("").append("</BANK_NAME>");
			buffer.append("<TRANS_AMT>").append("").append("</TRANS_AMT>");
			buffer.append("<REMARK>").append("单笔测试").append("</REMARK>");
			buffer.append("<RESV>").append("").append("</RESV>");
			buffer.append("</TRAN_REQ>");

			byte[] bodyBytes = buffer.toString().getBytes(charset);
			String mac = md5(buffer.append(messagekey).toString().getBytes(charset));
			bytes = ArrayUtils.addAll(bytes, StringUtils.leftPad(String.valueOf(bodyBytes.length), headLength, "0").getBytes(charset));
			bytes = ArrayUtils.addAll(bytes, StringUtils.rightPad(tranMessageCode, messageCodeLength, " ").getBytes(charset));
			bytes = ArrayUtils.addAll(bytes, bodyBytes);
			bytes = ArrayUtils.addAll(bytes, mac.getBytes(charset));
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return bytes;
	}

	/**
	 * 解包
	 * 
	 * @param bytes
	 */
	private void unpack(byte[] bytes) {

	}

	/**
	 * MD5加密
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	private String md5(byte[] bytes) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(bytes);
		String result = new BigInteger(1, md.digest()).toString(16);
		result = StringUtils.leftPad(result, 32, "0").toUpperCase();
		return result;
	}
}