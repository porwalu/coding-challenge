package com.porwau.reportgenerator.keyservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.porwau.reportgenerator.keyservice.dao.KeyMapDao;

@RestController
public class KeyServiceController {
	
	@Autowired
	KeyMapDao keymapdao;
	
	@GetMapping("/keys")
	public Map<Integer, String> getAllKeys() {
		//return "In Key Service";
		return keymapdao.getKeymap();
	}
	
	@GetMapping("/keys/{id}")
	public String getKeyByID(@PathVariable Integer id) {
		//return "In Key Service";
		return keymapdao.getKeyByID(id);
	}
}
