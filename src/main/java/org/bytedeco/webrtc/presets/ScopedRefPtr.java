package org.bytedeco.webrtc.presets;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

@Properties(
        value = @Platform(include = {"api/scoped_refptr.h"})
)
@Namespace("rtc")
@Name("scoped_refptr") // rtc::scoped_refptr -> scoped_refptr로 매핑
@NoOffset
public class ScopedRefPtr<T> extends Pointer {
    static {
        Loader.load();
    }

    public ScopedRefPtr() {
        allocate();
    }

    public ScopedRefPtr(T ptr) {
        allocate(ptr);
    }

    private native void allocate();

    private native void allocate(T ptr);

    public native @ByRef T get();
}