package com.gestankbratwurst.epro.moderation;

import com.gestankbratwurst.epro.tasks.TaskManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserStatusRequest {

  private static final Gson gson = new GsonBuilder().create();

  public static UserStatus get(UUID userId) {
    try {
      String endpoint = "https://backend.sidusgames.eu/api/v1/epro/checkUUID?uuid=%uuid%&token=LvHwqPWIvoazbYIDjxzC%23G1Ejh9olA6IvI9PUuTPr";
      String url = endpoint.replace("%uuid%", userId.toString().replace("-", ""));
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(userId + " > " + response.body());
      JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);

      return UserStatus.fromId(Integer.parseInt(jsonObject.get("status").getAsString()));
    } catch (Exception e) {
      e.printStackTrace();
      return UserStatus.ERROR;
    }
  }

  public static CompletableFuture<UserStatus> getAsync(UUID userId) {
    return TaskManager.supplyOnIOPool(() -> get(userId));
  }

}
