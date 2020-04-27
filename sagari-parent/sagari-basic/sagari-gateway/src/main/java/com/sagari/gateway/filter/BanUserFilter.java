package com.sagari.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.sagari.common.utils.RegexUtils;
import com.sagari.gateway.entity.Ban;
import com.sagari.gateway.mapper.BanMapper;
import com.sagari.gateway.mapper.UserMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

/**
 * @author biubiupiu~
 */
@Component
public class BanUserFilter extends ZuulFilter {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BanMapper banMapper;
    private Logger log = LoggerFactory.getLogger(BanUserFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        ctx.getZuulRequestHeaders().put("TRUE_IP", request.getRemoteAddr());
        ctx.getZuulRequestHeaders().put("TRUE_HOST", request.getRemoteHost());
        String account = request.getParameter("account");
        Integer userId = null;
        if (StringUtils.isNotBlank(account)) {
            if (RegexUtils.checkMobile(account)) {
                userId = userMapper.getIdByPhone(account);
            } else if (RegexUtils.checkEmail(account)) {
                userId = userMapper.getIdByEmail(account);
            } else {
                userId = userMapper.getIdByUsername(account);
            }
        }
        if (userId != null) {
            Ban ban = banMapper.getBanInfo(userId);
            if (ban != null && ban.getActive()) {
                ctx.setResponseStatusCode(403);
                StringBuffer sb = new StringBuffer("尊敬的用户，您的账号由于");
                sb.append(ban.getReason()).append("，从");
                LocalDateTime time1 = LocalDateTime.ofEpochSecond(ban.getStartTime() / 1000,
                        0, ZoneOffset.ofHours(8));
                String start = time1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                sb.append(start).append("起封禁至");
                LocalDateTime time2 = LocalDateTime.ofEpochSecond(ban.getEndTime() / 1000,
                        0, ZoneOffset.ofHours(8));
                String end = time2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                sb.append(end).append("，封禁期间您的账号无法登录");
                JSONObject result = new JSONObject();
                result.put("code", 403);
                result.put("msg", sb.toString());
                result.put("data", "");
                ctx.getResponse().setContentType("application/json;charset=UTF-8");
                ctx.setResponseBody(result.toJSONString());
                ctx.setSendZuulResponse(false);
            }
        }
        return null;
    }
}
