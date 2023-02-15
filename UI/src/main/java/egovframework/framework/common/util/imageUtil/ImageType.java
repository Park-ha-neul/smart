package egovframework.framework.common.util.imageUtil;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author James
 */
public enum ImageType {
    JPG,
    GIF,
    PNG,
    UNKNOWN;

    private static final Map<String, ImageType> EXTENSION_MAP = new HashMap<String, ImageType>();

    static {
    	EXTENSION_MAP.put("jpg", ImageType.JPG);
    	EXTENSION_MAP.put("jpeg", ImageType.JPG);
    	EXTENSION_MAP.put("gif", ImageType.GIF);
    	EXTENSION_MAP.put("png", ImageType.PNG);
    }

    public static ImageType getType(String ext) {
        ext = ext.toLowerCase();
        if (EXTENSION_MAP.containsKey(ext))
            return EXTENSION_MAP.get(ext);
        else
            return UNKNOWN;
    }
}
