package com.borjaglez.repository.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.borjaglez.repository.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer>, JpaSpecificationExecutor<Address> {

}
