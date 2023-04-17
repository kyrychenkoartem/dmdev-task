package com.artem.validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.util.CollectionUtils;

public class EnumPropertyValidator implements ConstraintValidator<EnumProperty, String> {

    private List<String> acceptedValues;

    @Override
    public void initialize(EnumProperty annotation) {
        this.acceptedValues = Arrays.stream(annotation.clazz().getEnumConstants())
                .map(Enum::name).collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        HibernateConstraintValidatorContext constraintContext = context.unwrap(HibernateConstraintValidatorContext.class);
        constraintContext.disableDefaultConstraintViolation();
        constraintContext.addMessageParameter("acceptedValues", acceptedValues)
                .buildConstraintViolationWithTemplate(constraintContext.getDefaultConstraintMessageTemplate())
                .addConstraintViolation();
        return CollectionUtils.contains(acceptedValues.iterator(), value);
    }
}
