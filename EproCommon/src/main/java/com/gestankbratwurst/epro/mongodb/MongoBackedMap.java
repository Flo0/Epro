package com.gestankbratwurst.epro.mongodb;

import com.gestankbratwurst.epro.gson.GsonSerializer;
import com.mongodb.client.MongoCollection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MongoBackedMap<K, V> implements Map<K, V> {

  private final MongoMap<K, V> mongoMap;
  private final Map<K, V> localMap;

  public MongoBackedMap(MongoCollection<V> mongoBackbone, GsonSerializer gsonSerializer, Class<K> keyClass, Map<K, V> localMap) {
    this.mongoMap = new MongoMap<>(mongoBackbone, gsonSerializer, keyClass);
    this.localMap = localMap;
  }

  public MongoBackedMap(MongoCollection<V> mongoBackbone, GsonSerializer gsonSerializer, Class<K> keyClass) {
    this(mongoBackbone, gsonSerializer, keyClass, new ConcurrentHashMap<>());
  }

  @Override
  public int size() {
    return localMap.size();
  }

  public int remoteSize() {
    return mongoMap.size();
  }

  @Override
  public boolean isEmpty() {
    return localMap.isEmpty();
  }

  public boolean remoteIsEmpty() {
    return mongoMap.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return localMap.containsKey(key);
  }

  public boolean remoteContainsKey(Object key) {
    return mongoMap.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return localMap.containsValue(value);
  }

  public boolean remoteContainsValue(Object value) {
    return mongoMap.containsValue(value);
  }

  @Override
  public V get(Object key) {
    return localMap.get(key);
  }

  public V remoteGet(Object key) {
    return mongoMap.get(key);
  }

  @Override
  public V put(K key, V value) {
    return localMap.put(key, value);
  }

  public V remotePut(K key, V value) {
    return mongoMap.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return localMap.remove(key);
  }

  public V remoteRemove(Object key) {
    return mongoMap.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> other) {
    localMap.putAll(other);
  }

  public void remotePutAll(Map<? extends K, ? extends V> other) {
    mongoMap.putAll(other);
  }

  @Override
  public void clear() {
    localMap.clear();
  }

  public void remoteClear() {
    mongoMap.clear();
  }

  @Override
  public Set<K> keySet() {
    return localMap.keySet();
  }

  public Set<K> remoteKeySet() {
    return mongoMap.keySet();
  }

  @Override
  public Collection<V> values() {
    return localMap.values();
  }

  public Collection<V> remoteValues() {
    return mongoMap.values();
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return localMap.entrySet();
  }

  public Set<Entry<K, V>> remoteEntrySet() {
    return mongoMap.entrySet();
  }

  public void syncLocalToRemote() {
    localMap.clear();
    localMap.putAll(mongoMap);
  }

  public void syncRemoteToLocal() {
    mongoMap.clear();
    mongoMap.putAll(localMap);
  }

  public void transferFromLocalToRemote(K key) {
    V value = localMap.remove(key);
    if (value != null) {
      mongoMap.put(key, value);
    }
  }

  public void transferFromRemoteToLocal(K key) {
    V value = mongoMap.remove(key);
    if (value != null) {
      localMap.put(key, value);
    }
  }
}
