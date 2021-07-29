package com.porwau.reportgenerator.reportexecutor.remote;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.porwau.reportgenerator.common.model.Transaction;

/**
 * @author Utkarsh Porwal<br>
 *Fetcher for transactions  from transaction-service
 */
public class TransactionsFetcher {
	private static final String TRANSACTIONS_URL = "http://localhost:8111/transactions";
    public List<Transaction> getAllTransactions() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Transaction>> responseEntity = restTemplate.exchange(TRANSACTIONS_URL,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Transaction>>() {
                });
        List<Transaction> transactions = responseEntity.getBody();
        return transactions;
    }
}