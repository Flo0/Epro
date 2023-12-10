package com.gestankbratwurst.epro.quests.impl;

import com.gestankbratwurst.epro.quests.QuestManager;
import org.bukkit.event.Listener;

public class QuestTargetListener implements Listener {

  private final QuestManager questManager;

  public QuestTargetListener(QuestManager questManager) {
    this.questManager = questManager;
  }
}
