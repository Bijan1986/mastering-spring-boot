package com.study.chapter3.chapter3.repository;

import com.study.chapter3.chapter3.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {
    Mono<User> findByEmail(String email);
}
