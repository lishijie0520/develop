package com.lsj.cmbc.scoket;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * <strong>Title : TestSocketClient</strong><br>
 * <strong>Description : TCP客户端测试类</strong><br>
 * <strong>Create on : 2015-9-30</strong><br>
 * 
 * @author linda1@cmbc.com.cn<br>
 */
public class TestSocketClient {
	public static void main(String[] args) {
		LinkedBlockingQueue<byte[]> sendQueue = new LinkedBlockingQueue<byte[]>();
		LinkedBlockingQueue<byte[]> receiveQueue = new LinkedBlockingQueue<byte[]>();

		SocketAsyncLongOutputAdapter adapter = new SocketAsyncLongOutputAdapter();
		adapter.setSendQueue(sendQueue);
		adapter.setReceiveQueue(receiveQueue);
		adapter.start();

		MessageHandler handler = new MessageHandler();
		handler.setSendQueue(sendQueue);
		handler.setReceiveQueue(receiveQueue);
		handler.start();
	}
}
