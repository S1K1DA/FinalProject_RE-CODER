package com.heartlink.admin.model.mapper;

import com.heartlink.admin.model.dto.PaymentHistoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminPaymentMapper {

    public List<PaymentHistoryDto> getRefundHistory(String startDate, String endDate);

    public List<PaymentHistoryDto> getAllPaymentHistory(String startDate, String endDate);

    public int updateCanceledPaymentHistory(String paymentNo, String state);

    public int updatePaymentHistory(String paymentNo, String state);

}
