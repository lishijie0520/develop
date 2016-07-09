package com.develop.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GetDataTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Before
	public void init(){
	   final FlushData flushData = new FlushData();
	   logger.info("2222");
	   flushData.init();
	  
	}
	@Test
	public void test() throws InterruptedException{
	
		FlushData flushData = new FlushData();
		flushData.set("name", "sss");
		String name = flushData.getString("name");
		logger.info(name); 
		Thread.sleep(6000);
		
	}

}
