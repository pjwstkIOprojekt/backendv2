package com.gary.backendv2.utils.annotations.impl;

import com.gary.backendv2.utils.annotations.ContainsKeys;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContainsKeysValidator implements ConstraintValidator<ContainsKeys, Map<? extends String, ?>> {

    private String[] allowedKeys;

    @Override
    public void initialize(ContainsKeys constraintAnnotation) {
        allowedKeys = constraintAnnotation.allowedKeys();
    }

    @Override
    public boolean isValid(Map<? extends String, ?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value.isEmpty()) {
            return true;
        }

        Set<? extends String> mapKeys = value.keySet(); // sub set
        Set<String> allowed = new HashSet<>(List.of(allowedKeys)); //superset

        return allowed.containsAll(mapKeys); // check if mapkeys is a subset of allowed keys set
    }
}
