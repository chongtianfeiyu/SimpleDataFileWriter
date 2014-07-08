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
   * Canonical name of this data field, this name will be written to output
   */
  String name();

  /**
   * Relative position of this data field inside a POJO, fields with smaller positions appears before those with larger
   * positions
   */
  int position() default -1;
}
