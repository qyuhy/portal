package com.portal.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.portal.annotation.Log;
import com.portal.service.system.SpringService;

public class LoggerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("after-->");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        System.out.println("after before-->");
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AnnotationMethodHandlerAdapter adapter = (AnnotationMethodHandlerAdapter) SpringService
                .getBeanByName("annotationMethodHandlerAdapter");
        Class<?> clz = adapter.getClass();
        Method method = clz.getDeclaredMethod("getMethodResolver", Object.class);
        method.setAccessible(true);
        Object servletHandlerMethodResolver = method.invoke(adapter, handler);
        Class<?> resolverClz = servletHandlerMethodResolver.getClass();
        Method resolveMethod = resolverClz.getDeclaredMethod("resolveHandlerMethod", HttpServletRequest.class);
        resolveMethod.setAccessible(true);
        Method currMethod = (Method) resolveMethod.invoke(servletHandlerMethodResolver, request);
        if (currMethod.isAnnotationPresent(Log.class)) {
            Log log = currMethod.getAnnotation(Log.class);
            System.out.println("找到日志说明-->" + log.value());
        }
        return super.preHandle(request, response, handler);
    }

}
