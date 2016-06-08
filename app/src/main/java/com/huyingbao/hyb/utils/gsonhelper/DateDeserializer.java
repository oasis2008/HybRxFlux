package com.huyingbao.hyb.utils.gsonhelper;

/**
 * Created by Administrator on 2016/6/8.
 */

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

class DateDeserializer
        implements JsonDeserializer<Date> {
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return new Date(json.getAsJsonPrimitive().getAsLong());
    }
}