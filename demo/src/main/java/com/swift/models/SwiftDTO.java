package com.swift.models;

import lombok.*;


@NoArgsConstructor
@Data
@AllArgsConstructor
public class SwiftDTO {
    private String countryISO2;
    private String swiftCode;
    private String bankName;
    private String address;
    private boolean isHeadquarter;
    private String countryName;

}