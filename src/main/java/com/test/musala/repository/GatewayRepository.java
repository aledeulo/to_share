package com.test.musala.repository;

import com.test.musala.model.Gateway;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayRepository extends CrudRepository<Gateway, String> {
}
