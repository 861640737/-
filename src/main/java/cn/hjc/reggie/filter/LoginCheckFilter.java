package cn.hjc.reggie.filter;

import cn.hjc.reggie.common.BaseContext;
import cn.hjc.reggie.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取本次请求的URI
        String requestURI = request.getRequestURI();

        //定义不需要拦截的URI
        String[] uris = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };

        //判断本次请求是否放行
        boolean check = check(uris, requestURI);

        //该请求不需要处理,放行
        if (check) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //判断登录状态,若已登录,则放行
        if (request.getSession().getAttribute("employee") != null) {
            Long employeeId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(employeeId);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (request.getSession().getAttribute("user") != null) {
            Long userId = Long.parseLong((String) request.getSession().getAttribute("user"));
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //未登录,返回未登录结果,通过输出流向客户端页面响应数据
        log.info("用户未登录,该请求已被拦截:" + requestURI);
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径检查,判断当前路径是否需要放行
     * @param uris
     * @param requestURI
     * @return
     */
    private boolean check(String[] uris, String requestURI) {
        for (String uri : uris) {
            boolean match = PATH_MATCHER.match(uri, requestURI);
            if (match) return true;
        }
        return false;
    }

}
