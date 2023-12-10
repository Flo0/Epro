package com.gestankbratwurst.epro.quests;

import com.gestankbratwurst.epro.quests.abstraction.QuestReward;
import com.gestankbratwurst.epro.quests.abstraction.QuestTarget;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuestBuilder {

  private String name;
  private ItemStack icon;
  private final List<QuestReward> rewards = new ArrayList<>();
  private final List<QuestTarget> targets = new ArrayList<>();

  protected QuestBuilder() {
  }

  public QuestBuilder name(String name) {
    this.name = name;
    return this;
  }

  public QuestBuilder icon(ItemStack icon) {
    this.icon = icon;
    return this;
  }

  public QuestBuilder addReward(QuestReward reward) {
    this.rewards.add(reward);
    return this;
  }

  public QuestBuilder addTarget(QuestTarget target) {
    this.targets.add(target);
    return this;
  }

  public Quest build() {
    if (this.name == null || this.icon == null) {
      throw new IllegalStateException("Name and Icon must be set.");
    }
    Quest quest = new Quest(this.name, this.icon);
    this.rewards.forEach(quest::addReward);
    this.targets.forEach(quest::addTarget);
    return quest;
  }

}
