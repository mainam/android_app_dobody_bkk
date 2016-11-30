package com.dobody.bkk.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by MaiNam on 5/25/2016.
 */
public class ConvertUtils {
    private static final String TAG = ConvertUtils.class.getSimpleName();

    static JsonParser jsonParser;

    public static JsonParser getJsonParser() {
        if (jsonParser == null)
            jsonParser = new JsonParser();
        return jsonParser;
    }

    static Gson gson;

    public static Gson getGson() {
        if (gson == null)
            gson = new Gson();
        return gson;
    }


    public static int toInt(Object value, int defaultValue) {
        try {
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static int toInt(Object value) {
        return toInt(value, 0);
    }

    public static int toInt(JsonElement value) {
        return toInt(value, 0);
    }

    public static int toInt(JsonElement value, int defaultValue) {
        try {
            return value.getAsInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static boolean toBoolean(JsonElement value, boolean defaultValue) {
        try {
            return value.getAsBoolean();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return defaultValue;
    }

    public static boolean toBoolean(Object value, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(String.valueOf(value));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return defaultValue;
    }

    public static boolean toBoolean(JsonElement value) {
        return toBoolean(value, false);
    }

    public static float toFloat(Object value, float defaultValue) {
        try {
            return Float.parseFloat(value.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static float toFloat(Object value) {
        return toFloat(value, 0);
    }

    public static float toFloat(JsonElement value) {
        return toFloat(value, 0);
    }

    public static float toFloat(JsonElement value, float defaultValue) {
        try {
            return value.getAsFloat();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static float toFloat(Number value, String format, float defaultValue) {
        try {
            Number number = value;
            String valueParse = value.toString();
            valueParse = valueParse.replace(",", ".");
            DecimalFormat df = new DecimalFormat(format);
            df.setRoundingMode(RoundingMode.HALF_DOWN);
            String temp = df.format(number.floatValue()).replace(",", ".");
            return toFloat(temp, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static float toFloat(Object value, String format, float defaultValue) {
        return ConvertUtils.toFloat(ConvertUtils.toFloat(value, 0), format, defaultValue);

    }

    public static double toDouble(Object value, double defaultValue) {
        try {
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static double toDouble(Object value) {
        return toDouble(value, 0);
    }

    public static double toDouble(JsonElement value) {
        return toDouble(value, 0);
    }

    public static double toDouble(JsonElement value, double defaultValue) {
        try {
            return value.getAsDouble();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }


    public static long toLong(Object value, long defaultValue) {
        try {
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static long toLong(Object value) {
        return toLong(value, 0);
    }

    public static long toLong(JsonElement value) {
        return toLong(value, 0);
    }

    public static long toLong(JsonElement value, long defaultValue) {
        try {
            return value.getAsLong();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static String toString(JsonElement value) {
        return toString(value, "");
    }

    public static String toString(JsonElement value, String defaultValue) {
        try {
            return value.getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;

    }

    public static String toString(Object object, String defaultValue) {
        try {
            return object.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static JsonArray toJsonArray(JsonElement value) {
        return toJsonArray(value, new JsonArray());
    }

    public static JsonArray toJsonArray(JsonElement value, JsonArray defaultValue) {
        try {
            return value.getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static JsonArray toJsonArray(String json) {
        return toJsonArray(json, new JsonArray());
    }

    public static JsonArray toJsonArray(String json, JsonArray defaultValue) {
        try {
            Log.d(TAG, "toJsonArray: " + json);
            return getJsonParser().parse(json).getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }


    public static JSONObject toJsonObject(JsonObject jsonObject) throws JSONException {
        return new JSONObject(jsonObject.toString());
    }

    public static JsonObject toJsonObject(String json) {
        return toJsonObject(json, new JsonObject());
    }

    public static JsonObject toJsonObject(String json, JsonObject defaultValue) {
        try {
            return getJsonParser().parse(json).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static JsonObject toJsonObject(JsonElement json) {
        return toJsonObject(json, new JsonObject());
    }

    public static JsonObject toJsonObject(JsonElement json, JsonObject defaultValue) {
        try {
            return json.getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static String toJson(Object object) {
        try {
            return getGson().toJson(object);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String toJson(JsonElement object) {
        return object.toString();
    }

    public static String toDateString(long milliSeconds, String dateFormat) {
        try {

            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            return formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static ArrayList<JsonObject> toArrayList(JsonArray values) {
        try {
            ArrayList<JsonObject> list = new ArrayList<>();
            for (JsonElement jsonElement : values) {
                list.add(ConvertUtils.toJsonObject(jsonElement));
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static ArrayList<JsonObject> toArrayList(JsonArray... values) {
        try {
            ArrayList<JsonObject> list = new ArrayList<>();
            for (JsonArray jsonArray : values) {
                for (JsonElement jsonElement : jsonArray) {
                    list.add(ConvertUtils.toJsonObject(jsonElement));
                }
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static <T> ArrayList<T> toArrayList(String json) {
        try {
            return getGson().fromJson(json, new TypeToken<ArrayList<T>>() {
            }.getType());

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    public static <T> ArrayList<T> toArrayList(Class<T> c, T... values) {
        try {
            ArrayList<T> list = new ArrayList<>();
            list.addAll(Arrays.asList(values));
            return list;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    public static <T> T[] toArray(Class<T> c, Collection<T> data) {
        try {
            final T[] a = (T[]) Array.newInstance(c, data.size());
            return data.toArray(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
