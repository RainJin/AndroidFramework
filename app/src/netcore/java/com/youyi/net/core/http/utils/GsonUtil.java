package com.youyi.net.core.http.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.io.Reader;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

/**
 * Gson数据解析工具
 * Created by Rain on 2016/2/16.
 */
public class GsonUtil {
    private static final String TAG = GsonUtil.class.getName();

    private static final Gson GSON = createGson(true);
    private static final Gson GSON_NO_NULLS = createGson(false);

    /**
     * Create the standard {@link com.google.gson.Gson} configuration
     *
     * @return created gson, never null
     */
    public static final Gson createGson() {
        return createGson(true);
    }

    /**
     * Create the standard {@link com.google.gson.Gson} configurationØ
     *
     * @param serializeNulls whether nulls should be serialized
     * @return created gson, never null
     */
    public static final Gson createGson(final boolean serializeNulls) {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateFormatter());
        builder.setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES);
        if (serializeNulls) {
            builder.serializeNulls();
        }
        return builder.create();
    }

    /**
     * Get reusable pre-configured {@link com.google.gson.Gson} instance
     *
     * @return Gson instance
     */
    public static final Gson getGson() {
        return GSON;
    }

    /**
     * Get reusable pre-configured {@link com.google.gson.Gson} instance
     *
     * @return Gson instance
     */
    public static final Gson getGson(final boolean serializeNulls) {
        return serializeNulls ? GSON : GSON_NO_NULLS;
    }

    /**
     * Convert object to json
     *
     * @return json string
     */
    public static final String toJson(final Object object) {
        return toJson(object, true);
    }

    /**
     * Convert object to json
     *
     * @return json string
     */
    public static final String toJson(final Object object, final boolean includeNulls) {
        return includeNulls ? GSON.toJson(object) : GSON_NO_NULLS.toJson(object);
    }

    /**
     * Convert string to given type
     *
     * @return instance of type
     */
    public static final <V> V fromJson(String json, Class<V> type) {
        try {
            return GSON.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * Convert string to given type
     *
     * @return instance of type
     */
    public static final <V> V fromJson(String json, Type type) {
        try {
            return GSON.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * Convert content of reader to given type
     *
     * @return instance of type
     */
    public static final <V> V fromJson(Reader reader, Class<V> type) {
        try {
            return GSON.fromJson(reader, type);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * Convert content of reader to given type
     *
     * @return instance of type
     */
    public static final <V> V fromJson(Reader reader, Type type) {
        try {
            return GSON.fromJson(reader, type);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * 日期格式化
     */
    public static class DateFormatter implements JsonDeserializer<Date>, JsonSerializer<Date> {

        private final DateFormat[] formats;

        /**
         * Create date formatter
         */
        public DateFormatter() {
            formats = new DateFormat[1];
            formats[0] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final TimeZone timeZone = TimeZone.getTimeZone("Zulu"); //$NON-NLS-1$
            for (DateFormat format : formats) {
                format.setTimeZone(timeZone);
            }
        }

        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            // JsonParseException exception = null;
            final String value = json.getAsString();
            if (StringUtil.isEmpty(value) || value.length() == 1) return null;
            for (DateFormat format : formats) {
                try {
                    synchronized (format) {
                        return format.parse(value);
                    }
                } catch (ParseException e) {
                    LogUtil.e(TAG, "日期转换错误， " + value, e);
                    // exception = new JsonParseException(e);
                }
            }
            // 会使程序崩溃了
            // throw exception;
            return new Date(0);
        }

        public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
            final DateFormat primary = formats[0];
            String formatted;
            synchronized (primary) {
                formatted = primary.format(date);
            }
            return new JsonPrimitive(formatted);
        }
    }

}
