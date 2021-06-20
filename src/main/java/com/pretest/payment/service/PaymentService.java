package com.pretest.payment.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pretest.payment.entity.InfoResponse;
import com.pretest.payment.entity.PaymentResponse;
import com.pretest.payment.entity.PaymentVO;
import com.pretest.payment.entity.Transaction;
import com.pretest.payment.repository.PaymentRepository;
import com.pretest.payment.repository.TransactionRepository;
import com.pretest.payment.util.AES256Util;
import com.pretest.payment.util.ConstantsVariable;
import com.pretest.payment.util.Utility;

@Service
public class PaymentService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Transactional
	public PaymentResponse payment(PaymentVO paymentVO, String opVat) {		
		AES256Util encr = new AES256Util();
//		if (Utility.validationCheck(paymentRequest)) {
//			return null;
//		}
		
		paymentVO.setPayType(ConstantsVariable.TYPE_PAYMENT);
		
		int vat = Utility.calcVat(opVat, paymentVO.getAmount());
		paymentVO.setVat(vat);
		
		String encrCardInfo = String.valueOf(paymentVO.getCardNum()) + ConstantsVariable.SYMBOL_SEPARATION
				+ String.valueOf(paymentVO.getMmyy()) + ConstantsVariable.SYMBOL_SEPARATION
				+ String.valueOf(paymentVO.getCvc());		
		paymentVO.setEncrCardInfo(encr.encrypt(encrCardInfo));

		paymentRepository.save(paymentVO);

		Transaction tran = new Transaction();
		tran.setContent(Utility.makePgString(paymentVO));
		transactionRepository.save(tran);

		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setId(paymentVO.getId());
		paymentResponse.setContent(tran.getContent());

		return paymentResponse;
	}

	@Transactional(readOnly = true)
	public InfoResponse getPaymentInfoById(String id) {

		InfoResponse infoResponse = new InfoResponse();
		PaymentVO paymentVO = new PaymentVO();
		AES256Util decr = new AES256Util();

		Optional<PaymentVO> paymentInfo = paymentRepository.findById(id);

		if (paymentInfo.isPresent()) {
			paymentVO = paymentInfo.get();
		} else {
			return infoResponse;
		}

		infoResponse.setId(paymentVO.getId());

		String decrCardInfo = paymentVO.getEncrCardInfo();
		decrCardInfo = decr.decrypt(decrCardInfo);
		String str[] = decrCardInfo.split(ConstantsVariable.SYMBOL_SEPARATION);

		infoResponse.setCardNum(Utility.masking(str[0], 6, 3));

		// TODO String validTerm = Utility.lPad(String.valueOf(paymentVO.getMmyy()), 4,
		// ConstantsVariable.SYMBOL_SPACE);
		infoResponse.setMmyy(Integer.valueOf(str[1]));

		infoResponse.setCvc(Integer.valueOf(str[2]));
		infoResponse.setAmount(paymentVO.getAmount());
		infoResponse.setVat(paymentVO.getVat());

		if (paymentVO.getPayType().equals(ConstantsVariable.TYPE_PAYMENT)) {
			infoResponse.setPayType("결제");
		} else {
			infoResponse.setPayType("취소");
		}

		return infoResponse;
	}

	@Transactional
	public PaymentResponse paymentCancel(PaymentVO paymentVO, String opVat) {
		PaymentVO paymentinput = new PaymentVO();
		
		Optional<PaymentVO> paymentInfo = paymentRepository.findById(paymentVO.getId());

		if (paymentInfo.isPresent()) {
			paymentVO = paymentInfo.get();
		} else {
			return null; 
		}
		
		paymentinput.setAmount(paymentVO.getAmount());
		paymentinput.setEncrCardInfo(paymentVO.getEncrCardInfo());
		paymentinput.setInstMm(0);
		paymentinput.setPayType(ConstantsVariable.TYPE_CANCLE);
		paymentinput.setRefId(paymentVO.getId());
		
		int vat = Utility.calcVat(opVat, paymentVO.getAmount());
		paymentinput.setVat(vat);
		
		paymentRepository.save(paymentinput);
		
		Transaction tran = new Transaction();
		tran.setContent(Utility.makePgString(paymentinput));
		
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setId(paymentVO.getId());
		paymentResponse.setContent(tran.getContent());
		
		return paymentResponse;
	}

}
