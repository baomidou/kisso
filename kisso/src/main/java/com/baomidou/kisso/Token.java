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
package com.baomidou.kisso;

import java.io.Serializable;

/**
 * <p>
 * SSO 票据顶级父类
 * </p>
 * 
 * @author hubin
 * @Date 2015-12-29
 */
@SuppressWarnings("serial")
public class Token implements Serializable {
	/* 正常 */
	public final static int FLAG_NORMAL = 0;

	/* 缓存宕机 */
	public final static int FLAG_CACHE_SHUT = 1;
	
	/* 系统名称  */
	private String app;

	/* 用户 ID（长整型） */
	private Long id;
	
	/* 用户 ID（字符串类型，默认 fastjson 无值该参数不参与 json 序列化） */
	private String uid;

	/* 登录 IP */
	private String ip;

	/* 创建 token 当前系统时间 */
	private long time = System.currentTimeMillis();
	
	/**
	 * Token 状态标示
	 * <p>
	 * 默认正常执行
	 * </p>
	 */
	private int flag = FLAG_NORMAL;

	
	public String getApp() {
		return app;
	}
	
	public void setApp( String app ) {
		this.app = app;
	}
	
	
	public Long getId() {
		return id;
	}

	
	public void setId( Long id ) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid( String uid ) {
		this.uid = uid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public long getTime() {
		return time;
	}

	public void setTime( long time ) {
		this.time = time;
	}
	
	/**
	 * <p>
	 * Token转为JSON格式
	 * </p>
	 * 
	 * @return JSON格式Token值
	 */
	public String jsonToken() {
		return SSOConfig.getInstance().getParser().toJson(this);
	}

	/**
	 * <p>
	 * JSON格式Token值转为Token对象
	 * </p>
	 * 
	 * @param jsonToken
	 *            JSON格式Token值
	 * @return Token对象
	 */
	public Token parseToken(String jsonToken) {
		return SSOConfig.getInstance().getParser().parseObject(jsonToken, this.getClass());
	}
	
	/**
	 * <p>
	 * AuthToken 转为字符串密文
	 * </p>
	 * 
	 * @return AuthToken 密文信息 {@link AuthToken}
	 */
	public String encryptAuthToken() {
		try {
			SSOConfig sc = SSOConfig.getInstance();
			return sc.getEncrypt().encrypt(jsonToken(), sc.getAuthCookieSecretkey());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 生成 Token 缓存主键
	 */
	public String toCacheKey() {
		if (this.getId() != null) {
			return SSOConfig.toCacheKey(this.getId());
		}
		return SSOConfig.toCacheKey(this.getUid());
	}
	
}
