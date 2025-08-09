package com.baomidou.kisso.starter;

import com.baomidou.kisso.SSOConfig;
import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.SSOPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(KissoProperties.class)
public class KissoAutoConfiguration {

    /**
     * 注入初始化
     */
    @Bean
    @ConditionalOnMissingClass
    public SSOConfig ssoConfig(@Autowired(required = false) List<SSOPlugin> ssoPluginList,
                               KissoProperties properties) {
        SSOConfig ssoConfig = properties.getConfig();
        if (null == ssoConfig) {
            ssoConfig = SSOHelper.getSsoConfig();
        }
        return ssoConfig.setPluginList(ssoPluginList);
    }
}
