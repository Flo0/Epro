package com.gestankbratwurst.epro.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class BackedMap<K, V, B> implements Map<K, V> {

  private final B backbone;
  private final Map<K, V> localMap;

  public BackedMap(B backbone, Map<K, V> localMap) {
    this.backbone = backbone;
    this.localMap = localMap;
  }

  protected abstract int remoteSize(B backbone);

  public int remoteSize() {
    return remoteSize(this.backbone);
  }

  protected abstract boolean remoteIsEmpty(B backbone);

  public boolean remoteIsEmpty() {
    return remoteIsEmpty(this.backbone);
  }

  protected abstract boolean remoteContainsKey(B backbone, Object key);

  public boolean remoteContainsKey(Object key) {
    return remoteContainsKey(this.backbone, key);
  }

  protected abstract boolean remoteContainsValue(B backbone, Object value);

  public boolean remoteContainsValue(Object value) {
    return remoteContainsValue(this.backbone, value);
  }

  protected abstract V remoteGet(B backbone, Object key);

  public V remoteGet(Object key) {
    return remoteGet(this.backbone, key);
  }

  protected abstract V remotePut(B backbone, K key, V value);

  public V remotePut(K key, V value) {
    return remotePut(this.backbone, key, value);
  }

  protected abstract V remoteRemove(B backbone, Object key);

  public V remoteRemove(Object key) {
    return remoteRemove(this.backbone, key);
  }

  protected abstract void remotePutAll(B backbone, Map<? extends K, ? extends V> other);

  public void remotePutAll(Map<? extends K, ? extends V> other) {
    remotePutAll(this.backbone, other);
  }

  protected abstract void remoteClear(B backbone);

  public void remoteClear() {
    remoteClear(this.backbone);
  }

  protected abstract Set<K> remoteKeySet(B backbone);

  public Set<K> remoteKeySet() {
    return remoteKeySet(this.backbone);
  }

  protected abstract Collection<V> remoteValues(B backbone);

  public Collection<V> remoteValues() {
    return remoteValues(this.backbone);
  }

  protected abstract Set<Entry<K, V>> remoteEntrySet(B backbone);

  public Set<Entry<K, V>> remoteEntrySet() {
    return remoteEntrySet(this.backbone);
  }

  protected abstract void syncLocalToRemote(B remote, Map<K, V> local);

  public void syncLocalToRemote() {
    syncLocalToRemote(this.backbone, this.localMap);
  }

  protected abstract void syncRemoteToLocal(B remote, Map<K, V> local);

  public void syncRemoteToLocal() {
    syncRemoteToLocal(this.backbone, this.localMap);
  }

  protected abstract void transferFromLocalToRemote(B remote, Map<K, V> local, K key, boolean remove);

  public void transferFromLocalToRemote(K key, boolean remove) {
    transferFromLocalToRemote(this.backbone, this.localMap, key, remove);
  }

  public void transferFromLocalToRemote(K key) {
    this.transferFromLocalToRemote(key, true);
  }

  protected abstract void transferFromRemoteToLocal(B remote, Map<K, V> local, K key, boolean remove);

  public void transferFromRemoteToLocal(K key, boolean remove) {
    transferFromLocalToRemote(this.backbone, this.localMap, key, remove);
  }

  public void transferFromRemoteToLocal(K key) {
    this.transferFromRemoteToLocal(key, true);
  }

  @Override
  public int size() {
    return localMap.size();
  }

  @Override
  public boolean isEmpty() {
    return localMap.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return localMap.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return localMap.containsValue(value);
  }

  @Override
  public V get(Object key) {
    return localMap.get(key);
  }

  @Nullable
  @Override
  public V put(K key, V value) {
    return localMap.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return localMap.remove(key);
  }

  @Override
  public void putAll(@NotNull Map<? extends K, ? extends V> m) {
    localMap.putAll(m);
  }

  @Override
  public void clear() {
    localMap.clear();
  }

  @NotNull
  @Override
  public Set<K> keySet() {
    return localMap.keySet();
  }

  @NotNull
  @Override
  public Collection<V> values() {
    return localMap.values();
  }

  @NotNull
  @Override
  public Set<Entry<K, V>> entrySet() {
    return localMap.entrySet();
  }

}
