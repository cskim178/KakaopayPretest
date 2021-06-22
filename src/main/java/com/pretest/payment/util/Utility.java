package com.pretest.payment.util;

import org.springframework.util.ObjectUtils;

import com.pretest.payment.entity.Payment;

public final class Utility {

	public static boolean requestCheck(Payment paymentInput) {

		boolean result = true;

		if (paymentInput.getPayType().equals(cVariable.TYPE_PAYMENT)) {// 결제요청
			
			// 카드번호 10~16자리 숫자
			int cardNumLen = paymentInput.getCardNum().length();
			if (cardNumLen < 10 || cardNumLen > 20) {
				result = false;
			}
			try {// 숫자로만
				Integer.valueOf(cardNumLen);
			} catch (Exception e) {
				result = false;
			}

			// 유효기간 - 4자리 숫자
			int mmyyLen = paymentInput.getMmyy().length();
			if (mmyyLen != 4) {
				result = false;
			}
			try {// 숫자로만
				Integer.valueOf(mmyyLen);
			} catch (Exception e) {
				result = false;
			}

			// cvc - 3자리 숫자
			int cvcLen = paymentInput.getCvc().length();
			if (cvcLen != 3) {
				result = false;
			}
			try {// 숫자로만
				Integer.valueOf(cvcLen);
			} catch (Exception e) {
				result = false;
			}

			// 할부개월 - 0 ~ 12
			if (paymentInput.getInstMm() < 0 || paymentInput.getInstMm() > 12) {
				result = false;
			}

			// 결제금액 - 100 ~ 1,000,000,000
			if (paymentInput.getAmount() < 100 || paymentInput.getAmount() > 1000000000) {
				result = false;
			}

			// vat - 옵션. 값이 있다면 결제금액 이하
			if (!ObjectUtils.isEmpty(paymentInput.getVat()) && paymentInput.getVat() > paymentInput.getAmount()) {
				result = false;
			}

		} else if (paymentInput.getPayType().equals(cVariable.TYPE_CANCLE)) {// 결제취소
			// id
			if (ObjectUtils.isEmpty(paymentInput.getId())) {
				result = false;
			}
			
			// 결제금액 - 100 ~ 1,000,000,000
			if (paymentInput.getAmount() < 100 || paymentInput.getAmount() > 1000000000) {
				result = false;
			}

			// vat - 옵션. 값이 있다면 결제금액 이하
			if (!ObjectUtils.isEmpty(paymentInput.getVat()) && paymentInput.getVat() > paymentInput.getAmount()) {
				result = false;
			}

		} else {// 결제유형이 없으면 안됨...
			result = false;
		}

		return result;
	}

	public static String lPad(String strContext, int iLen, String strChar) {
		String strResult = "";
		
		if(ObjectUtils.isEmpty(strContext)) {
			strContext = "";
		}
		
		StringBuilder sbAddChar = new StringBuilder();
		for (int i = strContext.length(); i < iLen; i++) {
			sbAddChar.append(strChar);
		}
		strResult = sbAddChar + strContext;

		return strResult;
	}

	public static String rPad(String strContext, int iLen, String strChar) {
		String strResult = "";

		int len;
		
		if(ObjectUtils.isEmpty(strContext)) {
			strContext = "";
		}

		if (ObjectUtils.isEmpty(strContext)) {
			len = 0;
			strContext = "";
		} else {
			len = strContext.length();
		}

		StringBuilder sbAddChar = new StringBuilder();
		for (int i = len; i < iLen; i++) {
			sbAddChar.append(strChar);
		}
		strResult = strContext + sbAddChar;

		return strResult;
	}

	public static String masking(String strContext, int preLen, int postLen) {
		String strResult = "";
		
		if(ObjectUtils.isEmpty(strContext)) {
			strContext = "";
		}

		StringBuilder preChar = new StringBuilder();
		StringBuilder postChar = new StringBuilder();

		for (int i = 0; i < preLen; i++) {
			preChar.append(cVariable.SYMBOL_MASKING);
		}

		for (int i = 0; i < postLen; i++) {
			postChar.append(cVariable.SYMBOL_MASKING);
		}

		if (preLen + postLen > strContext.length()) {
			strResult = preChar + "" + postChar;
		} else {
			strResult = preChar + strContext.substring(preLen, strContext.length() - postLen) + postChar;
		}

		return strResult;

	}

	public static void encrCardInfo(Payment payment) {
		AES256Util encr = new AES256Util();
		if (ObjectUtils.isEmpty(payment.getCardNum())) {
			payment.setCardNum("");
		}

		if (ObjectUtils.isEmpty(payment.getMmyy())) {
			payment.setMmyy("");
		}

		if (ObjectUtils.isEmpty(payment.getCvc())) {
			payment.setCvc("");
		}

		payment.setEncrCardInfo(encr.encrypt(payment.getCardNum() + cVariable.SYMBOL_SEPARATION + payment.getMmyy()
				+ cVariable.SYMBOL_SEPARATION + payment.getCvc()));
	}

	public static void decrCardInfo(Payment payment) {
		AES256Util decr = new AES256Util();
		if (ObjectUtils.isEmpty(payment)) {
			return;
		}

		String decrCardInfo = decr.decrypt(payment.getEncrCardInfo());
		String str[] = decrCardInfo.split(cVariable.SYMBOL_SEPARATION);
		try {
			payment.setCardNum(str[0]);
			payment.setMmyy(str[1]);
			payment.setCvc(str[2]);
		} catch (Exception e) {
			payment.setCardNum("");
			payment.setMmyy("");
			payment.setCvc("");
		}

	}

	public static String makePgString(Payment paymentVO) {

		String strResult = "";

		// 데이터길이(숫자 4) _3
		String dataLen = "";

		// 데이터구분(문자 10) A_
		String dataType = "";
		if (paymentVO.getPayType().equals(cVariable.TYPE_PAYMENT)) {// 결재
			dataType = Utility.rPad("PAYMENT", 10, cVariable.SYMBOL_SPACE);
		} else if (paymentVO.getPayType().equals(cVariable.TYPE_CANCLE)) {// 취소
			dataType = Utility.rPad("CANCEL", 10, cVariable.SYMBOL_SPACE);
		}
//		else if (paymentVO.getPayType().equals(cVariable.TYPE_PART_CANCLE)) {// 부분취소
//			dataType = Utility.rPad("CANCEL", 10, cVariable.SYMBOL_SPACE);
//		}

		// 관리번호(문자 20) A_
		String id = Utility.rPad(String.valueOf(paymentVO.getId()), 20, cVariable.SYMBOL_SPACE);

		// 카드번호(숫자L 20) 3_
		String cardNum = Utility.rPad(paymentVO.getCardNum(), 20, cVariable.SYMBOL_SPACE);

		// 할부개월수(숫자0 2) 03
		String instMm = Utility.lPad(String.valueOf(paymentVO.getInstMm()), 2, cVariable.SYMBOL_ZERO);

		// 카드유효기간(숫자L 4) 3_
		String validTerm = Utility.lPad(String.valueOf(paymentVO.getMmyy()), 4, cVariable.SYMBOL_SPACE);

		// CVC(숫자L 3) 3_
		String cvc = Utility.lPad(String.valueOf(paymentVO.getCvc()), 3, cVariable.SYMBOL_SPACE);

		// 거래금액(숫자 10) _3
		String amount = Utility.lPad(String.valueOf(paymentVO.getAmount()), 10, cVariable.SYMBOL_SPACE);

		// 부가가치세(숫자0 10) _3
		String vat = Utility.lPad(String.valueOf(paymentVO.getVat()), 10, cVariable.SYMBOL_ZERO);

		// 원거래관리번호(문자 20) A_
		String refId = Utility.rPad(String.valueOf(paymentVO.getRefId()), 20, cVariable.SYMBOL_SPACE);

		// 암호화된카드정보(문자 300) A_
		String encrCardInfo = Utility.rPad(paymentVO.getEncrCardInfo(), 300, cVariable.SYMBOL_SPACE);

		// 예비필드(문자 47) A_
		String spare = Utility.rPad("", 47, cVariable.SYMBOL_SPACE);

		strResult = dataType + id + cardNum + instMm + validTerm + cvc + amount + vat + refId + encrCardInfo + spare;

		dataLen = Utility.lPad(String.valueOf(strResult.length()), 4, cVariable.SYMBOL_SPACE);

		strResult = dataLen + strResult;

		return strResult;
	}

}