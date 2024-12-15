package com.authentication.JWT_AUTH.repository;

import com.authentication.JWT_AUTH.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

