package com.gestankbratwurst.epro.start;

import com.gestankbratwurst.epro.tasks.TaskManager;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.time.Duration;
import java.util.List;

public class StartManager {

  private final List<Door> doors;

  public StartManager() {
    World world = Bukkit.getWorlds().get(0);
    this.doors = List.of(
        new Door(world.getBlockAt(-754, 66, 1020).getLocation(), world.getBlockAt(-754, 69, 1019).getLocation()),
        new Door(world.getBlockAt(-737, 66, 1020).getLocation(), world.getBlockAt(-737, 69, 1019).getLocation()),
        new Door(world.getBlockAt(-745, 66, 1011).getLocation(), world.getBlockAt(-746, 69, 1011).getLocation()),
        new Door(world.getBlockAt(-746, 66, 1028).getLocation(), world.getBlockAt(-745, 69, 1028).getLocation())
    );
  }

  public void startGame() {
    for (Door door : doors) {
      door.open(20);
    }
    TaskManager.runTaskLater(() -> Bukkit.getOnlinePlayers().forEach(player -> {
      Component text = Component.text("Epro beginnt!").color(NamedTextColor.YELLOW);
      Title.Times times = Title.Times.times(Duration.ofMillis(1000), Duration.ofMillis(4000), Duration.ofMillis(500));
      Title title = Title.title(text, Component.empty(), times);
      player.showTitle(title);
      Sound sound = Sound.sound(org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, Sound.Source.MASTER, 1F, 1F);
      player.playSound(sound);
    }), 20 * 5 - 10);
  }

  public void reset() {
    for (Door door : doors) {
      door.build();
    }
  }

}
