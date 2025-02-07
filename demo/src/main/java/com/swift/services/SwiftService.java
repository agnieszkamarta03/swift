package com.swift.services;

import com.swift.entities.SwiftEntity;
import com.swift.models.SwiftDTO;
import com.swift.parsers.SwiftParser;
import com.swift.repositories.SwiftRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SwiftService {

    @Autowired
    private SwiftRepository swiftRepository;


    @PostConstruct
    public void initializeData() {
        saveToDatabase();
    }

    public void saveToDatabase() {
        List<SwiftDTO> swiftDTOList = SwiftParser.parseSwiftFile("Interns_2025_SWIFT_CODES.xlsx");
        if (!swiftDTOList.isEmpty()) {
            List<SwiftEntity> swiftEntities = swiftDTOList.stream()
                    .map(this::convertToEntity)
                    .toList();
            swiftRepository.saveAll(swiftEntities);
            System.out.println("Successfully saved to database");
        } else {
            System.out.println("Error occurred");
        }
    }

    private SwiftEntity convertToEntity(SwiftDTO swiftDTO) {
        return new SwiftEntity(
                swiftDTO.getSwiftCode(),
                swiftDTO.getCountryISO2(),
                swiftDTO.getBankName(),
                swiftDTO.getAddress(),
                swiftDTO.getCountryName(),
                swiftDTO.isHeadquarter()
        );
    }


    public Optional<SwiftEntity> findBySwiftCode(String swiftCode) {
        return swiftRepository.findBySwiftCode(swiftCode);
    }

    public List<SwiftEntity> findAllBranchesForHeadquarter(String headquarterPrefix) {
        return swiftRepository.findBySwiftCodeStartingWith(headquarterPrefix);
    }

    public List<SwiftEntity> findAllByCountryISO2(String countryISO2) {
        return swiftRepository.findByCountryISO2(countryISO2);
    }

    public void saveSwiftCode(SwiftEntity swiftEntity) {
        swiftRepository.save(swiftEntity);
    }

    public boolean deleteSwiftCode(String swiftCode) {
        Optional<SwiftEntity> swiftEntityOptional = swiftRepository.findBySwiftCode(swiftCode);
        if (swiftEntityOptional.isPresent()) {
            swiftRepository.delete(swiftEntityOptional.get());
            return true;
        }
        return false;
    }
}
