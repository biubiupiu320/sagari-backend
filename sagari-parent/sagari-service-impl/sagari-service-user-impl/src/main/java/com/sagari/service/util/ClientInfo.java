package com.sagari.service.util;

import com.alibaba.fastjson.JSONObject;
import com.sagari.service.entity.SignIn;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author biubiupiu~
 */
@Component
public class ClientInfo {

    @Value("${sign-in.history.system}")
    private String system;
    private String cacheSystem;
    private JSONObject jsonObject;
    private static Pattern[] patterns = new Pattern[5];

    static {
        patterns[0] = Pattern.compile("(?i)Msie [\\d.]+");
        patterns[1] = Pattern.compile("(?i)Firefox\\/[\\d.]+");
        patterns[2] = Pattern.compile("(?i)Chrome\\/[\\d.]+");
        patterns[3] = Pattern.compile("(?i)Safari\\/[\\d.]+");
        patterns[4] = Pattern.compile("(?i)Opera\\/[\\d.]+");
    }

    public void getClientInfo(HttpServletRequest request, SignIn signIn) {
        getIpAddr(request, signIn);
        getBrowser(request, signIn);
        getSystem(request, signIn);
        getDeviceName(request, signIn);
    }

    private void getIpAddr(HttpServletRequest request, SignIn signIn) {
        String ip = request.getHeader("TRUE_IP");
        String unknown = "unknown";
        if (StringUtils.isNotBlank(ip) && !unknown.equalsIgnoreCase(ip)) {
            signIn.setIp(ip);
            return;
        }
        ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-for");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-Ip");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        int index = ip.indexOf(",");
        if (index != -1) {
            signIn.setIp(ip.substring(0, index));
        } else {
            signIn.setIp(ip);
        }
    }

    private void getBrowser(HttpServletRequest request, SignIn signIn) {
        String userAgent = request.getHeader("user-agent");
        signIn.setBrowser("未知");
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(userAgent);
            if (matcher.find()) {
                signIn.setBrowser(matcher.group().replace("/", " "));
                break;
            }
        }
    }

    private void getSystem(HttpServletRequest request, SignIn signIn) {
        String userAgent = request.getHeader("user-agent");
        signIn.setSystem("未知");
        if (!system.equals(cacheSystem)) {
            jsonObject = JSONObject.parseObject(system);
            cacheSystem = system;
        }
        for (String s : jsonObject.keySet()) {
            if (userAgent.indexOf(s) > 0) {
                signIn.setSystem(jsonObject.getString(s));
                break;
            }
        }
    }

    private void getDeviceName(HttpServletRequest request, SignIn signIn) {
        String device = request.getHeader("TRUE_HOST");
        if (!StringUtils.isBlank(device)) {
            int index = device.indexOf(':');
            if (index > 0) {
                signIn.setDevice(device.substring(0, index));
            } else {
                signIn.setDevice(device);
            }
        } else {
            signIn.setDevice("未知");
        }
    }
}
