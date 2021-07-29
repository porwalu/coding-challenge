package com.porwau.reportgenerator.transactionservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.porwau.reportgenerator.common.model.Transaction;
import com.porwau.reportgenerator.transactionservice.dao.TransactionRecordDao;

@RestController
public class TransactionServiceController {
	
	@Autowired
	TransactionRecordDao transactionDao;
	@GetMapping("/transactions")
	public List<Transaction> getTransactionData() {
		return transactionDao.getAllTransactions();
	}

}
