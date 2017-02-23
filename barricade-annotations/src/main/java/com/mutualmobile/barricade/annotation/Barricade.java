package com.mutualmobile.barricade.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Annotation to configure Barricade for an endpoint.
 *
 * @author phaniraja.bhandari, 7/25/16.
 */
@Documented @Retention(CLASS) @Target(ElementType.METHOD) public @interface Barricade {
  String endpoint();

  ResponseType[] options();
}