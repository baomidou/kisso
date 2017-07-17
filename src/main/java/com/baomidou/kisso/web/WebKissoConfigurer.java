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
package com.baomidou.kisso.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.exception.KissoException;

/**
 * <p>
 * Kisso 配置
 * </p>
 * 
 * @author hubin
 * @Date 2015-02-06
 */
public class WebKissoConfigurer extends SSOConfig {

	/**
	 * Parameter specifying the location of the kisso config file
	 */
	protected static final Logger logger = Logger.getLogger("WebKissoConfigurer");
	public static final String CONFIG_LOCATION_PARAM = "kissoConfigLocation";
	private String ssoPropPath = "sso.properties";

	public WebKissoConfigurer() {
		/* 支持无参构造函数 */
	}
	
	public WebKissoConfigurer(String ssoPropPath) {
		this.ssoPropPath = ssoPropPath;
	}

	/**
	 * 
	 * web.xml 启动监听, 初始化
	 * 
	 * @param servletContext
	 * 
	 */
	public void initKisso(ServletContext servletContext) {
		String location = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
		if (location != null) {
			if (location.indexOf("classpath") >= 0) {
				String[] cfg = location.split(":");
				if (cfg.length == 2) {
					this.initProperties(getInputStream(cfg[1]));
				}
			} else {
				File file = new File(location);
				if (file.isFile()) {
					try {
						this.initProperties(getInputStream(new FileInputStream(file)));
					} catch (FileNotFoundException e) {
						throw new KissoException(location, e);
					}
				} else {
					throw new KissoException(location);
				}
			}
		} else {
			servletContext.log("Initializing is not available kissoConfigLocation on the classpath");
		}
	}
	

	/**
	 * 
	 * Spring bean 注入初始化
	 * <p>
	 * xml 配置方法：
	 * 
	 * <bean id="kissoInit" class="com.baomidou.kisso.web.WebKissoConfigurer" init-method="initKisso">
	 * 		<property name="ssoPropPath" value="sso.properties" />
	 * </bean>
	 * </p>
	 * 
	 */
	public void initKisso() {
		Properties prop = null;
		
		/* 尝试文件读取 */
		File file = new File(this.getSsoPropPath());
		if ( file.isFile() ) {
			try {
				prop = getInputStream(new FileInputStream(file));
			} catch ( FileNotFoundException e ) {
				throw new KissoException(this.getSsoPropPath(), e);
			}
		} else {
			prop = getInputStream(this.getSsoPropPath());
		}
		
		/**
		 * 初始化
		 */
		if ( prop != null ) {
			this.initProperties(prop);
		} else {
			logger.severe("Initializing is not available kissoConfigLocation on the classpath");
		}
	}

	public void shutdownKisso() {
		logger.info("Uninstalling Kisso ");
	}


	private Properties getInputStream( String cfg ) {
		return getInputStream(WebKissoConfigurer.class.getClassLoader().getResourceAsStream(cfg));
	}


	private Properties getInputStream( InputStream in ) {
		Properties p = null;
		try {
			p = new Properties();
			p.load(in);
		} catch ( Exception e ) {
			logger.severe(" kisso read config file error. \n" + e.toString());
		}
		return p;
	}
	
	
	public String getSsoPropPath() {
		return ssoPropPath;
	}

	public void setSsoPropPath( String ssoPropPath ) {
		this.ssoPropPath = ssoPropPath;
	}
	
}
