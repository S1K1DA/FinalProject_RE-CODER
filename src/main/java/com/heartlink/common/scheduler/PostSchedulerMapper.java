package com.heartlink.common.scheduler;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostSchedulerMapper {

    public List<Integer> getDeleteFeedList(String today);
    public int setFeedDelete(int feedNo);
    public int deleteFeedLikes(int feedNo);
    public int deleteFeedComments(int feedNo);

    public List<Integer> getDeleteCommentList(String today);
    public int setFeedCommentDelete(int commentNo);

    public List<Integer> getDeleteReviewList(String today);
    public int deleteReviewPhoto(int reviewNo);
    public int setReviewDelete(int reviewNo);

    public List<Integer> getDeleteNoticeList(String today);
    public int setNoticeDelete(int noticeNo);
}
