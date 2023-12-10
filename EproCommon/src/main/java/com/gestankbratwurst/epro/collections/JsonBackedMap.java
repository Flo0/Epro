package com.gestankbratwurst.epro.collections;

import com.gestankbratwurst.epro.gson.GsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class JsonBackedMap<K, V> extends BackedMap<K, V, File> {

  private final GsonSerializer gsonSerializer;
  private final TypeToken<Map<K, V>> typeToken = new TypeToken<>() {
  };

  public JsonBackedMap(File backbone, GsonSerializer gsonSerializer) {
    this(backbone, new ConcurrentHashMap<>(), gsonSerializer);
  }

  public JsonBackedMap(File backbone, Map<K, V> localMap, GsonSerializer gsonSerializer) {
    super(backbone, localMap);
    this.gsonSerializer = gsonSerializer;
  }

  private String readJson(File backbone) {
    if (!backbone.exists()) {
      return "{}";
    }
    try {
      return Files.readString(backbone.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Map<K, V> loadMap(File backbone) {
    return this.gsonSerializer.fromJson(readJson(backbone), this.typeToken.getType());
  }

  private void writeMap(File file, Map<K, V> data) {
    String json = this.gsonSerializer.toJson(data);
    try {
      if (!file.exists()) {
        file.createNewFile();
      }
      Files.writeString(file.toPath(), json);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void applyToRemote(File file, Consumer<Map<K, V>> consumer) {
    Map<K, V> map = loadMap(file);
    consumer.accept(map);
    writeMap(file, map);
  }

  private <R> R functionToRemote(File file, Function<Map<K, V>, R> action) {
    Map<K, V> map = loadMap(file);
    R result = action.apply(map);
    writeMap(file, map);
    return result;
  }

  @Override
  protected int remoteSize(File backbone) {
    return loadMap(backbone).size();
  }

  @Override
  protected boolean remoteIsEmpty(File backbone) {
    return loadMap(backbone).isEmpty();
  }

  @Override
  protected boolean remoteContainsKey(File backbone, Object key) {
    return loadMap(backbone).containsKey(key);
  }

  @Override
  protected boolean remoteContainsValue(File backbone, Object value) {
    return loadMap(backbone).containsValue(value);
  }

  @Override
  protected V remoteGet(File backbone, Object key) {
    return loadMap(backbone).get(key);
  }

  @Override
  protected V remotePut(File backbone, K key, V value) {
    return functionToRemote(backbone, map -> map.put(key, value));
  }

  @Override
  protected V remoteRemove(File backbone, Object key) {
    return functionToRemote(backbone, map -> map.remove(key));
  }

  @Override
  protected void remotePutAll(File backbone, Map<? extends K, ? extends V> other) {
    applyToRemote(backbone, map -> map.putAll(other));
  }

  @Override
  protected void remoteClear(File backbone) {
    applyToRemote(backbone, Map::clear);
  }

  @Override
  protected Set<K> remoteKeySet(File backbone) {
    return loadMap(backbone).keySet();
  }

  @Override
  protected Collection<V> remoteValues(File backbone) {
    return loadMap(backbone).values();
  }

  @Override
  protected Set<Entry<K, V>> remoteEntrySet(File backbone) {
    return loadMap(backbone).entrySet();
  }

  @Override
  protected void syncLocalToRemote(File remote, Map<K, V> local) {
    Map<K, V> remoteMap = loadMap(remote);
    local.clear();
    local.putAll(remoteMap);
  }

  @Override
  protected void syncRemoteToLocal(File remote, Map<K, V> local) {
    writeMap(remote, local);
  }

  @Override
  protected void transferFromLocalToRemote(File remote, Map<K, V> local, K key, boolean remove) {
    V value = remove ? local.remove(key) : local.get(key);
    applyToRemote(remote, map -> map.put(key, value));
  }

  @Override
  protected void transferFromRemoteToLocal(File remote, Map<K, V> local, K key, boolean remove) {
    V value = remove ? remoteRemove(remote, key) : remoteGet(remote, key);
    local.put(key, value);
  }
}
