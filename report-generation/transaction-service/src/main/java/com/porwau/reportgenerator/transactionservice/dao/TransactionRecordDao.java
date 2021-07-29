package com.porwau.reportgenerator.transactionservice.dao;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.porwau.reportgenerator.common.model.Transaction;
import com.porwau.reportgenerator.common.utilities.EncryptDecrypt;

/**
 * @author Utkarsh Porwal<br>
 *         This is a DAO layer with mock data for transactions.This will create
 *         a static datastructure to hold dummy data. This also has data
 *         accessor methods. Structure is defined by {@link Transaction}.
 *
 */
@Component
public class TransactionRecordDao {

	private static Logger logger = LoggerFactory.getLogger("TransactionRecordDao.class");
	private static final List<Transaction> MOCK_TRANSACTIONS = new ArrayList<>();
	private static final String[] MOCK_USERS = { "Utkarsh", "Rahul", "Rajat", "Ram", "Basu", "Umesh", "Sheetal", "Uma",
			"Ashutosh", "Chaitanya" };
	static {
		createMockTransactions();
	}

	/**
	 * Method which creates Mock transaction data.
	 */
	private static void createMockTransactions() {
		for (int i = 0; i < 10; i++) {
			String symmetricKey = "password" + i;
			String transaction = MOCK_USERS[i] + " paid 100 rupees to " + MOCK_USERS[9 - i] + ".";
			SecretKeySpec key;
			try {
				key = EncryptDecrypt.createSecretKey(symmetricKey.toCharArray());
				String encryptedTransaction = EncryptDecrypt.encrypt(transaction, key);
				int bankId = 2001 + i;
				long transactionId = (long) (1001 + i);
				MOCK_TRANSACTIONS.add(new Transaction(transactionId, bankId, encryptedTransaction,
						Instant.now().getEpochSecond() + i * 1000));

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Fatal Error while Encryption. Review the implementation.", e);
				System.exit(1);
			}
		}
	}

	/**
	 * @return List of Transactions.
	 */
	public List<Transaction> getAllTransactions() {
		return MOCK_TRANSACTIONS;
	}
}
