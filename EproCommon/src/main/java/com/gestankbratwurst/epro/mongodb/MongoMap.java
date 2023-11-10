package com.gestankbratwurst.epro.mongodb;

import com.gestankbratwurst.epro.gson.GsonSerializer;
import com.google.common.base.Preconditions;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Sorts;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class MongoMap<K, V> implements Map<K, V> {

  private final MongoCollection<V> mongoBackbone;

  public MongoMap(MongoCollection<V> mongoBackbone, GsonSerializer gsonSerializer) {
    Preconditions.checkArgument(mongoBackbone != null && gsonSerializer != null);
    CodecRegistry customCodec = CodecRegistries.fromCodecs(new MongoGsonCodec<>(mongoBackbone.getDocumentClass(), gsonSerializer));
    CodecRegistry codec = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), customCodec);
    this.mongoBackbone = mongoBackbone.withCodecRegistry(codec);
  }

  @Override
  public int size() {
    return (int) mongoBackbone.countDocuments();
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean containsKey(Object key) {
    if (!mongoBackbone.getDocumentClass().isInstance(key)) {
      return false;
    }

    return mongoBackbone.countDocuments(Filters.eq(key)) > 0;
  }

  @Override
  public boolean containsValue(Object value) {
    try (MongoCursor<V> cursor = mongoBackbone.find().iterator()) {
      while (cursor.hasNext()) {
        if (cursor.next().equals(value)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public V get(Object key) {
    if (!mongoBackbone.getDocumentClass().isInstance(key)) {
      return null;
    }

    return mongoBackbone.find(Filters.eq(key)).first();
  }

  @Override
  public V put(K key, V value) {
    ReplaceOptions options = new ReplaceOptions().upsert(true);
    V replaced = get(key);
    mongoBackbone.replaceOne(Filters.eq(key), value, options);
    return replaced;
  }

  public void fastPut(K key, V value) {
    ReplaceOptions options = new ReplaceOptions().upsert(true);
    mongoBackbone.replaceOne(Filters.eq(key), value, options);
  }

  @Override
  public V remove(Object key) {
    V replaced = get(key);
    mongoBackbone.deleteOne(Filters.eq(key));
    return replaced;
  }

  public void fastRemove(Object key) {
    mongoBackbone.deleteOne(Filters.eq(key));
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> map) {
    map.forEach(this::fastPut);
  }

  @Override
  public void clear() {
    mongoBackbone.drop();
  }

  @Override
  public Set<K> keySet() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Collection<V> values() {
    List<V> values = new ArrayList<>();
    mongoBackbone.find().into(values);
    return values;
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    throw new UnsupportedOperationException();
  }

  public <E> List<V> findByProperty(String property, E value) {
    return query(coll -> coll.find(Filters.eq(property, value)), iter -> {
      List<V> values = new ArrayList<>();
      iter.into(values);
      return values;
    });
  }

  public List<V> queryToplist(String property, int limit, boolean ascending) {
    Bson sort = ascending ? Sorts.ascending(property) : Sorts.descending(property);
    return query(MongoCollection::find, iter -> {
      List<V> values = new ArrayList<>();
      iter.sort(sort).limit(limit).into(values);
      return values;
    });
  }

  public <I, R> R query(Function<MongoCollection<V>, I> queryFunction, Function<I, R> resultFunction) {
    return resultFunction.apply(queryFunction.apply(this.mongoBackbone));
  }

}