package com.swift.models.e1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchInHeadquarterResponse {
    private String address;
    private String bankName;
    private String countryISO2;
    private String swiftCode;


}
