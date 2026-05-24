package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TutoringSchedule;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TutoringScheduleRealNameMapperContractTest {

    private static final Path MAPPER_XML = Paths.get(
            "src/main/resources/mapper/system/TutoringScheduleMapper.xml");

    @Test
    void adminScheduleRowsExposeTutorRealNameField() throws Exception {
        String xml = new String(Files.readAllBytes(MAPPER_XML), StandardCharsets.UTF_8);

        assertTrue(xml.contains("property=\"realName\" column=\"real_name\""));
        assertTrue(xml.contains("ui.real_name as real_name"));
        assertTrue(xml.contains("ui.real_name as tutor_name"));
        assertTrue(TutoringSchedule.class.getDeclaredField("realName").getType().equals(String.class));
    }
}
