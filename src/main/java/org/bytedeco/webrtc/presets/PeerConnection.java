package org.bytedeco.webrtc.presets;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(
        value = @Platform(include = "api/peer_connection_interface.h", link = "webrtc.lib", includepath = ""),
        target = "org.bytedeco.webrtc",
        global = "org.bytedeco.webrtc.global.webrtc"
)
@NoOffset
public class PeerConnection implements InfoMapper {

    static {
        Loader.checkVersion("org.bytedeco", "webrtc");
    }

    @Override
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("RTC_EXPORT").cppTypes().annotations());
        infoMap.put(new Info("rtc::scoped_refptr").skip().annotations("@ScopedRefPtr"));
    }
}
