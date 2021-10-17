package com.noahvogt.snailmail.data;

import java.lang.reflect.Type;
import com.google.gson.*;
public class BooleanTypeAdapter implements JsonDeserializer<Boolean> {
    public Boolean deserialize(JsonElement json, Type typeOfT,
                               JsonDeserializationContext context) throws JsonParseException {
        if (((JsonPrimitive) json).isBoolean()) {
            return json.getAsBoolean();
        }
        if (((JsonPrimitive) json).isString()) {
            String jsonValue = json.getAsString();
            if (jsonValue.equalsIgnoreCase("true")) {
                return true;
            } else if (jsonValue.equalsIgnoreCase("false")) {
                return false;
            } else {
                return null;
            }
        }

        int code = json.getAsInt();
        return code == 0 ? false :
                code == 1 ? true : null;
    }
}