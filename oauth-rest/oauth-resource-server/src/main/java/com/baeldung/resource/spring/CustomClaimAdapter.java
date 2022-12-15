package com.baeldung.resource.spring;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;

import java.util.Collections;
import java.util.Map;

public class CustomClaimAdapter implements
        Converter<Map<String, Object>, Map<String, Object>> {

    private final MappedJwtClaimSetConverter delegate =
            MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

    public Map<String, Object> convert(Map<String, Object> claims) {
        Map<String, Object> convertedClaims = this.delegate.convert(claims);

        String preferred_username = convertedClaims.get("preferred_username") != null && convertedClaims.get("preferred_username").toString().endsWith("@test.com")?
                (String) convertedClaims.get("preferred_username") : "unknown user";

        convertedClaims.put("preferred_username", preferred_username);

        return convertedClaims;
    }
}
