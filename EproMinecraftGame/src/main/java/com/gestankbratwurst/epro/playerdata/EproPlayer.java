package com.gestankbratwurst.epro.playerdata;

import com.gestankbratwurst.epro.EproMinecraftGame;
import com.gestankbratwurst.epro.quests.Quest;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EproPlayer {

  public static EproPlayer of(Player player) {
    return EproMinecraftGame.getPlayerManager().getPlayerData(player.getUniqueId());
  }

  @Getter
  private final UUID playerId;

  private final List<Quest> activeQuests;

  public EproPlayer(UUID playerId) {
    this.playerId = playerId;
    this.activeQuests = new ArrayList<>();
  }

  public EproPlayer() {
    this(null);
  }

  public void addQuest(Quest quest) {
    this.activeQuests.add(quest);
  }

  public void removeQuest(Quest quest) {
    this.activeQuests.remove(quest);
  }

  public List<Quest> getActiveQuests() {
    return List.copyOf(this.activeQuests);
  }

}
