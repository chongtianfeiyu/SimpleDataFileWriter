package com.lizhaoliu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataField {

  /**
   * Canonical name of this data field
   */
  String name();

  /**
   * Position of this data field inside a DTO
   */
  int position() default -1;
}
