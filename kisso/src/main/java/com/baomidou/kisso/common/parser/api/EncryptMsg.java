/**
 * Copyright (c) 2011-2014, hubin (jobob@qq.com).
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
package com.baomidou.kisso.common.parser.api;

/**
 * <p>
 * API 加密消息实体类
 * </p>
 * 
 * @author hubin
 * @Date 2015-01-09
 */
public class EncryptMsg {
	/*
	 * 加密内容
	 */
	private String encrypt;

	/*
	 * 消息签名
	 */
	private String msgSignature;

	/*
	 * 时间戳
	 */
	private String timeStamp;

	/*
	 * 随机混淆参数
	 */
	private String nonce;

	public EncryptMsg() {

	}

	public EncryptMsg(String encrypt, String signature, String timestamp, String nonce) {
		this.encrypt = encrypt;
		this.msgSignature = signature;
		this.timeStamp = timestamp;
		this.nonce = nonce;
	}

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}

	public String getMsgSignature() {
		return msgSignature;
	}

	public void setMsgSignature(String msgSignature) {
		this.msgSignature = msgSignature;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

}