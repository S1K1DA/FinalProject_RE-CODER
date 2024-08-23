package com.heartlink.charge.model.mapper;

import com.heartlink.charge.model.dto.ChargeResponseDto;
import com.heartlink.member.model.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import com.heartlink.charge.model.dto.ChargeRequestDto;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
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

    public ChargeRequestDto getRequestPaymentInfo(String paymentNo);

    public int canceledState(String paymentNo);

    public int setCoindeduction(String userEmail, int userProduct);

    public List<ChargeRequestDto> getOldPendingPayments(@Param("limitMinute") LocalDateTime limitMinute);

    public int failPayment(String paymentNo);

    public MemberDto getUserIfo(String userEmail);
}
