package com.porwau.reportgenerator.reportexecutor.remote;

import org.springframework.web.client.RestTemplate;

/**
 * @author Utkarsh Porwal<br>
 *Fetcher for Decryption keys from key-service
 */
public class KeysForDecryptionFetcher {
	private static final String KEYS_URL = "http://localhost:8112/keys/";

	/**
	 * @param bankId
	 * @return Decryption key for a bank id.
	 */
	public static String getDecryptionKey(Integer bankId) {
		String url = KEYS_URL + bankId;
		return new RestTemplate().getForObject(url, String.class);
	}
}