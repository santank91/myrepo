package com.infosys.k8smp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.k8smp.response.LookupDataResponse;
import com.infosys.k8smp.service.LookUpDataService;

@RestController
@RequestMapping(path = "/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LookupDataController {


	@Autowired
	private LookUpDataService lookUpDataService;

	private static final Logger logger = LoggerFactory.getLogger(LookupDataController.class);

	@GetMapping("/lookupOptionsData")
	public ResponseEntity<LookupDataResponse> lookupOptionsData() throws Exception {

		try {

			LookupDataResponse lookupDataResponse = lookUpDataService.constructLookUpData();

			return new ResponseEntity<LookupDataResponse>(lookupDataResponse, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Exception occurred", ex);
			throw new Exception(ex);
		}

	}

}
