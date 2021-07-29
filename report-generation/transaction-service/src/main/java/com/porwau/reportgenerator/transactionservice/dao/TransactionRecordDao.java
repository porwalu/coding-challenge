package com.porwau.reportgenerator.transactionservice.dao;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.porwau.reportgenerator.common.model.Transaction;
import com.porwau.reportgenerator.common.utilities.EncryptDecrypt;

@Component
public class TransactionRecordDao {

	private static List<Transaction> transactionlist = new ArrayList<>();
	private static String[] users = { "Utkarsh", "Rahul", "Rajat", "Ram", "Basu", "Umesh", "Sheetal", "Uma", "Ashutosh",
			"Chaitanya" };
	static {
		for (int i = 0; i < 10; i++) {
			String symmetricKey = "password" + i;
			String transaction = users[i] + " paid 100 rupees to " + users[9 - i] + ".";
			SecretKeySpec key;
			try {
				key = EncryptDecrypt.createSecretKey(symmetricKey.toCharArray());
				String encryptedTransaction = EncryptDecrypt.encrypt(transaction, key);
				int bankId = 2001 + i;
				long transactionId = (long)(1001 + i);
				// System.out.println("Original password: " + password);
				transactionlist.add(new Transaction(transactionId, bankId, encryptedTransaction,
						Instant.now().getEpochSecond() + i * 1000));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public List<Transaction> getAllTransactions() {
		return transactionlist;
	}
}
