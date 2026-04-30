package com.ruoyi.wxmini.config;

import com.ruoyi.common.annotation.Anonymous;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Configuration
public class WxMiniAnonymousUrlProvider implements InitializingBean, ApplicationContextAware {

    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("\\{(.*?)\\}");

    private final List<AnonymousUrlPattern> urls = new ArrayList<>();

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        handlerMethods.forEach((info, handlerMethod) -> {
            if (!isAnonymous(handlerMethod)) {
                return;
            }

            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
            Objects.requireNonNull(info.getPatternsCondition().getPatterns()).stream()
                    .filter(url -> url.startsWith("/wxmini"))
                    .map(url -> RegExUtils.replaceAll(url, PATH_VARIABLE_PATTERN, "*"))
                    .forEach(url -> addAnonymousUrlPatterns(url, methods));
        });
    }

    public List<AnonymousUrlPattern> getUrls() {
        return urls;
    }

    public boolean matches(String method, String path) {
        return urls.stream().anyMatch(url -> url.matches(method, path));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private boolean isAnonymous(HandlerMethod handlerMethod) {
        Anonymous method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Anonymous.class);
        if (method != null) {
            return true;
        }
        return Optional.ofNullable(AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Anonymous.class)).isPresent();
    }

    private void addAnonymousUrlPatterns(String url, Set<RequestMethod> methods) {
        if (methods == null || methods.isEmpty()) {
            addAnonymousUrlPattern("GET", url);
            return;
        }
        methods.forEach(method -> addAnonymousUrlPattern(method.name(), url));
    }

    private void addAnonymousUrlPattern(String method, String url) {
        AnonymousUrlPattern pattern = new AnonymousUrlPattern(method, url);
        if (!urls.contains(pattern)) {
            urls.add(pattern);
        }
    }

    public static class AnonymousUrlPattern {
        private final String method;
        private final String url;

        public AnonymousUrlPattern(String method, String url) {
            this.method = method;
            this.url = url;
        }

        public boolean matches(String requestMethod, String requestPath) {
            return StringUtils.equalsIgnoreCase(method, requestMethod)
                    && new org.springframework.util.AntPathMatcher().match(url, requestPath);
        }

        public String getMethod() {
            return method;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AnonymousUrlPattern)) {
                return false;
            }
            AnonymousUrlPattern that = (AnonymousUrlPattern) o;
            return Objects.equals(method, that.method) && Objects.equals(url, that.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(method, url);
        }
    }
}
