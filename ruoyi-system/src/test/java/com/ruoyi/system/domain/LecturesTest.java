package com.ruoyi.system.domain;

import org.junit.jupiter.api.Test;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LecturesTest {

    @Test
    void exposesCoverIdBeanProperty() throws Exception {
        PropertyDescriptor[] descriptors = Introspector.getBeanInfo(Lectures.class).getPropertyDescriptors();

        boolean hasCoverId = Arrays.stream(descriptors)
                .anyMatch(descriptor -> "coverId".equals(descriptor.getName())
                        && descriptor.getReadMethod() != null
                        && descriptor.getWriteMethod() != null);

        assertTrue(hasCoverId, "Lectures should expose readable and writable coverId property");
    }
}
