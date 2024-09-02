package com.heartlink.common.scheduler;

import com.heartlink.board.model.mapper.NoticeMapper;
import com.heartlink.feed.model.mapper.FeedMapper;
import com.heartlink.review.model.dao.ReviewDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class PostSchedulerService {

    private final PostSchedulerMapper postSchedulerMapper;

    public PostSchedulerService(PostSchedulerMapper postSchedulerMapper){
        this.postSchedulerMapper = postSchedulerMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public String deleteFeedsMakedAsDeleted(String today){
        List<Integer> deleteFeedList = postSchedulerMapper.getDeleteFeedList(today);
        int deleteListCnt = deleteFeedList.size();

        int resultNum = 0;

        if(!Objects.isNull(deleteFeedList)){
            for(int feedNo : deleteFeedList){
                postSchedulerMapper.deleteFeedLikes(feedNo);
                postSchedulerMapper.deleteFeedComments(feedNo);
                resultNum += postSchedulerMapper.setFeedDelete(feedNo);
            }

            return "삭제 예정 Feed" +
                    deleteListCnt +
                    "개, 최종" +
                    resultNum +
                    "개 삭제 완료";
        }else{
            return "삭제 피드 없음";
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public String deleteFeedCommentsMakedAsDeleted(String today){
        List<Integer> deleteCommnetList = postSchedulerMapper.getDeleteCommentList(today);
        int deleteCommnetListCnt = deleteCommnetList.size();

        int resultNum = 0;

        if(!Objects.isNull(deleteCommnetList)){
            for(int commentNo : deleteCommnetList){
                resultNum += postSchedulerMapper.setFeedCommentDelete(commentNo);
            }

            return "삭제 예정 Feed Comment" +
                    deleteCommnetListCnt +
                    "개, 최종" +
                    resultNum +
                    "개 삭제 완료";
        }else{
            return "삭제 피드 코멘트 없음";
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public String deleteReviewsMakedAsDeleted(String today){
        List<Integer> deleteReviewList = postSchedulerMapper.getDeleteReviewList(today);
        int deleteReviewListCnt = deleteReviewList.size();

        int resultNum = 0;

        if(!Objects.isNull(deleteReviewList)){
            for(int reviewNo : deleteReviewList){
                postSchedulerMapper.deleteReviewPhoto(reviewNo);
                resultNum += postSchedulerMapper.setReviewDelete(reviewNo);
            }

            return "삭제 예정 Review" +
                    deleteReviewListCnt +
                    "개, 최종" +
                    resultNum +
                    "개 삭제 완료";
        }else{
            return "삭제 리뷰 없음";
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public String deleteNotiecsMakedAsDeleted(String today){
        List<Integer> deleteNoticeList = postSchedulerMapper.getDeleteNoticeList(today);
        int deleteNoticeListCnt = deleteNoticeList.size();

        int resultNum = 0;

        if(!Objects.isNull(deleteNoticeList)){
            for(int noticeNo : deleteNoticeList){
                resultNum += postSchedulerMapper.setNoticeDelete(noticeNo);
            }

            return "삭제 예정 Notice" +
                    deleteNoticeListCnt +
                    "개, 최종" +
                    resultNum +
                    "개 삭제 완료";
        }else{
            return "삭제 공지사항 없음";
        }
    }

}
