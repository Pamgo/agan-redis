package com.agan.redis.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Configuration
public class SessionCofig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityInterceptor())
                //排查拦截的2个路径
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/logout")

                //拦截所有URL路径
                .addPathPatterns("/**");
    }


    @Configuration
    public class SecurityInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            HttpSession session = request.getSession();
            //验证当前session是否存在，存在返回true true代表能正常处理业务逻辑
            if (session.getAttribute(session.getId()) != null){
                log.info("session拦截器，session={}，验证通过",session.getId());
                return true;
            }
            //session不存在，返回false，并提示请重新登录。
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write("请登录！！！！！");
            log.info("session拦截器，session={}，验证失败",session.getId());
            return false;
        }
    }
}
