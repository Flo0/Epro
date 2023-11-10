package com.gestankbratwurst.epro;

import com.gestankbratwurst.epro.bridge.RemoteEventManager;
import com.gestankbratwurst.epro.model.DataDomainManager;
import com.google.common.base.Preconditions;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Getter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class EproGateway {

  public static final String DEFAULT_EVENT_CHANNEL = "epro-default-message-event-channel";

  private static DataDomainManager dataDomainManager;
  private static MongoClient mongoClient;
  private static RedissonClient redissonClient;
  @Getter
  private static boolean redissonInitialized = false;
  @Getter
  private static boolean mongodbInitialized = false;
  private static RemoteEventManager remoteEventManager;

  public static DataDomainManager getDataDomainManager() {
    if (dataDomainManager == null) {
      Preconditions.checkState(redissonInitialized && mongodbInitialized, "Redisson and MongoDB need to be initialized.");
      dataDomainManager = new DataDomainManager(getRemoteEventManager());
    }
    return dataDomainManager;
  }

  public static RemoteEventManager getRemoteEventManager() {
    if (remoteEventManager == null) {
      Preconditions.checkState(redissonInitialized, "Redisson needs to be initialize for remote messages to work.");
      remoteEventManager = new RemoteEventManager(DEFAULT_EVENT_CHANNEL, redissonClient);
    }
    return remoteEventManager;
  }

  public static RedissonClient getRedissonClient() {
    Preconditions.checkState(redissonInitialized, "Redisson is not initialized!");
    return redissonClient;
  }

  public static MongoClient getMongoClient() {
    Preconditions.checkState(mongodbInitialized, "MongoDB is not initialized!");
    return mongoClient;
  }

  public static void initializeRedis(Config redissonConfig) {
    Preconditions.checkState(!redissonInitialized);
    redissonInitialized = true;
    redissonClient = Redisson.create(redissonConfig);
  }

  public static void initializeMongodb(MongoClientSettings mongoClientSettings) {
    Preconditions.checkState(!mongodbInitialized);
    mongoClient = MongoClients.create(mongoClientSettings);
    mongodbInitialized = true;
  }

}
