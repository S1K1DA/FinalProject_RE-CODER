package com.heartlink.charge.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.heartlink.charge.model.dto.ChargeRequestDto;

@Mapper
public interface ChargeMapper {

    public int getCurrentSequence();

    public int setPaymentHistory(ChargeRequestDto chargeRequestDto);

    public ChargeRequestDto setPaymentDbDetails(String paymentNo);

    public int setPaymentState(ChargeRequestDto apiResponse);
}
