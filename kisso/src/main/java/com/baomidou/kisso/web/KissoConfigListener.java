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
package com.baomidou.kisso.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * <p>
 * Kisso 配置启动监听
 * </p>
 * 
 * @author hubin
 * @Date 2015-02-06
 */
public class KissoConfigListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
		WebKissoConfigurer.initKisso(sce.getServletContext());
	}

	public void contextDestroyed(ServletContextEvent sce) {
		WebKissoConfigurer.shutdownKisso(sce.getServletContext());
	}

}
