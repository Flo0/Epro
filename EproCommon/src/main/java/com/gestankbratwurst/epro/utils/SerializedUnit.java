package com.gestankbratwurst.epro.utils;

import com.gestankbratwurst.epro.gson.GsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SerializedUnit<T> {

  private Class<T> elementType;
  private String json;

  public T translate(GsonSerializer serializer) {
    return serializer.fromJson(json, elementType);
  }

}
