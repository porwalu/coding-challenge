package com.porwau.reportgenerator.transactionservice.model;

public class TransactionRecord {
	
	private Long transactionId;
	private Integer bankId;
	private String encryptedData;
	private Long unixtime;
	
	public TransactionRecord(Long transactionId, Integer bankId, String encryptedData, Long unixtime) {
		super();
		this.transactionId = transactionId;
		this.bankId = bankId;
		this.encryptedData = encryptedData;
		this.unixtime = unixtime;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getEncryptedData() {
		return encryptedData;
	}

	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}

	public Long getUnixtime() {
		return unixtime;
	}

	public void setUnixtime(Long unixtime) {
		this.unixtime = unixtime;
	}
	
	

}
