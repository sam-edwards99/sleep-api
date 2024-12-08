package com.noom.interview.fullstack.sleep.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

public class SleepDateValidator implements ConstraintValidator<SleepDateConstraint, Date> {
    @Override
    public boolean isValid(Date sleepDate, ConstraintValidatorContext context) {
        // Can't allow users to Log Sleep Sessions in the future
        return !sleepDate.after(new Date());
    }
}