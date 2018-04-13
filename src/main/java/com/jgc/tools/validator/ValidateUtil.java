package com.jgc.tools.validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;


public class ValidateUtil {

    private static Validator validator;

    private static Validator getValidator() {
        if (validator == null) {
            ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
            validator = validatorFactory.getValidator();
        }
        return validator;
    }

    public static <T> void validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> set = getValidator().validate(object, groups);
        for (ConstraintViolation<T> constraintViolation : set) {
          //  throw new RuntimeException(constraintViolation.getMessage(), new Object[]{FieldsUtils.translate(constraintViolation.getPropertyPath().toString())});
        }
    }

    public static <T> boolean isValidate(T object, Class<?>... groups) {
        return getValidator().validate(object, groups).isEmpty();
    }
}
