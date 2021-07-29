package com.porwau.reportgenerator.common.model;

public class KeyMap {

	private Integer bankId;
	private String symmetricKey;

	public KeyMap(Integer bankId, String symmetricKey) {
		super();
		this.bankId = bankId;
		this.symmetricKey = symmetricKey;
	}

	public KeyMap() {
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getSymmetricKey() {
		return symmetricKey;
	}

	public void setSymmetricKey(String symmetricKey) {
		this.symmetricKey = symmetricKey;
	}
}
