package com.porwau.reportgenerator.keyservice.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class KeyMapDao {
	private static Map<Integer,String> keyMap = new HashMap<>();
	static {
		for (int i = 0; i < 10; i++) {
			Integer bankId = 2001 + i;
			String symmetricKey = "password" + i;
			keyMap.put( bankId, symmetricKey);
		}
	}
	

	public Map<Integer, String> getKeymap() {
		return keyMap;
	}
	public String getKeyByID(Integer id) {
		// TODO Auto-generated method stub
		return keyMap.get(id);
	}
}
