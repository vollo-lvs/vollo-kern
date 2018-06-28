package nl.vollo.kern.annotation;

import nl.vollo.kern.model.DomainEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DomainType {
    DomainEntity value();
}