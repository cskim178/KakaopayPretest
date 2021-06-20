package com.pretest.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.pretest.payment.entity.Transaction;
import com.pretest.payment.repository.TransactionRepository;

public class TracsactionController{
	
	@Autowired
	private TransactionRepository tranRepo;
	
	public void payment(Transaction transaction) {
		tranRepo.save(transaction);		
	}

}
