package com.pretest.payment.controller;


import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.pretest.payment.entity.Transaction;
import com.pretest.payment.repository.TransactionRepository;

@SpringBootTest
@Transactional
public class TransactionRepositoryTest {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@BeforeEach
	void setUp() throws Exception {
		
	}

	@Test
	void testSave() {
		Transaction tran = new Transaction();
		
		tran.setContent("TransactionTest 1.");
		transactionRepository.save(tran);		
		
		tran.setContent("TransactionTest 2.");
		transactionRepository.save(tran);
		
		List<Transaction> tranList = transactionRepository.findAll();
				 
		for( Transaction tranObj : tranList ) { 
			System.out.println("tranObj : " +  tranObj.getContent());
		}
		
	}

}
