package com.study.chapter3.chapter3.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("user")
public record User(@Id Long id, String name, String email) {
}
