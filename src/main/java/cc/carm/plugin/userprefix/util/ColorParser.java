
package cc.carm.plugin.userprefix.util;

public class ColorParser {

    public static String parseColor(final String text) {
        return text.replaceAll("&", "§").replace("§§", "&");
    }

}
