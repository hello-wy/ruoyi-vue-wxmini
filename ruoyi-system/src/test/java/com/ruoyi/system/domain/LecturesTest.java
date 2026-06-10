package com.ruoyi.system.domain;

import org.junit.jupiter.api.Test;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;

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
    void exposesEnrolledCountBeanProperty() throws Exception {
        assertBeanPropertyExists("enrolledCount");
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
