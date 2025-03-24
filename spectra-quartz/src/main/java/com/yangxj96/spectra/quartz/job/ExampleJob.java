package com.yangxj96.spectra.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * 示例job
 *
 * @author 杨新杰
 * @since 2025/3/24 10:45
 */
@Slf4j
@Component
public class ExampleJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.atInfo().log("ExampleJob executed,${}", jobExecutionContext.getJobDetail().getDescription());
    }


}
