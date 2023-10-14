package com.leopold.modules.security.tokenExtractor;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TokenExtractorImpl implements TokenExtractor {
    private List<ExtractTokenStrategy> strategies;
    private final ListableBeanFactory beanFactory;

    @Autowired
    public TokenExtractorImpl(ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    private void SetStrategies() {
        Map<String, ExtractTokenStrategy> extractTokenStrategyBeans = beanFactory.getBeansOfType(ExtractTokenStrategy.class);
        strategies = new ArrayList<>(extractTokenStrategyBeans.values());

    }
    @Override
    public String extract(HttpServletRequest req) {
        for (ExtractTokenStrategy s : strategies) {
            String token = s.extractToken(req);
            if (token != null) return token;
        }
        return null;
    }
}
