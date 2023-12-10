package com.gestankbratwurst.epro.playerdata;

import com.gestankbratwurst.epro.EproCore;
import com.gestankbratwurst.epro.EproMinecraftGame;
import com.gestankbratwurst.epro.collections.BackedMap;
import com.gestankbratwurst.epro.collections.JsonBackedMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.util.UUID;

public class EproPlayerManager implements Flushable {

  private final BackedMap<UUID, EproPlayer, File> playerMap;

  public EproPlayerManager() {
    File file = new File(JavaPlugin.getPlugin(EproMinecraftGame.class).getDataFolder(), "playerdata.json");
    this.playerMap = new JsonBackedMap<>(file, EproCore.getSerializer());

  }

  public void loadAllPlayers() {
    this.playerMap.syncLocalToRemote();
  }

  public void saveAllPlayers() {
    this.playerMap.syncRemoteToLocal();
  }

  public void loadPlayerData(UUID playerId) {
    if (!this.playerMap.containsKey(playerId)) {
      this.playerMap.put(playerId, new EproPlayer(playerId));
    }
  }

  public EproPlayer getPlayerData(UUID uniqueId) {
    return this.playerMap.get(uniqueId);
  }

  @Override
  public void flush() throws IOException {
    saveAllPlayers();
  }
}
