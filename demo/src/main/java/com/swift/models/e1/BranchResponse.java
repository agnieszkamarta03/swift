package com.swift.models.e1;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchResponse {
    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private boolean isHeadquarter;
    private String swiftCode;
}
