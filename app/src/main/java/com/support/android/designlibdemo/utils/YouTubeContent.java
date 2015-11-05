package com.support.android.designlibdemo.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Helper class for providing mock data to the app.
 * In a real world scenario you would either hard code the video ID's in the strings file or
 * retrieve them from a web service.
 */
public class YouTubeContent {

    /**
     * An array of YouTube videos
     */
    public static List<YouTubeVideo> ITEMS = new ArrayList<>();

    /**
     * A map of YouTube videos, by ID.
     */
    public static Map<String, YouTubeVideo> ITEM_MAP = new HashMap<>();

    static {
        addItem(new YouTubeVideo("c7Pcfz214qc", "Open in the YouTube App"));
        addItem(new YouTubeVideo("8umhb-h9spY", "Open in the YouTube App"));
        addItem(new YouTubeVideo("C3i2Kes-zRk", "Open in the YouTube App"));
        addItem(new YouTubeVideo("GtGokeer-b8", "Open in the YouTube App"));
        addItem(new YouTubeVideo("EtRy5PbD4JY", "Open in the YouTube App"));

    }

    private static void addItem(final YouTubeVideo item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A POJO representing a YouTube video
     */
    public static class YouTubeVideo {
        public String id;
        public String title;

        public YouTubeVideo(String id, String content) {
            this.id = id;
            this.title = content;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}