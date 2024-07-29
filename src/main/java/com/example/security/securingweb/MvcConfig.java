package com.example.security.securingweb;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer { //웹 애플리케이션은 Spring MVC를 기반으로 한다. 뷰 컨트롤러를 설정하기 위한 클래스

    //별도의 Controller 클래스 없이 특정 view에 대한 컨트롤러를 추가할 수 있는 메서드입니다.
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home"); // /home url이 요청되면 home이라는 view Object로 변환하여 home이라는 page로 이동합니다.
        registry.addViewController("/").setViewName("home"); //시작 페이지를 /가 아닌 home으로.
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login"); // /login url이 요청되면 login이라는 View object로 변환하여 이동하게 해줍니다.
    }
}
