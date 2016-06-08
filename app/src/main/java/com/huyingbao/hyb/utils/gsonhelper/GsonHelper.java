package com.huyingbao.hyb.utils.gsonhelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/8.
 */

public class GsonHelper {
    private static GsonBuilder getGsonBuilder() {
        GsonBuilder gsonb = new GsonBuilder();
        gsonb.registerTypeAdapter(Date.class, new DateDeserializer());
        gsonb.registerTypeAdapter(Date.class, new DateSerializer());
        gsonb.setDateFormat(1);
        return gsonb;
    }

    private static Gson getGson() {
        return getGsonBuilder().create();
    }

    private static Gson getGsonWithAnnotation() {
        return getGsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    public static String toJson(Object src) {
        Gson gson = getGson();
        return gson.toJson(src);
    }

    public static String toJsonWithAnnotation(Object src) {
        Gson gson = getGsonWithAnnotation();
        return gson.toJson(src);
    }

    public static String toJson(Object src, Type type) {
        Gson gson = getGson();
        return gson.toJson(src, type);
    }

    public static String toJsonWithAnnotation(Object src, Type type) {
        Gson gson = getGsonWithAnnotation();
        return gson.toJson(src, type);
    }

    public static <T> T fromJson(Reader json, Type type)
            throws JSONException {
        Gson gson = getGson();
        return (T) gson.fromJson(json, type);
    }

    public static <T> T fromJsonWithAnnotation(Reader json, Type type)
            throws JSONException {
        Gson gson = getGsonWithAnnotation();
        return (T) gson.fromJson(json, type);
    }

    public static <T> T fromJson(String json, Type type)
            throws JSONException {
        Gson gson = getGson();
        return (T) gson.fromJson(json, type);
    }

    public static <T> T fromJsonWithAnnotation(String json, Type type)
            throws JSONException {
        Gson gson = getGsonWithAnnotation();
        return (T) gson.fromJson(json, type);
    }
}

