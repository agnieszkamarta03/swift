package com.swift;

import com.swift.controllers.SwiftController;
import com.swift.entities.SwiftEntity;
import com.swift.repositories.SwiftRepository;
import com.swift.services.SwiftService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SwiftApplicationTests {
	@Mock
	private SwiftRepository swiftRepository;

	@InjectMocks
	private SwiftService swiftService;



	@Test
	void findBySwiftCode_ExistingCode() {
		String swiftCode = "BBVAUYMMXXX";
		SwiftEntity expectedEntity = new SwiftEntity("PL", swiftCode, "Test Bank", "Warsaw", "Poland", true);
		when(swiftRepository.findBySwiftCode(swiftCode)).thenReturn(Optional.of(expectedEntity));

		Optional<SwiftEntity> actualEntityOptional = swiftService.findBySwiftCode(swiftCode);

		assertTrue(actualEntityOptional.isPresent());
		SwiftEntity actualEntity = actualEntityOptional.get();
		assertEquals(expectedEntity.getSwiftCode(), actualEntity.getSwiftCode());
	}

	@Test
	void findBySwiftCode_NonExistingCode() {
		String swiftCode = "BHAHAUYMMXXX";
		SwiftEntity expectedEntity = new SwiftEntity("PL", swiftCode, "Test Bank", "Warsaw", "Poland", true);
		when(swiftRepository.findBySwiftCode(swiftCode)).thenReturn(Optional.empty());

		Optional<SwiftEntity> actualEntityOptional = swiftService.findBySwiftCode(swiftCode);

		assertTrue(actualEntityOptional.isEmpty());
	}

	@Test
	void findAllByCountryISO2_NonExistingCountry() {
		String countryCode = "XX";
		when(swiftRepository.findByCountryISO2(countryCode)).thenReturn(Collections.emptyList());

		List<SwiftEntity> actualEntities = swiftService.findAllByCountryISO2(countryCode);

		assertNotNull(actualEntities);
		assertTrue(actualEntities.isEmpty());
	}

	@Test
	void findAllByCountryISO2_ExistingCountry_ReturnsListOfSwiftEntities() {
		String countryCode = "PL";
		List<SwiftEntity> expectedEntities = Arrays.asList(
				new SwiftEntity("PL", "Test 1", "Bank 1", "Warsaw", "Poland", true),
				new SwiftEntity("PL", "Test 2", "Bank 2", "Warsaw", "Poland", true));
		when(swiftRepository.findByCountryISO2(countryCode)).thenReturn(expectedEntities);

		List<SwiftEntity> actualEntities = swiftService.findAllByCountryISO2(countryCode);

		assertNotNull(actualEntities);
		assertFalse(actualEntities.isEmpty());
		assertEquals(expectedEntities.size(), actualEntities.size());

	}




}
