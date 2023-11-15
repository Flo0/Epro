package com.gestankbratwurst.epro.utils.spigot;

import com.gestankbratwurst.epro.tasks.TaskManager;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;

public class UtilPlayer {

  public static void successSound(Player player) {
    Sound firstSound = Sound.sound()
        .pitch(1.0F)
        .volume(1.0F)
        .type(org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL)
        .build();
    Sound secondSound = Sound.sound()
        .pitch(1.33F)
        .volume(1.0F)
        .type(org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL)
        .build();
    player.playSound(firstSound);
    TaskManager.runTaskLater(() -> player.playSound(secondSound), 4);
  }

  public static void failSound(Player player) {
    Sound firstSound = Sound.sound()
        .pitch(0.8F)
        .volume(1.0F)
        .type(org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL)
        .build();
    Sound secondSound = Sound.sound()
        .pitch(0.8F - 0.33F)
        .volume(1.0F)
        .type(org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL)
        .build();
    player.playSound(firstSound);
    TaskManager.runTaskLater(() -> player.playSound(secondSound), 4);
  }

}