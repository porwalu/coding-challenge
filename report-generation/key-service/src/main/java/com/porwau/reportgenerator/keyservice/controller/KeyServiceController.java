package com.porwau.reportgenerator.keyservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.porwau.reportgenerator.keyservice.dao.KeyMapDao;

/**
 * @author Utkarsh Porwal
 * <br> Controller which exposed rest endpoints for talking to the dao layer for getting symmetric key for a Bank ID.
 */
@RestController
public class KeyServiceController {
	
	@Autowired
	private KeyMapDao keymapdao;
	
	/**
	 * @return Mapping of Bank Id and respective symmetric key.
	 */
	@GetMapping("/keys")
	public Map<Integer, String> getAllKeys() {
		//return "In Key Service";
		return keymapdao.getKeymap();
	}
	
	/**
	 * @param id  Takes the key value from path parameter which is basically the Bank Id.
	 * @return The symmetric key for the Bank Id.
	 */
	@GetMapping("/keys/{id}")
	public String getKeyByID(@PathVariable Integer id) {
		//return "In Key Service";
		return keymapdao.getKeyByID(id);
	}
}
