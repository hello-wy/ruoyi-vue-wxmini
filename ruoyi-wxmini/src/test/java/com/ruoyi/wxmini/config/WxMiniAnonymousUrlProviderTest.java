package com.ruoyi.wxmini.config;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WxMiniAnonymousUrlProviderTest {

    @Test
    void shouldMatchByMethodAndPath() {
        WxMiniAnonymousUrlProvider provider = new WxMiniAnonymousUrlProvider();
        provider.getUrls().add(new WxMiniAnonymousUrlProvider.AnonymousUrlPattern("GET", "/wxmini/tutoring/parents/*"));

        assertTrue(provider.matches("GET", "/wxmini/tutoring/parents/1"));
        assertFalse(provider.matches("DELETE", "/wxmini/tutoring/parents/1"));
    }
}
