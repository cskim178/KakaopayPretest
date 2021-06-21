package com.pretest.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pretest.payment.entity.InfoResponse;
import com.pretest.payment.entity.PaymentResponse;
import com.pretest.payment.entity.Payment;
import com.pretest.payment.service.PaymentService;
import com.pretest.payment.util.ConstantsVariable;

@RestController
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@PostMapping(value = "/payment", produces = "application/json;charset=utf-8")
	@ResponseBody
	public ResponseEntity<?> payment(Payment paymentInput) {
		
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse = paymentService.payment(paymentInput);

		return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
	}

	@PostMapping(value = "/info", produces = "application/json;charset=utf-8")
	public ResponseEntity<?> getInfo(@RequestParam(value = "id") String id) {
		InfoResponse infoResponse = new InfoResponse();
		infoResponse = paymentService.getPaymentInfoById(id);

		return new ResponseEntity<>(infoResponse, HttpStatus.OK);
	}

	@PostMapping(value = "/cancel", produces = "application/json;charset=utf-8")
	@ResponseBody
	public ResponseEntity<?> cancelAll(Payment paymentInput) {		
		HttpStatus httpStatus;		
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse = paymentService.paymentCancel(paymentInput);
		
		if(paymentResponse == null) {
			httpStatus = HttpStatus.NO_CONTENT;
		}else {
			httpStatus = HttpStatus.OK;
		}

		return new ResponseEntity<>(paymentResponse, httpStatus);
	}
	
}
