package com.gestankbratwurst.epro.quests.abstraction;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public interface QuestReward extends Consumer<Player> {

  List<String> getDescription();

}
