package com.pretest.payment.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.pretest.payment.entity.InfoResponse;
import com.pretest.payment.entity.Payment;
import com.pretest.payment.entity.PaymentResponse;
import com.pretest.payment.repository.PaymentRepository;

@SpringBootTest
@Transactional
public class PaymentRepositoryTest {

	@Autowired
	private PaymentRepository paymentRepository;

	@BeforeEach
	void setUp() throws Exception {

	}

	@Test
	void testGetAmtVatSumByRefId() {
		String refId = "1";
		List<Object[]> cancelAmtVatSumObj = paymentRepository.getAmtVatSumByRefId(refId);	
		Object[] cancelAmtVatSum = null;
		int amtSum = 0;
		int vatSum = 0;

		// 취소 이력이 있다면
		if (!ObjectUtils.isEmpty(cancelAmtVatSumObj)) {
			cancelAmtVatSum = cancelAmtVatSumObj.get(0);
			if (!ObjectUtils.isEmpty(cancelAmtVatSum[0])) {
				amtSum = Integer.valueOf(cancelAmtVatSum[0].toString());
			}
			if (!ObjectUtils.isEmpty(cancelAmtVatSum[1])) {
				vatSum = Integer.valueOf(cancelAmtVatSum[1].toString());
			}
		}
		
		System.out.println("testGetAmtVatSumByRefId : " + " amtSum = "+amtSum + ", vatSum = " + vatSum);
	}

}
