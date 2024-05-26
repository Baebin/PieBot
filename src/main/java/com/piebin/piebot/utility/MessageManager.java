package com.piebin.piebot.utility;

import net.dv8tion.jda.api.utils.FileUpload;

import java.io.InputStream;
import java.net.URL;

public class MessageManager {
    public static String getMention(String id) {
        return "<@" + id + ">";
    }

    public static FileUpload getProfile(String url) {
        try {
            InputStream in = new URL(url).openStream();
            return FileUpload.fromData(in, "profile.png");
        } catch (Exception e) {
            return null;
        }
    }
}
