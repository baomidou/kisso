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
package wang.leq.sso.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.leq.sso.common.encrypt.MD5;


/**
 * 文件 Hash 帮助类
 * <p>
 * @author   hubin
 * @Date	 2014-8-11
 */
public class FileHashHelper {
	private final static Logger logger = LoggerFactory.getLogger(FileHashHelper.class);
	
	/**
	 * 获取文件文件MD5 Hash值
	 * <p>
	 * @param pathName
	 * 				文件绝对地址
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String getMD5Hash(String fileName) throws NoSuchAlgorithmException, IOException{
		return getHash(fileName, "MD5");
	}
	
	/**
	 * 获取文件文件MD5 Hash值
	 * <p>
	 * @param InputStream
	 * 				文件输入流
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMD5Hash(InputStream ins) throws NoSuchAlgorithmException, IOException{
		return getHash(ins, "MD5");
	}

	/**
	 * 获取文件文件Hash值
	 * <p>
	 * @param pathName
	 * 				文件绝对地址
	 * @param hashType
	 * 				MessageDigest 加密算法
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getHash(String pathName, String hashType) throws IOException, NoSuchAlgorithmException {
		File f = new File(pathName);
		logger.debug(" -------------------------------------------------------------------------------");
		logger.debug("|当前文件名称:" + f.getName());
		logger.debug("|当前文件大小:" + (f.length() / 1024 / 1024) + "MB");
		logger.debug("|当前文件路径[绝对]:" + f.getAbsolutePath());
		logger.debug("|当前文件路径[---]:" + f.getCanonicalPath());
		logger.debug("|当前文件最后一次被修改时间[---]:" + f.lastModified());
		logger.debug(" -------------------------------------------------------------------------------");
		return getHash(new FileInputStream(f), hashType);
	}
	
	/**
	 * 获取文件文件Hash值
	 * <p>
	 * @param InputStream
	 * 				文件输入流
	 * @param hashType
	 * 				MessageDigest 加密算法
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getHash(InputStream ins, String hashType) throws IOException, NoSuchAlgorithmException {
		if(ins == null){
			//输入流为空返回 null
			return null;
		}
		byte[] buffer = new byte[8192];
		MessageDigest md5 = MessageDigest.getInstance(hashType);
		int len;
		while ((len = ins.read(buffer)) != -1) {
			md5.update(buffer, 0, len);
		}
		ins.close();
		return MD5.byte2Hex(md5.digest());
	}

	/**
	 * 获取MessageDigest支持几种加密算法
	 * SHA-256   SHA-512   SHA   SHA-384   SHA1   MD5   SHA-1   MD2
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private static String[] getCryptolmpls(String serviceType) {
		Set result = new HashSet();
		//all providers
		Provider[] providers = Security.getProviders();
		for (int i = 0; i < providers.length; i++) {
			//get services provided by each provider
			Set keys = providers[i].keySet();
			for (Iterator it = keys.iterator(); it.hasNext();) {
				String key = it.next().toString();
				key = key.split(" ")[0];
				if (key.startsWith(serviceType + ".")) {
					result.add(key.substring(serviceType.length() + 1));
				} else if (key.startsWith("Alg.Alias." + serviceType + ".")) {
					result.add(key.substring(serviceType.length() + 11));
				}
			}
		}
		return (String[]) result.toArray(new String[result.size()]);
	}
	
	/**
	 * 测试
	 */
	public static void main(String[] args) throws Exception {
		//MessageDigest 支持的加密算法
        String[] names = getCryptolmpls("MessageDigest");
        System.out.println("MessageDigest 支持的加密算法: ");
        for(String name:names){
            System.out.println(name);
        }
        
		long start = System.currentTimeMillis();
		System.out.println("开始计算文件MD5值,请稍后...");
		System.out.println("MD5:" + getMD5Hash("E:/Office_Visio_Pro_2007.iso"));
		long end = System.currentTimeMillis();
		System.out.println("一共耗时:" + (end - start) + "毫秒");
	}
}
