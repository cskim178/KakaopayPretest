package com.pretest.payment.util;

import org.springframework.util.ObjectUtils;

import com.pretest.payment.entity.PaymentVO;

public final class Utility {

	public static boolean validationCheck(PaymentVO vo) {

//		int idLen = 0;		
//		if(!ObjectUtils.isEmpty(vo.getId())) {
//			idLen = vo.getId().length();// 20
//		}
//		
//		int cardNumLen = vo.getCardNum().length();// 20
//		int validTermLen = (int) (Math.log10(vo.getValidTerm()) + 1);// 4
//		int cvcLen = (int) (Math.log10(vo.getCvc()) + 1);// 3
//		int instMmLen = (int) (Math.log10(vo.getInstMm()) + 1);// 2
//		int amountLen = (int) (Math.log10(vo.getAmount()) + 1);// 10
//		int vatLen = (int) (Math.log10(vo.getVat()) + 1);// 10

		return false;
	}
	
	public static int calcVat(String strVat, int amount) {

		int vat = 0;

		try {
			if (strVat != null) {
				vat = Integer.valueOf(strVat);
			}else {
				vat = (int) Math.round(((double) amount / 11));				
			}
		} catch (Exception e) {
			vat = 0;
		}

		return vat;
	}

	public static String lPad(String strContext, int iLen, String strChar) {
		String strResult = "";
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

		StringBuilder preChar = new StringBuilder();
		StringBuilder postChar = new StringBuilder();

		for (int i = 0; i < preLen; i++) {
			preChar.append(ConstantsVariable.SYMBOL_MASKING);
		}

		for (int i = 0; i < postLen; i++) {
			postChar.append(ConstantsVariable.SYMBOL_MASKING);
		}

		if (preLen + postLen > strContext.length()) {
			strResult = preChar + "" + postChar;
		} else {
			strResult = preChar + strContext.substring(preLen, strContext.length() - postLen) + postChar;
		}

		return strResult;

	}

	public static String makePgString(PaymentVO paymentVO) {

		String strResult = "";

		// 데이터길이(숫자 4) _3
		String dataLen = "";

		// 데이터구분(문자 10) A_
		String dataType = "";
		if (paymentVO.getPayType().equals(ConstantsVariable.TYPE_PAYMENT)) {// 결재
			dataType = Utility.rPad("PAYMENT", 10, ConstantsVariable.SYMBOL_SPACE);
		} else if (paymentVO.getPayType().equals(ConstantsVariable.TYPE_CANCLE)) {// 취소
			dataType = Utility.rPad("CANCEL", 10, ConstantsVariable.SYMBOL_SPACE);
		} else if (paymentVO.getPayType().equals(ConstantsVariable.TYPE_PART_CANCLE)) {// 부분취소
			dataType = Utility.rPad("CANCEL", 10, ConstantsVariable.SYMBOL_SPACE);
		}

		// 관리번호(문자 20) A_
		String id = Utility.rPad(paymentVO.getId(), 20, ConstantsVariable.SYMBOL_SPACE);

		// 카드번호(숫자L 20) 3_
		String cardNum = Utility.rPad(paymentVO.getCardNum(), 20, ConstantsVariable.SYMBOL_SPACE);

		// 할부개월수(숫자0 2) 03
		String instMm = Utility.lPad(String.valueOf(paymentVO.getInstMm()), 2, ConstantsVariable.SYMBOL_ZERO);

		// 카드유효기간(숫자L 4) 3_
		String validTerm = Utility.lPad(String.valueOf(paymentVO.getMmyy()), 4, ConstantsVariable.SYMBOL_SPACE);

		// CVC(숫자L 3) 3_
		String cvc = Utility.lPad(String.valueOf(paymentVO.getCvc()), 3, ConstantsVariable.SYMBOL_SPACE);

		// 거래금액(숫자 10) _3
		String amount = Utility.lPad(String.valueOf(paymentVO.getAmount()), 10, ConstantsVariable.SYMBOL_SPACE);

		// 부가가치세(숫자0 10) _3
		String vat = Utility.lPad(String.valueOf(paymentVO.getVat()), 10, ConstantsVariable.SYMBOL_ZERO);

		// 원거래관리번호(문자 20) A_
		String refId = Utility.rPad(paymentVO.getRefId(), 20, ConstantsVariable.SYMBOL_SPACE);

		// 암호화된카드정보(문자 300) A_
		String encrCardInfo = Utility.rPad(paymentVO.getEncrCardInfo(), 300, ConstantsVariable.SYMBOL_SPACE);

		// 예비필드(문자 47) A_
		String spare = Utility.rPad("", 47, ConstantsVariable.SYMBOL_SPACE);

		strResult = dataType + id + cardNum + instMm + validTerm + cvc + amount + vat + refId + encrCardInfo + spare;

		dataLen = Utility.lPad(String.valueOf(strResult.length()), 4, ConstantsVariable.SYMBOL_SPACE);

		strResult = dataLen + strResult;

		return strResult;
	}

}