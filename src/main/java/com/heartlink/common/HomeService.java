package com.heartlink.common;

import com.amazonaws.services.s3.AmazonS3;
import com.heartlink.feed.model.dto.FeedDto;
import com.heartlink.feed.model.mapper.FeedMapper;
import com.heartlink.member.model.dto.MemberDto;
import com.heartlink.member.model.mapper.MemberMapper;
import com.heartlink.review.model.dao.ReviewDao;
import com.heartlink.review.model.dto.ReviewDto;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HomeService {

    private final ReviewDao reviewDao;
    private final FeedMapper feedMapper;
    private final MemberMapper memberMapper;
    private final AmazonS3 s3Client;

    public HomeService(ReviewDao reviewDao,
                       FeedMapper feedMapper,
                       MemberMapper memberMapper,
                       AmazonS3 s3Client){
        this.reviewDao = reviewDao;
        this.feedMapper = feedMapper;
        this.memberMapper = memberMapper;
        this.s3Client = s3Client;
    }

    public List<ReviewDto> getPhotoView(){

        List<ReviewDto> reviews = reviewDao.getTopReviews();

        for (ReviewDto review : reviews) {
            String titleCut = "";
            if(review.getReviewTitle().length() > 7){
                titleCut = review.getReviewTitle().substring(0, 7) + "...";
            }
            review.setReviewTitle(titleCut);

            String firstImageUrl = extractFirstImageUrl(review.getReviewContent());
            review.setFirstImageUrl(firstImageUrl != null ? firstImageUrl : "/image/mainThumbnail.jpg");
        }

        return reviews;
    }

    //리뷰의 글에서 이미지 추출
    private String extractFirstImageUrl(String content) {
        String imgTagPattern = "<img[^>]+src=[\"']([^\"']+)[\"'][^>]*>";
        Pattern pattern = Pattern.compile(imgTagPattern);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1); // 첫 번째 이미지의 URL 추출
        }
        return null;
    }


    public List<FeedDto> getTopFeedList(){

        List<FeedDto> result = feedMapper.getTopFeedList();

        if(result.isEmpty()){
            result = feedMapper.getNewFeedList();
        }

        for(FeedDto item : result){
            
            // top 3 상세정보 가져오기
            FeedDto detail = feedMapper.getTopFeedDetail(item.getFeedNo());

            String titleCut = "";
            if(detail.getFeedTitle().length() > 9){
                titleCut = detail.getFeedTitle().substring(0, 9) + "...";
            }
            item.setFeedTitle(titleCut);

            // 태그 다시 달고 제거
            String originalContent = StringEscapeUtils.unescapeHtml4(detail.getFeedContent());
            String contentWithoutTags = Jsoup.clean(originalContent, Safelist.none());

            if (contentWithoutTags.length() > 45) {
                contentWithoutTags = contentWithoutTags.substring(0, 45) + "...";
            }

            item.setFeedContent(contentWithoutTags);

            item.setFeedTag(detail.getFeedTag());
            item.setAuthorUserNo(detail.getAuthorUserNo());
            item.setBasicUserNickname(detail.getBasicUserNickname());
        }

        while (result.size() < 3) {
            result.add(new FeedDto());
        }

        return result;
    }

    public List<MemberDto> getTopUserList(){

        List<MemberDto> result = memberMapper.getTopUserList();

        for(MemberDto user : result){
            MemberDto detail = memberMapper.getUserDetail(user.getUserNumber());

            user.setNickname(detail.getNickname());
            user.setGender(detail.getGender());
            user.setHobbyName(detail.getHobbyName());
            user.setPersonalLike(detail.getPersonalLike());
            user.setPersonalHate(detail.getPersonalHate());

            String s3Url = detail.getUserPhotoPath() + detail.getUserPhotoName();
            URL url = s3Client.getUrl("heart-link-bucket", s3Url);
            String txtUrl = "" + url;

            user.setUserPhotoPath(txtUrl);
        }

        while (result.size() < 3) {
            result.add(new MemberDto()); // 빈 FeedDto 객체를 추가합니다.
        }

        return result;
    }

}
