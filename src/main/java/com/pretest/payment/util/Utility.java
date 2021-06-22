package com.pretest.payment.util;

import org.springframework.util.ObjectUtils;

import com.pretest.payment.entity.Payment;

public final class Utility {

	public static boolean requestCheck(Payment paymentInput) {

		boolean result = true;

		if (paymentInput.getPayType().equals(cVariable.TYPE_PAYMENT)) {// ������û
			
			// ī���ȣ 10~16�ڸ� ����
			int cardNumLen = paymentInput.getCardNum().length();
			if (cardNumLen < 10 || cardNumLen > 20) {
				result = false;
			}
			try {// ���ڷθ�
				Integer.valueOf(cardNumLen);
			} catch (Exception e) {
				result = false;
			}

			// ��ȿ�Ⱓ - 4�ڸ� ����
			int mmyyLen = paymentInput.getMmyy().length();
			if (mmyyLen != 4) {
				result = false;
			}
			try {// ���ڷθ�
				Integer.valueOf(mmyyLen);
			} catch (Exception e) {
				result = false;
			}

			// cvc - 3�ڸ� ����
			int cvcLen = paymentInput.getCvc().length();
			if (cvcLen != 3) {
				result = false;
			}
			try {// ���ڷθ�
				Integer.valueOf(cvcLen);
			} catch (Exception e) {
				result = false;
			}

			// �Һΰ��� - 0 ~ 12
			if (paymentInput.getInstMm() < 0 || paymentInput.getInstMm() > 12) {
				result = false;
			}

			// �����ݾ� - 100 ~ 1,000,000,000
			if (paymentInput.getAmount() < 100 || paymentInput.getAmount() > 1000000000) {
				result = false;
			}

			// vat - �ɼ�. ���� �ִٸ� �����ݾ� ����
			if (!ObjectUtils.isEmpty(paymentInput.getVat()) && paymentInput.getVat() > paymentInput.getAmount()) {
				result = false;
			}

		} else if (paymentInput.getPayType().equals(cVariable.TYPE_CANCLE)) {// �������
			// id
			if (ObjectUtils.isEmpty(paymentInput.getId())) {
				result = false;
			}
			
			// �����ݾ� - 100 ~ 1,000,000,000
			if (paymentInput.getAmount() < 100 || paymentInput.getAmount() > 1000000000) {
				result = false;
			}

			// vat - �ɼ�. ���� �ִٸ� �����ݾ� ����
			if (!ObjectUtils.isEmpty(paymentInput.getVat()) && paymentInput.getVat() > paymentInput.getAmount()) {
				result = false;
			}

		} else {// ���������� ������ �ȵ�...
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

		// �����ͱ���(���� 4) _3
		String dataLen = "";

		// �����ͱ���(���� 10) A_
		String dataType = "";
		if (paymentVO.getPayType().equals(cVariable.TYPE_PAYMENT)) {// ����
			dataType = Utility.rPad("PAYMENT", 10, cVariable.SYMBOL_SPACE);
		} else if (paymentVO.getPayType().equals(cVariable.TYPE_CANCLE)) {// ���
			dataType = Utility.rPad("CANCEL", 10, cVariable.SYMBOL_SPACE);
		}
//		else if (paymentVO.getPayType().equals(cVariable.TYPE_PART_CANCLE)) {// �κ����
//			dataType = Utility.rPad("CANCEL", 10, cVariable.SYMBOL_SPACE);
//		}

		// ������ȣ(���� 20) A_
		String id = Utility.rPad(String.valueOf(paymentVO.getId()), 20, cVariable.SYMBOL_SPACE);

		// ī���ȣ(����L 20) 3_
		String cardNum = Utility.rPad(paymentVO.getCardNum(), 20, cVariable.SYMBOL_SPACE);

		// �Һΰ�����(����0 2) 03
		String instMm = Utility.lPad(String.valueOf(paymentVO.getInstMm()), 2, cVariable.SYMBOL_ZERO);

		// ī����ȿ�Ⱓ(����L 4) 3_
		String validTerm = Utility.lPad(String.valueOf(paymentVO.getMmyy()), 4, cVariable.SYMBOL_SPACE);

		// CVC(����L 3) 3_
		String cvc = Utility.lPad(String.valueOf(paymentVO.getCvc()), 3, cVariable.SYMBOL_SPACE);

		// �ŷ��ݾ�(���� 10) _3
		String amount = Utility.lPad(String.valueOf(paymentVO.getAmount()), 10, cVariable.SYMBOL_SPACE);

		// �ΰ���ġ��(����0 10) _3
		String vat = Utility.lPad(String.valueOf(paymentVO.getVat()), 10, cVariable.SYMBOL_ZERO);

		// ���ŷ�������ȣ(���� 20) A_
		String refId = Utility.rPad(String.valueOf(paymentVO.getRefId()), 20, cVariable.SYMBOL_SPACE);

		// ��ȣȭ��ī������(���� 300) A_
		String encrCardInfo = Utility.rPad(paymentVO.getEncrCardInfo(), 300, cVariable.SYMBOL_SPACE);

		// �����ʵ�(���� 47) A_
		String spare = Utility.rPad("", 47, cVariable.SYMBOL_SPACE);

		strResult = dataType + id + cardNum + instMm + validTerm + cvc + amount + vat + refId + encrCardInfo + spare;

		dataLen = Utility.lPad(String.valueOf(strResult.length()), 4, cVariable.SYMBOL_SPACE);

		strResult = dataLen + strResult;

		return strResult;
	}

}