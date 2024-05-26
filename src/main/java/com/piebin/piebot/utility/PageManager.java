package com.piebin.piebot.utility;

public class PageManager {
    public static int getPages(int total, int size) {
        return ((total - 1) / size) + 1;
    }

    public static boolean isInPage(int pages, int page) {
        return (0 < page && page <= pages);
    }

    public static boolean isInPage(int total, int size, int page) {
        return isInPage(getPages(total, size), page);
    }

    public static boolean isInPage(int pages, String page) {
        try {
            return isInPage(pages, Integer.parseInt(page));
        } catch (Exception e) {
            return false;
        }
    }

    public static int getPage(int total, int size, int page) {
        int pages = getPages(total, size);
        if (page > pages)
            return pages;
        return isInPage(total, size, page) ? page : 1;
    }

    public static int getPage(int total, int size, String page) {
        try {
            return getPage(total, size, Integer.parseInt(page));
        } catch (Exception e) {
            return 1;
        }
    }

    public static int getPage(int pages, int page) {
        if (page > pages)
            return pages;
        return isInPage(pages, page) ? page : 1;
    }

    public static int getPage(int pages, String page) {
        try {
            return getPage(pages, Integer.parseInt(page));
        } catch (Exception e) {
            return 1;
        }
    }
}
