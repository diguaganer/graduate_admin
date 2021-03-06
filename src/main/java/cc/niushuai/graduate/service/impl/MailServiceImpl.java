package cc.niushuai.graduate.service.impl;

import cc.niushuai.graduate.commons.constant.Constant;
import cc.niushuai.graduate.commons.utils.CronDateUtils;
import cc.niushuai.graduate.commons.utils.MailSendHelper;
import cc.niushuai.graduate.quartz.job.MailSendJob;
import cc.niushuai.graduate.service.MailService;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 邮件发送实现类
 *
 * @author niushuai
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Value("${spring.mail.username}")
    private String username;


    /**
     * 定时发送
     *
     * @param emails
     * @param title
     * @param content
     * @param sendTime
     * @return
     */
    @Override
    public boolean sendMailTiming(String[] emails, String title, String content, String sendTime) {
        try {
            String uuid = Constant.randomUUID;
            JobDetail jobDetail = JobBuilder.newJob(MailSendJob.class)
                    .withIdentity(uuid, Scheduler.DEFAULT_GROUP)
                    .build();

            String cron = CronDateUtils.getCron(sendTime);
            System.out.println(cron);
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(uuid)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .build();
            JobDataMap jobDataMap = cronTrigger.getJobDataMap();
            jobDataMap.put("emails", emails);
            jobDataMap.put("title", title);
            jobDataMap.put("content", content);
            jobDataMap.put("from", username);

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            Date date = scheduler.scheduleJob(jobDetail, cronTrigger);
            scheduler.start();
            log.info("邮件发送任务创建成功, 执行时间：{}", DateUtil.formatDateTime(date));

            return true;
        } catch (Exception e) {
            log.error("创建邮件发送任务失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 立即发送
     *
     * @param emails
     * @param title
     * @param content
     * @return
     */
    @Override
    public boolean sendMailImmediately(String[] emails, String title, String content) {
        try {
            MailSendHelper.sendMimeMessage(javaMailSender, emails, title, content, username);
            return true;
        } catch (Exception e) {
            log.error("立即发送邮件失败: {}", e.getMessage());
            return false;
        }
    }

}
