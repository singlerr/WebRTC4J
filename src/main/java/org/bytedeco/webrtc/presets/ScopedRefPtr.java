package org.bytedeco.webrtc.presets;

import org.bytedeco.javacpp.annotation.Adapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Adapter("ScopedRefPtrAdapter")
public @interface ScopedRefPtr {

    String value() default "";
}