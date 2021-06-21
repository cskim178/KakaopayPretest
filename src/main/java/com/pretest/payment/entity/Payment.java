package com.pretest.payment.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "PAYMENT")
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20)
	private String id;

	@Column(length = 1)
	private String payType;

	@Column
	private String encrCardInfo;
	
	@Transient
	@Column(length = 20)
	private String cardNum;

	@Column(length = 2)
	private String instMm;
	
	@Transient
	@Column(length = 4)
	private String mmyy;

	@Transient
	@Column(length = 3)
	private String cvc;

	@Column
	private int amount;

	@Column
	private Integer vat;

	@Column(length = 20)
	private String refId;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date create;
	
	public Payment() {
		
	}
	
	public Payment(String id, String payType, String encrCardInfo, String cardNum, String instMm, String mmyy, String cvc,
			int amount, Integer vat, String refId, Date create) {
		super();
		this.id = id;
		this.payType = payType;
		this.encrCardInfo = encrCardInfo;
		this.cardNum = cardNum;
		this.instMm = instMm;
		this.mmyy = mmyy;
		this.cvc = cvc;
		this.amount = amount;
		this.vat = vat;
		this.refId = refId;
		this.create = create;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getEncrCardInfo() {
		return encrCardInfo;
	}

	public void setEncrCardInfo(String encrCardInfo) {
		this.encrCardInfo = encrCardInfo;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getInstMm() {
		return instMm;
	}

	public void setInstMm(String instMm) {
		this.instMm = instMm;
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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Integer getVat() {
		return vat;
	}

	public void setVat(Integer vat) {
		this.vat = vat;
	}
	
	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

	@Override
	public String toString() {
		return "id : " + id + ", payType : " + payType + ", encrCardInfo : " + encrCardInfo + ", instMm : " + instMm
				+ ", amount : " + amount + ", vat : " + vat + ", refId : " + refId + ", cardNum : " + cardNum + ", mmyy : "
				+ mmyy + ", cvc : " + cvc + ", vat : " + vat;
	}

}
