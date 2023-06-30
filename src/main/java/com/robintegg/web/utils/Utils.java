package com.robintegg.web.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Utils {
    public static String relativeUrl(String url) {
        return url;
    }

    public static String escape(String title) {
        return title;
    }

    public static String absoluteUrl(String path) {
        return path;
    }

    public static String format(LocalDate date) {
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
    }

    public static String capitalize(String category) {
        return category.toUpperCase();
    }
}
