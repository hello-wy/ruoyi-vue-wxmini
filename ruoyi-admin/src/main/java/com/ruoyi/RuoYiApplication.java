package com.ruoyi;

import com.binarywang.spring.starter.wxjava.pay.config.WxPayAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@EnableScheduling
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, WxPayAutoConfiguration.class })
public class RuoYiApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
    }
}
