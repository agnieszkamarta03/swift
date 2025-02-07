package com.swift.parsers;

import com.swift.models.SwiftDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class SwiftParser {

    private static final Map<String, String> COUNTRY_NAMES = new HashMap<>();

    static {
        COUNTRY_NAMES.put("AL", "Albania");
        COUNTRY_NAMES.put("BG", "Bulgaria");
        COUNTRY_NAMES.put("UY", "Uruguay");
        COUNTRY_NAMES.put("MC", "Monaco");
        COUNTRY_NAMES.put("PL", "Poland");
        COUNTRY_NAMES.put("LV", "Latvia");
        COUNTRY_NAMES.put("MT", "Malta");
        COUNTRY_NAMES.put("AW", "Aruba");
    }

    private static String getCountryName(String countryISO2) {
        return COUNTRY_NAMES.getOrDefault(countryISO2.toUpperCase(), "Unknown");
    }



    public static List<SwiftDTO> parseSwiftFile(String fileLocation) {

        List<SwiftDTO> swiftList = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(new File(fileLocation));
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet){
                if (row.getRowNum() == 0) continue;
                String countryISO2 = row.getCell(0).getStringCellValue().toUpperCase();
                String swiftCode = row.getCell(1).getStringCellValue().toUpperCase();
                String bankName = row.getCell(3).getStringCellValue();
                String address = row.getCell(4).getStringCellValue();

                boolean isHeadquarter = swiftCode.endsWith("XXX");
                String countryName = getCountryName(countryISO2);

                SwiftDTO swiftDTO = new SwiftDTO();
                swiftDTO.setCountryISO2(countryISO2);
                swiftDTO.setSwiftCode(swiftCode);
                swiftDTO.setBankName(bankName);
                swiftDTO.setAddress(address);
                swiftDTO.setHeadquarter(isHeadquarter);
                swiftDTO.setCountryName(countryName);

                swiftList.add(swiftDTO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return swiftList;
    }

//    public static void main(String[] args) {
//        String filePath = "Interns_2025_SWIFT_CODES.xlsx";
//        List<SwiftDTO> swiftList = SwiftParser.parseSwiftFile(filePath);
//        swiftList.stream().limit(5).forEach(System.out::println);
//    }
}
