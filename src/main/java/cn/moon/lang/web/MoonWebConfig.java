package cn.moon.lang.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoonWebConfig {

    @Bean(name = "moon_spring_tool")
    public SpringTool springTool(){
        return new SpringTool();
    }
}
