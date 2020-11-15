package com.test.musala.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface FilterJpaRepo<T, K> extends JpaRepository<T, K> {
}
