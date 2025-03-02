package org.bytedeco.webrtc.presets;

import org.bytedeco.javacpp.annotation.Adapter;
import org.bytedeco.javacpp.annotation.Namespace;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Adapter("ScopedRefPtrAdapter")
public @interface ScopedRefPtr {
    String value() default "";
}
