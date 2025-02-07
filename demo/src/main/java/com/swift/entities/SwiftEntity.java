package com.swift.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "swift_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwiftEntity {
    @Id
    private String swiftCode;
    private String countryISO2;
    private String bankName;
    private String address;
    private String countryName;
    private boolean isHeadquarter;

}
