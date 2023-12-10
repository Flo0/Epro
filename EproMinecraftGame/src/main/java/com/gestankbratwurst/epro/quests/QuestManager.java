package com.gestankbratwurst.epro.quests;

import com.gestankbratwurst.epro.EproMinecraftGame;
import com.gestankbratwurst.epro.quests.impl.QuestTargetListener;

public class QuestManager {

  public QuestManager() {
    EproMinecraftGame.register(new QuestTargetListener(this));
  }

}
