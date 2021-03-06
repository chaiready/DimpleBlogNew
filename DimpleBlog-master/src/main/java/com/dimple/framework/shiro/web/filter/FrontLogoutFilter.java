package com.dimple.framework.shiro.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;

import com.dimple.common.constant.Constants;
import com.dimple.common.utils.MessageUtils;
import com.dimple.common.utils.StringUtils;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.framework.manager.AsyncManager;
import com.dimple.framework.manager.factory.AsyncFactory;
import com.dimple.project.system.user.domain.User;

import lombok.extern.slf4j.Slf4j;

/**
 * @className: LogoutFilter
 * @description: 退出过滤器
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
@Slf4j
public class FrontLogoutFilter extends org.apache.shiro.web.filter.authc.LogoutFilter {

    /**
     * 退出后重定向的地址
     */
    private String loginUrl;

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
    
    
    public FrontLogoutFilter() {
  	}
    public FrontLogoutFilter(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	@Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        try {
            Subject subject = getSubject(request, response);
            String redirectUrl = "";//getRedirectUrl(request, response, subject);
            try {
                String toPage = request.getParameter("toPage");
                User user = ShiroUtils.getSysUser();
                String loginName = "";
                if (StringUtils.isNotNull(user)) {
                  loginName = user.getLoginName();
                }
                if(StringUtils.isEmpty(toPage)){
                  redirectUrl = "/bbs/"+loginName+".html";
                }else{
                    redirectUrl = "/"+toPage;
                }
                // 记录用户退出日志
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
                // 退出登录
                subject.logout();
            } catch (SessionException ise) {
                log.error("logout fail.", ise);
            }
            issueRedirect(request, response, redirectUrl);
        } catch (Exception e) {
            log.error("Encountered session exception during logout.  This can generally safely be ignored.", e);
        }
        return false;
    }

    /**
     * 退出跳转URL
     */
    @Override
    protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject) {
        String url = getLoginUrl();
        if (StringUtils.isNotEmpty(url)) {
            return url;
        }
        return super.getRedirectUrl(request, response, subject);
    }
}
