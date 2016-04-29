/**
 * Copyright (c) 2011-2014, hubin (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.kisso.common;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 * <p>
 * Properties 辅助工具类
 * </p>
 * 
 * @author hubin
 * @Date 2014-5-12
 */
public class SSOProperties {

	private static final Logger logger = Logger.getLogger("SSOProperties");

	private final Properties properties;

	public SSOProperties(Properties properties) {
		this.properties = properties;
	}

	public SSOProperties(Properties mergeProperties, String runMode) {
		this.properties = extractRunMode(mergeProperties, runMode);
	}
	
	public SSOProperties(Properties mergeProperties, String runMode, String currentMode) {
		this.properties = extractRunMode(mergeProperties, runMode, currentMode);
	}

	public String get(String key) {
		return properties.getProperty(key);
	}


	/**
	 * properties 提取当前模式配置 
	 * -------------------- 
	 * dev_mode 开发模式 
	 * test_mode 测试模式
	 * online_mode 生产模式 
	 * -------------------- 
	 * eclipse 开发模式配置，启动参数 Arguments 属性 VM
	 * arguments 设置 -Dsso.run.mode=dev_mode
	 */
	public static Properties extractRunMode( Properties prop, String runMode ) {
		return extractRunMode(prop, runMode, "online_mode");
	}


	/**
	 * <p>
	 * properties 提取当前模式配置
	 * </p>
	 * 
	 * @param prop
	 *            配置文件 Properties
	 * @param runMode
	 *            当前配置模式
	 * @param defaultMode
	 *            默认模式
	 * @return
	 */
	public static Properties extractRunMode( Properties prop, String runMode, String defaultMode ) {
		if ( prop == null || runMode == null || defaultMode == null ) {
			return null;
		}

		/**
		 * 获取路由规则, 系统属性设置mode优先
		 */
		Properties properties = new Properties();
		String mode = System.getProperty(runMode);
		if ( mode == null ) {
			String str = prop.getProperty(runMode);
			mode = (str != null) ? str : defaultMode;
		}
		logger.info("sso.run.mode=" + mode);
		properties.put(runMode, mode);
		Set<Entry<Object, Object>> es = prop.entrySet();
		for ( Entry<Object, Object> entry : es ) {
			String key = (String) entry.getKey();
			int idx = key.lastIndexOf("_mode");
			String realKey = key;
			if ( idx > 0 ) {
				if ( key.contains(mode) ) {
					realKey = key.substring(0, key.lastIndexOf("_" + mode));
				} else {
					realKey = null;
				}
			}
			if ( realKey != null && !properties.containsKey(realKey) ) {
				Object value = null;
				if ( idx > 0 ) {
					value = prop.get(realKey + "_" + mode);
				} else {
					value = prop.get(realKey);
				}
				if ( value != null ) {
					properties.put(realKey, value);
				} else {
					throw new RuntimeException("impossible empty property for " + realKey);
				}
			}
		}
		return properties;
	}


	public String get( String key, String defaultVal ) {
		String val = this.get(key);
		return val == null ? defaultVal : val;
	}


	public String findValue( String... keys ) {
		for ( String key : keys ) {
			String value = this.get(key);
			if ( value != null ) {
				return value;
			}
		}
		return null;
	}


	public boolean getBoolean( String key, boolean defaultVal ) {
		String val = this.get(key);
		return val == null ? defaultVal : Boolean.parseBoolean(val);
	}


	public long getLong( String key, long defaultVal ) {
		String val = this.get(key);
		return val == null ? defaultVal : Long.parseLong(val);
	}


	public int getInt( String key, int defaultVal ) {
		return (int) getLong(key, defaultVal);
	}


	public double getDouble( String key, double defaultVal ) {
		String val = this.get(key);
		return val == null ? defaultVal : Double.parseDouble(val);
	}


	public <T extends Enum<T>> T getEnum( String key, Class<T> type, T defaultValue ) {
		String val = this.get(key);
		return val == null ? defaultValue : Enum.valueOf(type, val);
	}

}
