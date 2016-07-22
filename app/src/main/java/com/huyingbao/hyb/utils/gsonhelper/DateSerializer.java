package com.huyingbao.hyb.utils.gsonhelper;

/**
 * Created by Administrator on 2016/6/8.
 */

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

class DateSerializer
        implements JsonSerializer<Date>
{
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context)
    {
        return new JsonPrimitive(Long.valueOf(src.getTime()));
    }
}
