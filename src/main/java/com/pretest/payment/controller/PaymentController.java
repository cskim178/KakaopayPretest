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
import com.pretest.payment.entity.PaymentVO;
import com.pretest.payment.service.PaymentService;
import com.pretest.payment.util.ConstantsVariable;

@RestController
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@PostMapping(value = "/api/payment", produces = "application/json;charset=utf-8")
	@ResponseBody
	public ResponseEntity<?> payment(@RequestParam(required = false, value = "opVat") String opVat,
			PaymentVO paymentVO) {
		
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse = paymentService.payment(paymentVO, opVat);

		return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
	}

	@PostMapping(value = "/api/info", produces = "application/json;charset=utf-8")
	public ResponseEntity<?> getInfo(@RequestParam(value = "id") String id) {
		InfoResponse infoResponse = new InfoResponse();
		infoResponse = paymentService.getPaymentInfoById(id);

		return new ResponseEntity<>(infoResponse, HttpStatus.OK);
	}

	@PostMapping(value = "/api/cancel/all", produces = "application/json;charset=utf-8")
	public ResponseEntity<?> cancelAll(@RequestParam(required = false, value = "opVat") String opVat,
			PaymentVO paymentVO) {
		
		HttpStatus httpStatus;		
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse = paymentService.paymentCancel(paymentVO, opVat);
		
		if(paymentResponse == null) {
			httpStatus = HttpStatus.NO_CONTENT;
		}else {
			httpStatus = HttpStatus.OK;
		}

		return new ResponseEntity<>(paymentResponse, httpStatus);
	}

//	@PostMapping("/api/cancel/part")
//	public Payment cancelPart(Payment payment) {
//		System.out.println("REST API : /api/cancel/part ");
//		return repo.save(payment);
//	}

}
