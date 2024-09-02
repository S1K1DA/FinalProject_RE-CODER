package com.heartlink.common.scheduler;

import com.heartlink.board.model.service.NoticeService;
import com.heartlink.feed.model.service.FeedService;
import com.heartlink.review.model.service.ReviewService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class PostManagementScheduler {

    private final PostSchedulerService postSchedulerService;
    private static final Logger logger = LogManager.getLogger(PostManagementScheduler.class);

    public PostManagementScheduler(PostSchedulerService postSchedulerService){
        this.postSchedulerService = postSchedulerService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void postScheduledTask(){

        LocalDate todayLocal = LocalDate.now();
        String today = todayLocal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        try {
            deleteFeedsMakedAsDeleted(today);

            deleteFeedCommentsMakedAsDeleted(today);

            deleteReviewsMakedAsDeleted(today);

            deleteNotiecsMakedAsDeleted(today);

        } catch (Exception e){
            logger.warn("PostManagementScheduler error : " + e);
        }
    }

    // feed
    private void deleteFeedsMakedAsDeleted(String today){

        String result = postSchedulerService.deleteFeedsMakedAsDeleted(today);

        logger.info(result);
    }

    // feed-comments
    private void deleteFeedCommentsMakedAsDeleted(String today){

        String result = postSchedulerService.deleteFeedCommentsMakedAsDeleted(today);

        logger.info(result);
    }

    // review
    private void deleteReviewsMakedAsDeleted(String today){

        String result = postSchedulerService.deleteReviewsMakedAsDeleted(today);

        logger.info(result);
    }

    // notice
    private void deleteNotiecsMakedAsDeleted(String today){

        String result = postSchedulerService.deleteNotiecsMakedAsDeleted(today);

        logger.info(result);
    }

}
