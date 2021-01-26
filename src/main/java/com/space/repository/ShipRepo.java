package com.space.repository;   // this package id for DB interaction

import com.space.model.Ship;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ShipRepo extends
        CrudRepository<Ship, Long>,
        JpaSpecificationExecutor<Ship>,
        PagingAndSortingRepository<Ship, Long> {}








