package com.gestankbratwurst.epro.epro;

import com.gestankbratwurst.epro.EproGateway;
import com.gestankbratwurst.epro.model.AutoSynchronizedGlobalDataMap;
import com.gestankbratwurst.epro.model.DataDomainManager;
import com.gestankbratwurst.epro.model.DataMapContext;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.redisson.api.RedissonClient;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class EproNetworkUserManager {

  public static final String EPRO_NETWORK_USER_NAMESPACE = "epro-network-user-data";

  private final AutoSynchronizedGlobalDataMap<UUID, EproNetworkUser> userDataMap;

  public EproNetworkUserManager(RedissonClient redissonClient, MongoClient mongoClient) {
    MongoDatabase mongoDatabase = mongoClient.getDatabase(EproGateway.EPRO_DATABASE_NAME);
    DataMapContext<UUID, EproNetworkUser> dataMapContext = DataMapContext.<UUID, EproNetworkUser>mapContextBuilder()
            .creator(EproNetworkUser::new)
            .redissonClient(redissonClient)
            .keyClass(UUID.class)
            .mongoDatabase(mongoDatabase)
            .namespace(EPRO_NETWORK_USER_NAMESPACE)
            .serializer(new EproNetworkGsonSerializer())
            .valueClass(EproNetworkUser.class)
            .build();

    DataDomainManager domainManager = EproGateway.getDataDomainManager();
    this.userDataMap = domainManager.getOrCreateAutoSyncDataDomain(dataMapContext);
  }

  public CompletableFuture<Void> cache(UUID userId) {
    return userDataMap.enableLocalCacheAsyncFor(userId);
  }

  public void uncache(UUID userId) {
    userDataMap.disableLocalCacheFor(userId);
  }

  public EproNetworkUser getLocalCopy(UUID userId) {
    return userDataMap.getCachedValue(userId);
  }

  public void applyToRemoteUser(UUID userId, Consumer<EproNetworkUser> consumer) {
    userDataMap.applyToData(userId, consumer);
  }

}