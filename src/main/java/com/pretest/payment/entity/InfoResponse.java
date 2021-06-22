package com.pretest.payment.entity;

import java.util.ArrayList;

public class InfoResponse {
	
	private String id;	
	private String cardNum;	
	private String mmyy;
	private String cvc;	
	private String payType;	
	private int amount;
	private int vat;
//	private ArrayList<Payment> paymentList = new ArrayList<Payment>();
	
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
	public String getMmyy() {
		return mmyy;
	}
	public void setMmyy(String mmyy) {
		this.mmyy = mmyy;
	}
	public String getCvc() {
		return cvc;
	}
	public void setCvc(String cvc) {
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
//	public ArrayList<Payment> getPaymentList() {
//		return paymentList;
//	}
//	public void setPaymentList(ArrayList<Payment> paymentList) {
//		this.paymentList = paymentList;
//	}
	@Override
	public String toString() {
		return "InfoResponse [id=" + id + ", cardNum=" + cardNum + ", mmyy=" + mmyy + ", cvc=" + cvc + ", payType="
				+ payType + ", amount=" + amount + ", vat=" + vat + "]";
	}
	
}
