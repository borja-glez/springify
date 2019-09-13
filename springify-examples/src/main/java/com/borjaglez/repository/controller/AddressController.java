package com.borjaglez.repository.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.borjaglez.repository.model.Address;
import com.borjaglez.repository.repository.AddressRepository;
import com.borjaglez.springify.repository.filter.impl.Filter;
import com.borjaglez.springify.repository.filter.impl.PageFilter;
import com.borjaglez.springify.repository.specification.SpecificationBuilder;

@RestController
@RequestMapping("address")
public class AddressController {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private AddressRepository addressRepository;

	@PostMapping(path = "/getAllAddress", produces = "application/json")
	@Transactional(readOnly = true)
	public @ResponseBody List<Address> getAllAddress() {
		return addressRepository.findAll();
	}

	@PostMapping(path = "/selectFromFindAll", produces = "application/json")
	@Transactional(readOnly = true)
	public @ResponseBody List<Address> selectFromFindAll() {
		return SpecificationBuilder.selectFrom(addressRepository).findAll();
	}

	@PostMapping(path = "/getAddress", produces = "application/json")
	@Transactional(readOnly = true)
	public @ResponseBody List<Address> getAddress(@RequestBody Filter filter) {
		return SpecificationBuilder.selectFrom(addressRepository).leftJoin("users").where(filter).findAll();
	}

	@PostMapping(path = "/selectFromFindAllPagination", produces = "application/json")
	@Transactional(readOnly = true)
	public @ResponseBody List<Address> selectFromFindAllPagination(@RequestBody PageFilter pageFilter) {
		return SpecificationBuilder.selectFrom(addressRepository).findAll(pageFilter).getContent();
	}
}
