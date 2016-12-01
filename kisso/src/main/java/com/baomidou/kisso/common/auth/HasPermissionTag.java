package com.baomidou.kisso.common.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.SSOToken;

public class HasPermissionTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2132027302470928774L;
	
	private String name; //权限码名称
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
    public int doStartTag() throws JspException { // 在标签开始处出发该方法
        HttpServletRequest request=(HttpServletRequest) pageContext.getRequest();
        SSOToken token = SSOHelper.getToken(request);
        // 如果 token 或者 name 为空
		if (token != null && name != null && !"".equals(name.trim())) {
			boolean result = SSOConfig.getInstance().getAuthorization().isPermitted(token, name);
			if (result) {
				//权限验证通过
				return BodyTagSupport.EVAL_BODY_INCLUDE;// 返回此则执行标签body中内容，SKIP_BODY则不执行
			}
		} 
		return BodyTagSupport.SKIP_BODY;
    }

}
