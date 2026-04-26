package com.ruoyi.wxmini.config;

import com.ruoyi.common.annotation.Anonymous;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Configuration
public class WxMiniAnonymousUrlProvider implements InitializingBean, ApplicationContextAware {

    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("\\{(.*?)\\}");

    private final List<String> urls = new ArrayList<>();

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        handlerMethods.forEach((info, handlerMethod) -> {
            if (!isAnonymous(handlerMethod)) {
                return;
            }

            Objects.requireNonNull(info.getPatternsCondition().getPatterns()).stream()
                    .filter(url -> url.startsWith("/wxmini"))
                    .map(url -> RegExUtils.replaceAll(url, PATH_VARIABLE_PATTERN, "*"))
                    .forEach(url -> {
                        if (!urls.contains(url)) {
                            urls.add(url);
                        }
                    });
        });
    }

    public List<String> getUrls() {
        return urls;
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
}
