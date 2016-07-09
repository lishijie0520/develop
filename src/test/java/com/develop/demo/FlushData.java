package com.develop.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlushData {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private  static final Map<String, Object> configParams = new HashMap<String, Object>(); 
	
	private final   List list = new ArrayList<String>();
	
	private boolean b  = true;
	
//	public int getAge() {
//		return age;
//	}

	public void init() {
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
						int interval = NumberUtils.toInt((String) configParams.get("REFRESH_INTERVAL"), 1) * 1000;// 刷新间隔，单位：秒
						logger.info("睡眠中。。。"+interval);
						Thread.sleep(interval);
					} catch (Exception e) {
						logger.error(e.getLocalizedMessage(), e);
					}
				}
			}
		}).start();
	}
	
	private void refresh() {
		configParams.put("name", "lishijie");
		configParams.put("age", "118");
		list.add("dd");
		b  = false;
		//list = new ArrayList<String>();
	
	}
	public Object set(String key, Object value) {
		return configParams.put(key, value);
	}
	public Object getObject(String key) {
		return configParams.get(key);
	}
	public String getString(String key) {
		Object obj = configParams.get(key);
		if (obj == null) {
			return null;
		}
		return StringUtils.trimToNull(String.valueOf(configParams.get(key)));
	}
}
