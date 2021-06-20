package com.pretest.payment.entity;

public class PaymentRequest {
	
	private String cardNum;	
	private int mmyy;
	private int cvc;
	private int instMm;
	private int amount;
	private String vat;
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public int getMmyy() {
		return mmyy;
	}
	public void setMmyy(int mmyy) {
		this.mmyy = mmyy;
	}
	public int getCvc() {
		return cvc;
	}
	public void setCvc(int cvc) {
		this.cvc = cvc;
	}
	public int getInstMm() {
		return instMm;
	}
	public void setInstMm(int instMm) {
		this.instMm = instMm;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getVat() {
		return vat;
	}
	public void setVat(String vat) {
		this.vat = vat;
	}

}
