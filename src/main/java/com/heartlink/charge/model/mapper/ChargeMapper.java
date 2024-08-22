package com.heartlink.charge.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.heartlink.charge.model.dto.ChargeRequestDto;

import java.util.List;

@Mapper
public interface ChargeMapper {

    public int getCurrentSequence();

    public int setPaymentHistory(ChargeRequestDto chargeRequestDto);

    public int setUserCoin(ChargeRequestDto dbResponse);

    public ChargeRequestDto setPaymentDbDetails(String paymentNo);

    public int setPaymentState(ChargeRequestDto apiResponse);

    public List<ChargeRequestDto> getUserPaymentHistory(String userEmail);

    public int selectUserCoin(String userEmail);
}
