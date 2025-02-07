package com.swift.controllers;

import com.swift.entities.SwiftEntity;
import com.swift.models.e1.BranchInHeadquarterResponse;
import com.swift.models.e1.BranchResponse;
import com.swift.models.e1.HeadquarterResponse;
import com.swift.models.e2.CountryResponse;
import com.swift.models.e2.SwiftCodeResponse;
import com.swift.models.e3.NewUserRequest;
import com.swift.models.message.MessageResponse;
import com.swift.services.SwiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftController {

    private final SwiftService swiftService;

    @Autowired
    public SwiftController(SwiftService swiftService) {
        this.swiftService = swiftService;
    }

    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> getSwiftCodeDetails(@PathVariable String swiftCode) {
        Optional<SwiftEntity> swiftEntityOptional = swiftService.findBySwiftCode(swiftCode.toUpperCase());

        if (swiftEntityOptional.isEmpty()) {
            return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.NOT_FOUND);
        }

        SwiftEntity swiftEntity = swiftEntityOptional.get();
        if (swiftEntity.isHeadquarter()) {
            List<SwiftEntity> branches = swiftService.findAllBranchesForHeadquarter(swiftEntity.getSwiftCode().substring(0, 8));

            List<BranchInHeadquarterResponse> branchDTOs = branches.stream()
                    .map(branch -> new BranchInHeadquarterResponse(
                            branch.getAddress(),
                            branch.getBankName(),
                            branch.getCountryISO2(),
                            branch.getSwiftCode()
                    ))
                    .collect(Collectors.toList());

            HeadquarterResponse headquarterDTO = new HeadquarterResponse(
                    swiftEntity.getAddress(),
                    swiftEntity.getBankName(),
                    swiftEntity.getCountryISO2(),
                    swiftEntity.getCountryName(),
                    swiftEntity.isHeadquarter(),
                    swiftEntity.getSwiftCode(),
                    branchDTOs
            );
            return ResponseEntity.ok(headquarterDTO);
        } else {
            BranchResponse branchDTO = new BranchResponse(
                    swiftEntity.getAddress(),
                    swiftEntity.getBankName(),
                    swiftEntity.getCountryISO2(),
                    swiftEntity.getCountryName(),
                    swiftEntity.isHeadquarter(),
                    swiftEntity.getSwiftCode()
            );
            return ResponseEntity.ok(branchDTO);
        }
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<?> getSwiftCodesByCountry(@PathVariable String countryISO2code) {
        List<SwiftEntity> swiftEntities = swiftService.findAllByCountryISO2(countryISO2code.toUpperCase());

        if (swiftEntities.isEmpty()) {
            return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.NOT_FOUND);
        }

        List<SwiftCodeResponse> swiftCode = swiftEntities.stream()
                .map(swiftEntity -> new SwiftCodeResponse(
                        swiftEntity.getAddress(),
                        swiftEntity.getBankName(),
                        swiftEntity.getCountryISO2(),
                        swiftEntity.isHeadquarter(),
                        swiftEntity.getSwiftCode()
                ))
                .collect(Collectors.toList());

        CountryResponse countryResponse = new CountryResponse(
                countryISO2code.toUpperCase(),
                swiftEntities.get(0).getCountryName(),
                swiftCode
        );

        return ResponseEntity.ok(countryResponse);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addSwiftCode(@RequestBody NewUserRequest request) {
        if (request.getSwiftCode() == null || request.getSwiftCode().isEmpty() ||
                request.getCountryISO2() == null || request.getCountryISO2().isEmpty() ||
                request.getBankName() == null || request.getBankName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("More info required"));
        }

        Optional<SwiftEntity> existingSwiftCode = swiftService.findBySwiftCode(request.getSwiftCode().toUpperCase());
        if (existingSwiftCode.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("User already exists"));
        }

        SwiftEntity swiftEntity = new SwiftEntity(
                request.getCountryISO2().toUpperCase(),
                request.getSwiftCode().toUpperCase(),
                request.getBankName(),
                request.getAddress(),
                request.getCountryName(),
                request.isHeadquarter()
        );
        swiftService.saveSwiftCode(swiftEntity);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("User added"));
    }

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<MessageResponse> deleteSwiftCode(@PathVariable String swiftCode) {
        boolean deleted = swiftService.deleteSwiftCode(swiftCode.toUpperCase());
        if (deleted) {
            return ResponseEntity.ok(new MessageResponse("Swift code deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Swift code not found"));
        }

    }
}
