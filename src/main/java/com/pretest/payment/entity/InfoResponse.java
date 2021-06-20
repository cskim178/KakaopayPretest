package com.pretest.payment.entity;

public class InfoResponse {
	
	private String id;	
	private String cardNum;	
	private int mmyy;
	private int cvc;	
	private String payType;	
	private int amount;
	private int vat;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getVat() {
		return vat;
	}
	public void setVat(int vat) {
		this.vat = vat;
	}
	
}
