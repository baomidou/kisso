/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
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
package wang.leq.sso.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import wang.leq.sso.SSOConfig;
import wang.leq.sso.exception.KissoException;

/**
 * KISSO 配置
 * @author   hubin
 * @Date	 2015-02-06
 */
public class WebKissoConfigurer {

	/**
	 * Parameter specifying the location of the kisso config file
	 */
	public static final String CONFIG_LOCATION_PARAM = "kissoConfigLocation";


	private WebKissoConfigurer() {
	}


	public static void initKisso( ServletContext servletContext ) {
		String location = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
		if ( location != null ) {
			if ( location.indexOf("classpath") >= 0 ) {
				String[] cfg = location.split(":");
				if ( cfg.length == 2 ) {
					InputStream in = WebKissoConfigurer.class.getClassLoader().getResourceAsStream(cfg[1]);
					//初始化配置
					SSOConfig.init(getInputStream(servletContext, in));
				}
			} else {
				File file = new File(location);
				if ( file.isFile() ) {
					try {
						SSOConfig.init(getInputStream(servletContext, new FileInputStream(file)));
					} catch ( FileNotFoundException e ) {
						e.printStackTrace();
						throw new KissoException(location);
					}
				} else {
					throw new KissoException(location);
				}
			}
		} else {
			servletContext.log("Initializing is not available kissoConfigLocation on the classpath");
		}
	}


	public static void shutdownKisso( ServletContext servletContext ) {
		servletContext.log("Uninstalling Kisso ");
	}


	private static Properties getInputStream( ServletContext servletContext, InputStream in ) {
		Properties p = null;
		try {
			p = new Properties();
			p.load(in);
		} catch ( Exception e ) {
			servletContext.log(" kisso read config file error. ", e);
		}
		return p;
	}
}
