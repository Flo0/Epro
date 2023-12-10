package com.gestankbratwurst.epro.tablist.implementation;

import java.util.concurrent.ThreadLocalRandom;

public class EmptyTabList extends AbstractTabList {

  private static final String[] RND_FOOTER = new String[]{
      "§7Jetzt mit Keksen",
      "§7Ess dein Knoppers schon um 9!",
      "§7Du hast Koriander zwischen den Zähnen",
      "§7Komm auf den Discord",
      "§7404 – fortune not found",
      "§7Glaubt btw25 nicht",
      "§7Ich mag Züge",
      "§7Jetzt mit 100% mehr Keksen",
      "§7Jetzt mit 100% mehr Koriander",
      "§7Jetzt mit 100% mehr Zügen",
      "§7Jetzt mit 7% mehr Discord",
      "§7Jetzt mit 100% mehr btw25",
      "§7Probiers mal mit Gemütlichkeit",
      "§7Jetzt mit 100% mehr Gemütlichkeit",
      "§7Bitte keine Werbung\n\n§7Jetzt mit §a100% §7mehr Werbung",
      "§7Bitte keine Steine werfen...",
      "§7Bitte keine Steine werfen...\n\n§7Jetzt mit §a100% §7mehr Werbung",
      "§7Nachsts ist es kälter als draußen",
      "§7Bier macht dick.\n§7Ollo ist der Beweis :)",
      "§7Wie lange noch?"
  };

  public EmptyTabList() {
    super();
    this.setHeader("\n§e§l✪ Epro ✪\n");
    setRandomFooter();
  }

  @Override
  public void init() {

  }

  private void setRandomFooter() {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    setFooter(RND_FOOTER[random.nextInt(RND_FOOTER.length)]);
  }

}
