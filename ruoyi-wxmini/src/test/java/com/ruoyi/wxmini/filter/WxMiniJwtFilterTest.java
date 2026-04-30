package com.ruoyi.wxmini.filter;

import com.ruoyi.wxmini.config.WxMiniAnonymousUrlProvider;
import com.ruoyi.wxmini.service.IWxMiniJwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WxMiniJwtFilterTest {

    @Mock
    private IWxMiniJwtService jwtService;

    @Mock
    private WxMiniAnonymousUrlProvider anonymousUrlProvider;

    @InjectMocks
    private WxMiniJwtFilter filter;

    @Test
    void shouldAllowAnonymousTutoringTutorListWithoutToken() throws Exception {
        when(anonymousUrlProvider.matches("GET", "/wxmini/tutoring/tutors/list")).thenReturn(true);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/wxmini/tutoring/tutors/list");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
        assertEquals("", response.getContentAsString());
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldRejectDeleteWithoutTokenWhenOnlyGetRouteIsAnonymous() throws Exception {
        when(anonymousUrlProvider.matches("DELETE", "/wxmini/tutoring/parents/1")).thenReturn(false);
        MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/wxmini/tutoring/parents/1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
        assertEquals("Missing or invalid token", response.getContentAsString());
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldAllowPortalPostWithoutToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/wxmini/portal/demo-appid");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
        assertEquals("", response.getContentAsString());
        verifyNoInteractions(jwtService);
    }
}
