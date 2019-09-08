package com.borjaglez.repository.repository;

import com.borjaglez.repository.model.User;
import com.borjaglez.springify.repository.jpa.ExtendedJpaRepository;

public interface UserRepository extends ExtendedJpaRepository<User, Integer> {

}
