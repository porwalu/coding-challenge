package com.porwau.reportgenerator.keyservice.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * 
 * @author Utkarsh Porwal <br>
 *         This is a DAO layer with mock data for keymap.
 *         This will create a static datastructure to hold dummy data.
 *         This also has data accessor methods.
 */
@Component
public class KeyMapDao {
	private static final Map<Integer, String> KEY_MAP = new HashMap<>();
	static {
		createMockKeyMap();
	}

	/**
	 * Creates a static KEY_MAP datastructure to hold dummy data.<br>
	 * The format of the map is(bankId,symmetrickey).
	 */
	private static void createMockKeyMap() {
		for (int i = 0; i < 10; i++) {
			Integer bankId = 2001 + i;
			String symmetricKey = "password" + i;
			KEY_MAP.put(bankId, symmetricKey);
		}
	}

	/**
	 * @return The static KEY_MAP datastructure.
	 */
	public Map<Integer, String> getKeymap() {
		return KEY_MAP;
	}

	/**
	 * @param id Takes the Bank Id as the key.
	 * @return The symmetric key used to decrypt transactions for this Bank Id.
	 */
	public String getKeyByID(Integer id) {
		// TODO Auto-generated method stub
		return KEY_MAP.get(id);
	}
}
