package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LecturesTest {

    @Test
    void exposesCoverIdBeanProperty() throws Exception {
        assertBeanPropertyExists("coverId");
    }

    @Test
    void exposesRequiresEnrollmentBeanProperty() throws Exception {
        assertBeanPropertyExists("requiresEnrollment");
    }

    @Test
    void exposesIsTopBeanProperty() throws Exception {
        assertBeanPropertyExists("isTop");
    }

    @Test
    void exposesEnrolledCountBeanProperty() throws Exception {
        assertBeanPropertyExists("enrolledCount");
    }

    @Test
    void exposesCoursePriceBeanProperty() throws Exception {
        assertBeanPropertyExists("coursePrice");
    }

    @Test
    void coursePriceUpdatedSupportsExplicitNullClearing() throws Exception {
        Lectures lectures = new Lectures();
        lectures.setCoursePrice(new BigDecimal("3980.00"));
        lectures.setCoursePrice(null);
        lectures.setCoursePriceUpdated(Boolean.TRUE);

        assertTrue(lectures.getCoursePriceUpdated());
        assertNull(lectures.getCoursePrice());
        assertBeanPropertyExists("coursePriceUpdated");

        Field field = Lectures.class.getDeclaredField("coursePriceUpdated");
        assertEquals(JsonProperty.Access.WRITE_ONLY, field.getAnnotation(JsonProperty.class).access());
    }

    @Test
    void mapperPersistsHomepageRecentCoursePinning() {
        InputStream mapperStream = Objects.requireNonNull(
                getClass().getResourceAsStream("/mapper/system/LecturesMapper.xml"));
        String mapperXml = new Scanner(mapperStream, StandardCharsets.UTF_8.name())
                .useDelimiter("\\A").next();

        assertTrue(mapperXml.contains("<if test=\"isTop != null\">is_top = #{isTop},</if>"));
    }

    @Test
    void mapperUpdatesCoursePriceOnlyWhenExplicitlyRequested() {
        InputStream mapperStream = Objects.requireNonNull(
                getClass().getResourceAsStream("/mapper/system/LecturesMapper.xml"));
        String mapperXml = new Scanner(mapperStream, StandardCharsets.UTF_8.name())
                .useDelimiter("\\A").next();

        assertTrue(mapperXml.contains(
                "<if test=\"coursePriceUpdated != null and coursePriceUpdated\">course_price = #{coursePrice},</if>"));
    }

    private void assertBeanPropertyExists(String propertyName) throws Exception {
        PropertyDescriptor[] descriptors = Introspector.getBeanInfo(Lectures.class).getPropertyDescriptors();

        boolean exists = Arrays.stream(descriptors)
                .anyMatch(descriptor -> propertyName.equals(descriptor.getName())
                        && descriptor.getReadMethod() != null
                        && descriptor.getWriteMethod() != null);

        assertTrue(exists, "Lectures should expose readable and writable " + propertyName + " property");
    }
}
