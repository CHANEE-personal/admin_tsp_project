package com.tsp.new_tsp_admin.common;

import com.tsp.new_tsp_admin.api.domain.model.CareerJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
class CustomConverterTest {
    @Autowired private CustomConverter customConverter;

    @Test
    @DisplayName("convertToDataBaseColumnTest")
    void convertToDataBaseColumnTest() {
        List<CareerJson> careerList = of(new CareerJson("title", "text"));
        assertThat(customConverter.convertToDatabaseColumn(careerList))
                .isEqualTo("[{\"title\":\"title\",\"txt\":\"text\"}]");
    }

    @Test
    @DisplayName("convertToEntityAttributeTest")
    void convertToEntityAttributeTest() {
        String dbData = "[{\"title\":\"title\",\"txt\":\"txt\"}]";
        assertNotNull(customConverter.convertToEntityAttribute(dbData));
    }
}