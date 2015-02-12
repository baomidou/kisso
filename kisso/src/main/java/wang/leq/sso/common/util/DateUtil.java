/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wang.leq.sso.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 * <p>
 * @author   hubin
 * @Date	 2014-5-12 	 
 */
public class DateUtil {

	/**
	 * @Description 获取当前中国时区的TIMESTAMP日期
	 * @return
	 */
	public static Timestamp getSysTimestamp() {
		final TimeZone zone = TimeZone.getTimeZone("GMT+8");//获取中国时区
		TimeZone.setDefault(zone);//设置时区
		return new Timestamp((new java.util.Date()).getTime());
	}

	/**
	 * 格式日期为字符串内容
	 * <p>
	 * @param date 		时间
	 * @param pattern 	日期格式,例： yyyyMMddHHmmss
	 * @return String	格式后的字符串日期
	 */
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 格式long类型日期为 Date
	 * <p>
	 * @param time	long类型日期
	 * @return Date
	 */
	public static Date formatDate(long time) {
		return new Date(time);
	}

}
