package com.borjaglez.repository.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.borjaglez.repository.entity.UserStateEnum;
import com.borjaglez.repository.model.User;
import com.borjaglez.repository.repository.UserRepository;
import com.borjaglez.springify.repository.filter.Filter;
import com.borjaglez.springify.repository.filter.PageFilter;
import com.borjaglez.springify.repository.filter.Filter.Operator;
import com.borjaglez.springify.repository.specification.SpecificationBuilder;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping(path = "/getAllUsers", produces = "application/json")
	public @ResponseBody List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@PostMapping(path = "/selectFromFindAll", produces = "application/json")
	public @ResponseBody List<User> selectFromFindAll(@RequestBody Filter filter) {
		return SpecificationBuilder.selectFrom(userRepository).findAll();
	}

	@PostMapping(path = "/getUsers", produces = "application/json")
	public @ResponseBody List<User> getUsers(@RequestBody Filter filter) {
		return SpecificationBuilder.selectFrom(userRepository).where(filter).findAll();
	}

	@PostMapping(path = "/getActiveUsers", produces = "application/json")
	public @ResponseBody List<User> getActiveUsers(@RequestBody Filter filter) {
		Filter stateActiveFilter = Filter.and(new Filter("state", Operator.EQUAL, UserStateEnum.ACTIVE));
		return SpecificationBuilder.selectFrom(userRepository).where(filter).where(stateActiveFilter).findAll();
	}
	
	@PostMapping(path = "/getActiveUsersSorting", produces = "application/json")
	public @ResponseBody Page<User> getActiveUsersSorting(@RequestBody PageFilter pageFilter) {
		Filter stateActiveFilter = Filter.and(new Filter("state", Operator.EQUAL, UserStateEnum.ACTIVE));
		return SpecificationBuilder.selectFrom(userRepository).where(pageFilter).where(stateActiveFilter).findAll(pageFilter);
	}
	
	@PostMapping(path = "/selectFromFindAllPagination", produces = "application/json")
	public @ResponseBody List<User> selectFromFindAllPagination(@RequestBody PageFilter pageFilter) {
		return SpecificationBuilder.selectFrom(userRepository).findAll(pageFilter).getContent();
	}
}
