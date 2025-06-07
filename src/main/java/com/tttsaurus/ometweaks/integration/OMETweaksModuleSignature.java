package com.tttsaurus.ometweaks.integration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OMETweaksModuleSignature
{
    // module name
    String value() default "Unspecified Module Name";
}
