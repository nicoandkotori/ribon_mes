package com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {

    public final static String tempFilePath = System.getProperty("user.dir") + "/import/temp/";

    /**
     * 添加外部静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/qcProcessImages" + "/**").addResourceLocations("file:" + System.getProperty("user.dir") + "/upload/qcProcessImages/");
        registry.addResourceHandler("/invAttrUpload" + "/**").addResourceLocations("file:" + System.getProperty("user.dir") + "/upload/invAttrUpload/");
    }

    /**
     * 添加跨域请求支持
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);

    }
}
