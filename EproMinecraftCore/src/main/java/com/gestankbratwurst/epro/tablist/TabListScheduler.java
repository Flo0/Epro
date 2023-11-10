package com.gestankbratwurst.epro.tablist;



import com.gestankbratwurst.epro.tablist.implementation.ScheduledSingleUserTabList;
import com.gestankbratwurst.epro.tasks.TaskManager;

import java.util.ArrayList;
import java.util.List;

public class TabListScheduler implements Runnable {

  private static final TabListScheduler INSTANCE = new TabListScheduler();

  protected static TabListScheduler getInstance() {
    return INSTANCE;
  }

  private final List<ScheduledSingleUserTabList> tabListList = new ArrayList<>();

  private TabListScheduler() {
    TaskManager.runTaskTimer(this, 1, 1);
  }

  protected void register(ScheduledSingleUserTabList tabList) {
    tabListList.add(tabList);
  }

  protected void unregister(ScheduledSingleUserTabList tabList) {
    tabListList.remove(tabList);
  }

  @Override
  public void run() {
    tabListList.forEach(ScheduledSingleUserTabList::onTick);
  }
}
