package com.baomidou.kisso.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.kisso.SSOConfig;

@Configuration
@EnableConfigurationProperties(KissoProperties.class)
public class KissoAutoConfiguration {

    @Autowired
    private KissoProperties properties;

    /**
     * 注入初始化
     */
    @Bean
    public SSOConfig getInstance() {
        return SSOConfig.init(properties.getConfig());
    }
}
