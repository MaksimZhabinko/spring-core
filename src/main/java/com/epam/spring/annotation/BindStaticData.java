package com.epam.spring.annotation;

import com.epam.spring.model.entity.Identifiable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindStaticData {
    String fileLocation();
    Class<? extends Identifiable> castTo();

}
