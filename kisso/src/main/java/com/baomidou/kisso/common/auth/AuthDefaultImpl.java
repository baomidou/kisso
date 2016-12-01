package com.baomidou.kisso.common.auth;

import com.baomidou.kisso.SSOAuthorization;
import com.baomidou.kisso.Token;

/**
 * 用户不自定义权限，默认用此类来代替
 * @author 20160405
 *
 */
public class AuthDefaultImpl implements SSOAuthorization {

	@Override
	public boolean isPermitted(Token token, String permission) {
		return true;
	}

}
