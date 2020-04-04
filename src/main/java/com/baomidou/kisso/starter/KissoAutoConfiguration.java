package com.baomidou.kisso.starter;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return SSOHelper.setSsoConfig(properties.getConfig());
    }
}
