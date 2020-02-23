package com.lsx.component.mqttbroker.bean;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


//用于多线程 获取bean
public class SpringBean implements ServletContextListener {
    private static WebApplicationContext webApplicationContext = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (webApplicationContext == null)
            webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }


    public static Object getBean(String name){
        return webApplicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> class_){
        return webApplicationContext.getBean(class_);
    }
}
