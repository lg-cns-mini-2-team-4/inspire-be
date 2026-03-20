package com.inspire.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inspire.user.domain.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {}
