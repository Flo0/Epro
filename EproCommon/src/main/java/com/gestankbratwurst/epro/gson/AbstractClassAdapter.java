package com.gestankbratwurst.epro.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Type;

@RequiredArgsConstructor
public class AbstractClassAdapter implements JsonSerializer<Object>, JsonDeserializer<Object> {

  private static final String CLASS_KEY = "@CLASS";
  private static final String DATA_KEY = "@DATA";

  private final GsonSerializer serializer;

  @Override
  public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    try {
      Class<?> tClass = Class.forName(jsonObject.get(CLASS_KEY).getAsString());
      JsonElement data = jsonObject.get(DATA_KEY);
      return serializer.getGsonWithout(this).fromJson(data, tClass);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(CLASS_KEY, o.getClass().getName());
    jsonObject.addProperty(DATA_KEY, serializer.getGsonWithout(this).toJson(o));
    return jsonObject;
  }
}
