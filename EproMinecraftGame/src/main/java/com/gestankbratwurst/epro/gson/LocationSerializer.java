package com.gestankbratwurst.epro.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Type;

public class LocationSerializer implements JsonSerializer<Location>, JsonDeserializer<Location> {
  @Override
  public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    String worldName = jsonObject.get("world").getAsString();
    double x = jsonObject.get("x").getAsDouble();
    double y = jsonObject.get("y").getAsDouble();
    double z = jsonObject.get("z").getAsDouble();
    float yaw = jsonObject.get("yaw").getAsFloat();
    float pitch = jsonObject.get("pitch").getAsFloat();
    return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
  }

  @Override
  public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("world", src.getWorld().getName());
    jsonObject.addProperty("x", src.getX());
    jsonObject.addProperty("y", src.getY());
    jsonObject.addProperty("z", src.getZ());
    jsonObject.addProperty("yaw", src.getYaw());
    jsonObject.addProperty("pitch", src.getPitch());
    return jsonObject;
  }
}
