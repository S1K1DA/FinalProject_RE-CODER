package com.heartlink.matching.area.model.service;

import com.amazonaws.services.s3.AmazonS3;
import com.heartlink.matching.area.model.dto.BoundsRequestDto;
import com.heartlink.matching.area.model.dto.MatchingAreaDto;
import com.heartlink.matching.area.model.mapper.MatchingAreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchingAreaService {
    private final MatchingAreaMapper areaMapper;
    private final AmazonS3 s3Client;

    @Autowired
    public MatchingAreaService(MatchingAreaMapper areaMapper,
                               AmazonS3 s3Client){
        this.areaMapper = areaMapper;
        this.s3Client = s3Client;
    }

    public MatchingAreaDto setUserLocation(int basicUserNo){
        return areaMapper.setUserLocation(basicUserNo);
    }

    public List<MatchingAreaDto> getUserIsBounds(BoundsRequestDto bounds){

        List<MatchingAreaDto> userDetails = areaMapper.selectUserDetails(bounds);

        for (MatchingAreaDto item : userDetails) {
            // consentLocationInfo 값 가져오기
            String consentLocationInfo = item.getConsentLocationInfo();

            // consentLocationInfo 값 확인하고 처리
            if ("N".equals(consentLocationInfo)) {
                item.setLatitude(0);
                item.setLongitude(0);
            }

            String s3Url = item.getPhotoPath() + item.getPhotoName();
            URL url = s3Client.getUrl("heart-link-bucket", s3Url);
            String txtUrl = "" + url;

            item.setPhotoPath(txtUrl);
        }

        return userDetails;
    }

}
