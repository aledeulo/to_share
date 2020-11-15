package com.test.musala.repository;

import com.test.musala.model.Peripheral;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeripheralRepository extends CrudRepository<Peripheral, Long> {
}
