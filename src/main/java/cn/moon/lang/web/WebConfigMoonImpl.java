package cn.moon.lang.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = WebConfigMoonImpl.class)
public class WebConfigMoonImpl {

    @Bean(name = "moon_spring_tool")
    public SpringTool springTool(){
        return new SpringTool();
    }


}
