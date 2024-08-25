package com.heartlink.admin.model.mapper;

import com.heartlink.admin.model.dto.PaymentHistoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminPaymentMapper {

    public List<PaymentHistoryDto> getRefundHistory();

    public int updateCanceledPaymentHistory(String paymentNo, String state);

    public int updatePaymentHistory(String paymentNo, String state);
}
