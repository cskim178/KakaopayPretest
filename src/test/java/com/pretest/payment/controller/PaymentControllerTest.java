package com.pretest.payment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.pretest.payment.entity.InfoResponse;
import com.pretest.payment.entity.Payment;
import com.pretest.payment.entity.PaymentResponse;

@SpringBootTest
@Transactional
public class PaymentControllerTest {

	@Autowired
	private PaymentController paymentController;

	@BeforeEach
	void setUp() throws Exception {

	}

	@Test
	void testGetInfo() {
		String id = "1";
		ResponseEntity<InfoResponse> result = paymentController.getInfo(id);		
		System.out.println("testGetInfo : " + result.toString());
	}
	
	@Test
	void testPayment() {
		Payment payment = new Payment();
		payment.setCardNum("1234567890123456");
		payment.setMmyy("1125");
		payment.setCvc("777");
		payment.setInstMm(0);
		payment.setAmount(110000);
		payment.setVat(10000);
		ResponseEntity<PaymentResponse> result = paymentController.payment(payment);		
		System.out.println("testPayment : " + result.toString());
	}
	
	@Test
	void testCancel() {
		Payment payment = new Payment();
		payment.setId("1");
		payment.setAmount(1000);
		ResponseEntity<PaymentResponse> result = paymentController.cancel(payment);		
		System.out.println("testCancel : " + result.toString());
	}

}
