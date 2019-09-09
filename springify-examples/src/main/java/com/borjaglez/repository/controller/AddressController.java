package com.borjaglez.repository.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.borjaglez.repository.model.Address;
import com.borjaglez.repository.model.User;
import com.borjaglez.repository.repository.AddressRepository;
import com.borjaglez.springify.repository.filter.AdvancedPageFilter;
import com.borjaglez.springify.repository.filter.AnyPageFilter;
import com.borjaglez.springify.repository.filter.BasicPageFilter;
import com.borjaglez.springify.repository.filter.Filter;
import com.borjaglez.springify.repository.filter.PageFilter;
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
	public @ResponseBody List<Address> selectFromFindAll(@RequestBody Filter filter) {
		return SpecificationBuilder.selectFrom(addressRepository).findAll();
	}

	@PostMapping(path = "/getAddress", produces = "application/json")
	@Transactional(readOnly = true)
	public @ResponseBody List<Address> getAddress(@RequestBody Filter filter) {
		return SpecificationBuilder.selectFrom(addressRepository).where(filter).findAll();
	}

	@PostMapping(path = "/selectFromFindAllPagination", produces = "application/json")
	@Transactional(readOnly = true)
	public @ResponseBody List<Address> selectFromFindAllPagination(@RequestBody PageFilter pageFilter) {
		return SpecificationBuilder.selectFrom(addressRepository).findAll(pageFilter).getContent();
	}

	@PostMapping(path = "/basicPageFilter", produces = "application/json")
	@Transactional(readOnly = true)
	public @ResponseBody List<Address> basicPageFilter(@RequestBody BasicPageFilter basicPageFilter) {
		return SpecificationBuilder.selectFrom(addressRepository).where(basicPageFilter).findAll(basicPageFilter)
				.getContent();
	}

	@PostMapping(path = "/advancedPageFilter", produces = "application/json")
	@Transactional(readOnly = true)
	public @ResponseBody List<Address> advancedPageFilter(@RequestBody AdvancedPageFilter advancedPageFilter) {
		return SpecificationBuilder.selectFrom(addressRepository)
				.where(advancedPageFilter, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).findAll(advancedPageFilter)
				.getContent();
	}

	@PostMapping(path = "/almostFilter", produces = "application/json")
	@Transactional(readOnly = true)
	public @ResponseBody List<Address> almostFilter(@RequestBody AnyPageFilter anyPageFilter) {
		return SpecificationBuilder.selectDistinctFrom(addressRepository).where(anyPageFilter).findAll(anyPageFilter)
				.getContent();
	}

	@PostMapping(path = "/almostFilter2", produces = "application/json")
	@Transactional(readOnly = true)
	public @ResponseBody List<Address> almostFilter2(@RequestBody AnyPageFilter anyPageFilter) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Address> query = cb.createQuery(Address.class);

		Root<Address> root = query.from(Address.class);
		Join<Address, User> join = root.join("users", JoinType.LEFT);
		query = query.select(root)
				.where(cb.or(cb.like(root.get("name"), "%Test%"), cb.like(join.get("name"), "%Test%")));
		return entityManager.createQuery(query.distinct(true)).getResultList();
	}
}
