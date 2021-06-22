package com.pretest.payment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.pretest.payment.entity.Payment;
import com.pretest.payment.util.Utility;
import com.pretest.payment.util.cVariable;

@SpringBootTest
//@Transactional
public class UtilityTest {

	@BeforeEach
	void setUp() throws Exception {

	}

	@Test
	void testRequestCheck() {
		Payment payment = new Payment();
		payment.setCardNum("1234567890123456");
		payment.setMmyy("1125");
		payment.setCvc("777");
		payment.setInstMm(0);
		payment.setAmount(110000);
		payment.setVat(10000);
		payment.setPayType(cVariable.TYPE_PAYMENT);
		System.out.println("testRequestCheck[TYPE_PAYMENT] : " + Utility.requestCheck(payment));
	}

	@Test
	void testLPad() {
		System.out.println("testLPad[*10]: " + Utility.lPad("test", 10, "*"));
	}

	@Test
	void testRPad() {
		System.out.println("testRPad[10*]: " + Utility.rPad("test", 10, "*"));
	}

	@Test
	void testMasking() {
		System.out.println("testMasking[3_4]: " + Utility.masking("1234567890", 3, 4));
	}

	@Test
	void testEncrDecrCardInfo() {
		Payment payment = new Payment();
		payment.setCardNum("1234567890123456");
		payment.setMmyy("1125");
		payment.setCvc("777");
		System.out.println("CardInfo : " + payment.getCardNum() + " " + payment.getMmyy() + " " + payment.getCvc());
		Utility.encrCardInfo(payment);
		System.out.println("encrCardInfo : " + payment.getEncrCardInfo());
		Utility.decrCardInfo(payment);
		System.out.println("decrCardInfo : " + payment.getCardNum() + " " + payment.getMmyy() + " " + payment.getCvc());

	}

	@Test
	void testMakePgString() {
		Payment payment = new Payment();
		payment.setId("XXXXXXXXXXXXXXXXXXXX");
		payment.setCardNum("1234567890123456");
		payment.setMmyy("1125");
		payment.setCvc("777");
		payment.setInstMm(0);
		payment.setAmount(110000);
		payment.setVat(10000);
		payment.setEncrCardInfo(
				"YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
		payment.setRefId("");
		payment.setPayType(cVariable.TYPE_PAYMENT);
		System.out.println("testMakePgString[TYPE_PAYMENT] : " + Utility.makePgString(payment));

		payment.setId("ZZZZZZZZZZZZZZZZZZZZ");
		payment.setRefId("XXXXXXXXXXXXXXXXXXXX");
		payment.setPayType(cVariable.TYPE_CANCLE);
		System.out.println("testMakePgString[TYPE_CANCLE] : " + Utility.makePgString(payment));
	}

}
