package com.yangxj96.spectra.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 项目启动类
 *
 * @author 杨新杰
 * @since 2025/3/21 15:20
 */
@ComponentScan("com.yangxj96.spectra.*.configuration")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpectraApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpectraApplication.class, args);
    }

}
