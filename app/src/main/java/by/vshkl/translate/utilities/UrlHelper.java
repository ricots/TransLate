package by.vshkl.translate.utilities;

public class UrlHelper {

    public static String extractStopId(String url) {
        return url.substring(url.lastIndexOf("s") + 2, url.length());
    }
}
