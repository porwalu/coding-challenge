package com.porwau.reportgenerator.transactionservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.porwau.reportgenerator.common.model.Transaction;
import com.porwau.reportgenerator.transactionservice.dao.TransactionRecordDao;

/**
 * @author Utkarsh Porwal
 * <br> Controller which exposes rest endpoints for talking to the dao layer for getting transaction data.
 */
@RestController
public class TransactionServiceController {
	
	@Autowired
	TransactionRecordDao transactionDao;
	/**
	 * @return List of transactions.
	 */
	@GetMapping("/transactions")
	public List<Transaction> getTransactionData() {
		return transactionDao.getAllTransactions();
	}

}
