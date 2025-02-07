package com.swift.repositories;

import com.swift.entities.SwiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SwiftRepository extends JpaRepository<SwiftEntity, Integer> {

    Optional<SwiftEntity> findBySwiftCode(String swiftCode);

    List<SwiftEntity> findBySwiftCodeStartingWith(String headquarterPrefix);

    List<SwiftEntity> findByCountryISO2(String countryISO2);
}

