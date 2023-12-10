package com.gestankbratwurst.epro.quests.abstraction;

import com.gestankbratwurst.epro.utils.spigot.UtilString;

import java.util.ArrayList;
import java.util.List;

public interface QuestTarget {

  String getDisplay();

  List<String> getDescription();

  int getTargetAmount();

  int getCurrentAmount();

  default boolean isCompleted() {
    return this.getCurrentAmount() >= this.getTargetAmount();
  }

  default List<String> getFormattedDescription() {
    List<String> formatted = new ArrayList<>();

    formatted.add("§9" + getDisplay() + " §7(" + getCurrentAmount() + "/" + getTargetAmount() + ")");
    getDescription().forEach(line -> formatted.add("§7 " + line));
    formatted.add(UtilString.progressBar(getProgress(), 20, '█', 'a', '7') + "§7 " + ((int) (getProgress() * 100)) + "%");

    return formatted;
  }

  default double getProgress() {
    return isCompleted() ? 1.0 : (double) this.getCurrentAmount() / (double) this.getTargetAmount();
  }

}