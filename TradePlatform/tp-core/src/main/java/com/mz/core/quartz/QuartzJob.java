/**
 * @Description:
 * @Title: QuartzJob.java
 * @Package com.joyce.quartz
 * @Copyright: Copyright (c) 2014
 * @author Comsys-LZP
 * @date 2014-6-26 下午03:37:11
 * @version V2.0
 */
package com.mz.core.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * 任务执行类
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2016年4月20日 下午1:42:22
 */
public class QuartzJob implements Job {

  @Override
  public void execute(JobExecutionContext context) {
    try {
      ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
      TaskUtils.invokMethod(scheduleJob);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
