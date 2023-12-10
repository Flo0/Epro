package com.gestankbratwurst.epro.quests;

import com.gestankbratwurst.epro.quests.abstraction.QuestReward;
import com.gestankbratwurst.epro.quests.abstraction.QuestTarget;
import com.gestankbratwurst.epro.utils.spigot.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quest {

  public static QuestBuilder builder() {
    return new QuestBuilder();
  }

  private final String name;
  private final ItemStack displayItem;
  private final List<QuestReward> rewards;
  private final Map<Class<? extends QuestTarget>, List<QuestTarget>> targets;

  public Quest(String name, ItemStack displayItem) {
    this.name = name;
    this.displayItem = displayItem;
    this.rewards = new ArrayList<>();
    this.targets = new HashMap<>();
  }

  public void addReward(final QuestReward reward) {
    this.rewards.add(reward);
  }

  public void giveRewardsToPlayer(Player player) {
    rewards.forEach(reward -> reward.accept(player));
  }

  public <T extends QuestTarget> void addTarget(T target) {
    targets.computeIfAbsent(target.getClass(), clazz -> new ArrayList<>()).add(target);
  }

  public ItemStack getInfoIcon() {
    ItemBuilder builder = ItemBuilder.of(displayItem.clone());

    builder.name("§6" + this.name);

    builder.lore("").lore("§eZiele:");

    targets.values().stream().flatMap(List::stream).forEach(target -> target.getFormattedDescription().forEach(line -> builder.lore("§7" + line)));

    builder.lore("").lore("§eBelohnungen:").lore("");

    this.rewards.forEach(reward -> reward.getDescription().forEach(line -> builder.lore("§f- " + line)));

    return builder.build();
  }

}
