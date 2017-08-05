/**
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.kisso.common.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.security.token.SSOToken;

/**
 * <p>
 * JSP 权限判断标签
 * </p>
 * <p>
 * 使用 JSP 引用：<%@ taglib prefix="kisso" uri="http://kisso.baomidou.com/tags"%><br>
 * <kisso:hasPermission name="需要验证的内容">判断内容<kisso:hasPermission><br>
 * </p>
 *
 * @author 441558032@qq.com
 * @since 2016-12-15
 */
public class HasPermissionTag extends BodyTagSupport {

    private String name; // 权限码名称

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int doStartTag() throws JspException {
        // 在标签开始处出发该方法
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        SSOToken token = SSOHelper.getSSOToken(request);
        // 如果 token 或者 name 为空
        if (token != null && this.getName() != null && !"".equals(this.getName().trim())) {
            boolean result = SSOConfig.getInstance().getAuthorization().isPermitted(token, this.getName());
            if (result) {
                // 权限验证通过
                // 返回此则执行标签body中内容，SKIP_BODY则不执行
                return BodyTagSupport.EVAL_BODY_INCLUDE;
            }
        }
        return BodyTagSupport.SKIP_BODY;
    }

}
