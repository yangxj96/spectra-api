package com.yangxj96.spectra.quartz.configuration;

import com.yangxj96.spectra.quartz.job.ExampleJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Quartz相关配置
 *
 * @author 杨新杰
 * @since 2025/3/24 10:49
 */
@Slf4j
@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail exampleJobDetail() {
        log.atInfo().log("ExampleJobDetail");
        return JobBuilder.newJob(ExampleJob.class)
                .withIdentity("exampleJob", "exampleJobGroup")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger exampleJobTrigger() {
        log.atInfo().log("exampleJobTrigger");
        var schedule = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(10)  // 每10秒执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(exampleJobDetail())
                .withIdentity("exampleJobTrigger")
                .withSchedule(schedule)
                .withDescription("SimpleScheduleBuilder")
                .build();
    }

    @Bean
    public Trigger cronJobTrigger() {
        log.atInfo().log("cronJobTrigger");
        var schedule = CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *");
        return TriggerBuilder.newTrigger()
                .forJob(exampleJobDetail())
                .withIdentity("cronJobTrigger")
                .withSchedule(schedule)  // 每分钟执行一次
                .withDescription("CronScheduleBuilder")
                .build();
    }
}
