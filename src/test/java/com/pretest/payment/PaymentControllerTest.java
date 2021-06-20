package com.pretest.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.pretest.payment.controller.PaymentController;
import com.pretest.payment.entity.PaymentVO;

@SpringBootTest
@Transactional
public class PaymentControllerTest {
	
	@Autowired
	private PaymentController PaymentController;
	
	@BeforeEach
	void setUp() throws Exception {
		
	}

	@Test
	void testCreateCoupon() {
//		PaymentVO paymentVO = new PaymentVO();
//		paymentVO.setAmount("100");
//		PaymentController.payment(paymentVO);
		
	}

}
