package com.porwau.reportgenerator.reportexecutor.remote;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Utkarsh Porwal<br>
 *         Fetcher for Decryption keys from key-service
 */
public class KeysForDecryptionFetcher {
	private  Map<Integer, String> keyCache = new HashMap<>();
	private static final String KEYS_URL = "http://localhost:8112/keys/";
	private static Logger logger = LoggerFactory.getLogger("KeysForDecryptionFetcher.class");

	/**
	 * @param bankId
	 * @return Decryption key for a bank id.
	 */
	public String getDecryptionKey(Integer bankId) {
		String url = KEYS_URL + bankId;
		String symmKey = null;
		if (keyCache.containsKey(bankId)) {
			symmKey = keyCache.get(bankId);
			logger.info("keyCache Hit");
		} else {
			logger.info("keyCache Miss");
			symmKey = new RestTemplate().getForObject(url, String.class);
			keyCache.put(bankId, symmKey);
		}
		return symmKey;
	}
}