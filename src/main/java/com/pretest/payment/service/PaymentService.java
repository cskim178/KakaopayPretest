package com.pretest.payment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.pretest.payment.entity.InfoResponse;
import com.pretest.payment.entity.Payment;
import com.pretest.payment.entity.PaymentResponse;
import com.pretest.payment.entity.Transaction;
import com.pretest.payment.repository.PaymentRepository;
import com.pretest.payment.repository.TransactionRepository;
import com.pretest.payment.util.Utility;
import com.pretest.payment.util.cVariable;

@Service
public class PaymentService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Transactional
	public PaymentResponse payment(Payment paymentInput) {

		paymentInput.setPayType(cVariable.TYPE_PAYMENT);

		if (!Utility.requestCheck(paymentInput)) {
			return null;
		}

		if (ObjectUtils.isEmpty(paymentInput.getVat())) {
			paymentInput.setVat((int) Math.round(((double) paymentInput.getAmount() / 11)));
		}

		Utility.encrCardInfo(paymentInput);
		paymentInput.setRefId("");
		paymentRepository.save(paymentInput);

		Transaction tran = new Transaction();
		tran.setContent(Utility.makePgString(paymentInput));
		transactionRepository.save(tran);

		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setId(paymentInput.getId());
		paymentResponse.setContent(tran.getContent());

		return paymentResponse;
	}

	@Transactional
	public PaymentResponse paymentCancel(Payment paymentInput) {
		// 취소 요청 paymentInput
		// DB에서 가져온 결재정보 paymentDB
		// DB에 입력할 결재취소 paymentDto
		Payment paymentDB = new Payment();
		Payment paymentDto = new Payment();
		
		paymentInput.setPayType(cVariable.TYPE_CANCLE);

		// 입력된 값으로 결제 조회
		Optional<Payment> paymentDbObj = paymentRepository.findById(String.valueOf(paymentInput.getId()));

		if (paymentDbObj.isPresent()) {
			paymentDB = paymentDbObj.get();

			// 결제 이력 없음
			if (ObjectUtils.isEmpty(paymentDB) || (!paymentDB.getPayType().equals(cVariable.TYPE_PAYMENT))) {
				System.out.println("// 0. 결제 이력 없음");
				return null;
			}
			Utility.decrCardInfo(paymentDB);//결제 이력이 있다면 카드정보 복호화
		} else {
			return null;// 결제 이력 없음!!!
		}

		// 취소 이력 -> 금액, vat 총합을 가져온다.
		List<Object[]> cancelAmtVatSumObj = paymentRepository.getAmtVatSumByRefId(paymentInput.getId());
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
		
		// 취소불가 : 취소가능 금액 = 결재금액 - sum(취소금액) < 요청금액
		if (paymentDB.getAmount() - amtSum < paymentInput.getAmount()) {
			return null;
		}
		// 전체취소(잔액 모두 취소)면
		else if (paymentDB.getAmount() - amtSum == paymentInput.getAmount()) {

			// 입력받은 vat가 없으면 잔액(vat)으로 처리하여 전체 취소가 정상 수행되게 한다.
			if (ObjectUtils.isEmpty(paymentInput.getVat())) {
				paymentInput.setVat(paymentDB.getVat() - vatSum);
			} else if (!(paymentDB.getVat() - vatSum == paymentInput.getVat())) {
				return null;
			}
		}
		// 부분취소 : 취소가능 금액 = 결재금액 - sum(취소금액) > 취소 요청금액
		else {
			// 입력받은 vat가 없으면 취소요청금액/11
			if (ObjectUtils.isEmpty(paymentInput.getVat())) {
				paymentInput.setVat((int) Math.round(((double) paymentInput.getAmount() / 11)));
			}
			// 취소가능 vat = 결재vat - sum(취소vat) < 요청vat paymentDB.getVat() - vatSum <
			if (paymentDB.getVat() - vatSum < paymentInput.getVat()) {
				return null;
			}
		}

		paymentDto.setCardNum(paymentDB.getCardNum());
		paymentDto.setMmyy(paymentDB.getMmyy());
		paymentDto.setCvc(paymentDB.getCvc());
		paymentDto.setEncrCardInfo(paymentDB.getEncrCardInfo());
		paymentDto.setInstMm(0);
		paymentDto.setAmount(paymentInput.getAmount());
		paymentDto.setVat(paymentInput.getVat());
		paymentDto.setPayType(cVariable.TYPE_CANCLE);
		paymentDto.setRefId(paymentInput.getId());
		paymentRepository.save(paymentDto);

		Transaction tran = new Transaction();
		tran.setContent(Utility.makePgString(paymentDto));
		transactionRepository.save(tran);

		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setId(paymentDto.getId());
		paymentResponse.setContent(tran.getContent());

		return paymentResponse;
	}

	@Transactional(readOnly = true)
	public InfoResponse getPaymentInfoById(String id) {
		InfoResponse infoResponse = new InfoResponse();
		Payment paymentVO = new Payment();

		Optional<Payment> paymentInfo = paymentRepository.findById(id);

		if (paymentInfo.isPresent()) {
			paymentVO = paymentInfo.get();
		} else {
			return infoResponse;
		}

		infoResponse.setId(paymentVO.getId());
		Utility.decrCardInfo(paymentVO);
		infoResponse.setCardNum(Utility.masking(paymentVO.getCardNum(), 6, 3));
		infoResponse.setMmyy(paymentVO.getMmyy());
		infoResponse.setCvc(paymentVO.getCvc());
		infoResponse.setAmount(paymentVO.getAmount());
		infoResponse.setVat(paymentVO.getVat());

		if (paymentVO.getPayType().equals(cVariable.TYPE_PAYMENT)) {
			infoResponse.setPayType("결제");
		} else {
			infoResponse.setPayType("취소");
		}

		return infoResponse;
	}

}
