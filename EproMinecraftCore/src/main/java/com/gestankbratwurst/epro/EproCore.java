package com.gestankbratwurst.epro;

import co.aikar.commands.PaperCommandManager;
import com.gestankbratwurst.epro.actionbar.ActionBarManager;
import com.gestankbratwurst.epro.blockdata.BlockDataManager;
import com.gestankbratwurst.epro.gson.GsonSerializer;
import com.gestankbratwurst.epro.gui.GuiListener;
import com.gestankbratwurst.epro.gui.GuiManager;
import com.gestankbratwurst.epro.holograms.HologramManager;
import com.gestankbratwurst.epro.holograms.implementations.protocollib.PlibHologramFactory;
import com.gestankbratwurst.epro.messaging.Msg;
import com.gestankbratwurst.epro.mongodb.MongoDriverProperties;
import com.gestankbratwurst.epro.redis.RedisGsonCodec;
import com.gestankbratwurst.epro.resourcepack.assembly.BlockModel;
import com.gestankbratwurst.epro.resourcepack.assembly.CustomSound;
import com.gestankbratwurst.epro.resourcepack.assembly.TextureModel;
import com.gestankbratwurst.epro.resourcepack.distribution.ResourcepackManager;
import com.gestankbratwurst.epro.skinclient.PlayerSkinManager;
import com.gestankbratwurst.epro.tablist.TabListManager;
import com.gestankbratwurst.epro.tablist.implementation.EmptyTabList;
import com.gestankbratwurst.epro.tokenclick.TokenActionManager;
import com.gestankbratwurst.epro.utils.spigot.UtilChunk;
import com.google.common.base.Preconditions;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.UuidRepresentation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.util.Optional;

public class EproCore extends JavaPlugin {

  public static final String CORE_DB = "epro-core";

  @Getter
  private static EproCore instance;
  @Getter
  private static PaperCommandManager paperCommandManager;
  @Getter
  private static ActionBarManager actionBarManager;
  @Getter
  private static BlockDataManager blockDataManager;
  @Getter
  private static HologramManager hologramManager;
  @Getter
  private static TabListManager tabListManager;
  @Getter
  private static TokenActionManager tokenActionManager;
  @Getter
  private static PlayerSkinManager playerSkinManager;
  @Getter
  private static ResourcepackManager resourcepackManager;
  @Getter
  private static GuiManager guiManager;

  public static MongoDatabase getDatabase() {
    return EproGateway.getMongoClient().getDatabase(CORE_DB);
  }

  public static GsonSerializer getSerializer() {
    return EproCore.getInstance().coreConfigurationService.getSerializer();
  }

  private EproCoreConfigurationService coreConfigurationService;

  @Override
  public void onEnable() {
    instance = this;

    fetchConfigurationService();
    setupGateway();
    Msg.setServerPrefix(coreConfigurationService.getMessagePrefix());

    paperCommandManager = new PaperCommandManager(this);
    actionBarManager = new ActionBarManager(this);
    blockDataManager = new BlockDataManager(this);
    hologramManager = new HologramManager(new PlibHologramFactory());
    tabListManager = new TabListManager(this, player -> new EmptyTabList());
    tokenActionManager = new TokenActionManager(this, paperCommandManager);
    playerSkinManager = new PlayerSkinManager(new GsonSerializer());
    guiManager = new GuiManager();

    Bukkit.getPluginManager().registerEvents(new UtilChunk.ChunkTrackListener(), this);
    Bukkit.getPluginManager().registerEvents(new GuiListener(), this);

    setupResourcepack();
  }

  @Override
  public void onDisable() {
    Optional.ofNullable(blockDataManager).ifPresent(BlockDataManager::terminate);
    if (coreConfigurationService.isResourcepackEnabled() && resourcepackManager != null) {
      resourcepackManager.shutdown();
    }
  }

  private void setupResourcepack() {
    if (coreConfigurationService.isResourcepackEnabled()) {

      coreConfigurationService.getTextureModels().forEach(TextureModel::register);
      coreConfigurationService.getBlockModels().forEach(BlockModel::register);
      coreConfigurationService.getCustomSounds().forEach(CustomSound::register);

      String host = coreConfigurationService.resourcepackServerHost();
      int port = coreConfigurationService.resourcepackServerPort();
      resourcepackManager = new ResourcepackManager(host, port);
      if (!resourcepackManager.zipResourcepack(this, coreConfigurationService.getRawResourcepackFiles())) {
        Bukkit.shutdown();
      }
      resourcepackManager.startServer();
    }
  }

  private void setupGateway() {

    if (coreConfigurationService.isRedisEnabled()) {
      Config redissonConfig = new Config();

      RedisGsonCodec codec = new RedisGsonCodec(coreConfigurationService.getSerializer());
      redissonConfig.setCodec(codec);
      redissonConfig.setNettyThreads(coreConfigurationService.getRedisNettyThreads());

      SingleServerConfig singleServerConfig = redissonConfig.useSingleServer();
      singleServerConfig.setRetryInterval(coreConfigurationService.getRedisRetryInterval());
      singleServerConfig.setTimeout(coreConfigurationService.getRedisTimeout());
      singleServerConfig.setConnectionPoolSize(coreConfigurationService.getRedisConnectionPoolSize());
      singleServerConfig.setAddress(coreConfigurationService.getRedisAddress());

      EproGateway.initializeRedis(redissonConfig);
    }

    MongoDriverProperties properties = coreConfigurationService.getMongoDriverProperties();
    String connectionString = "mongodb://%s:%s@%s:%s".formatted(
        properties.getUser(),
        properties.getPassword(),
        properties.getHostAddress(),
        properties.getHostPort()
    );

    MongoClientSettings mongoSettings = MongoClientSettings.builder()
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .applyConnectionString(new ConnectionString(connectionString))
        .build();

    EproGateway.initializeMongodb(mongoSettings);
  }

  private void fetchConfigurationService() {
    RegisteredServiceProvider<EproCoreConfigurationService> configServiceProvider = Bukkit.getServicesManager().getRegistration(EproCoreConfigurationService.class);
    Preconditions.checkState(configServiceProvider != null, "No configuration service is provided.");
    this.coreConfigurationService = configServiceProvider.getProvider();
  }

}