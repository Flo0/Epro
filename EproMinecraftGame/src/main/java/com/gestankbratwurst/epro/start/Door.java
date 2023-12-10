package com.gestankbratwurst.epro.start;

import com.gestankbratwurst.epro.tasks.TaskManager;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;

import java.lang.ref.WeakReference;
import java.util.function.Consumer;

public class Door {

  private final WeakReference<World> worldRef;
  private final BoundingBox boundingBox;

  public Door(Block blockA, Block blockB) {
    this.worldRef = new WeakReference<>(blockA.getWorld());
    this.boundingBox = BoundingBox.of(blockA, blockB);
  }

  public Door(Location locationA, Location locationB) {
    this.worldRef = new WeakReference<>(locationA.getWorld());
    this.boundingBox = BoundingBox.of(locationA.getBlock(), locationB.getBlock());
  }

  public static void forEachBlock(final BoundingBox box, final World world, final Consumer<Block> blockConsumer, int delay) {
    for (int y = (int) box.getMinY(); y <= (int) box.getMaxY() - 1; y++) {
      int finalY = y;
      TaskManager.runTaskLater(() -> {
        for (int x = (int) box.getMinX(); x <= (int) box.getMaxX() - 1; x++) {
          for (int z = (int) box.getMinZ(); z <= (int) box.getMaxZ() - 1; z++) {
            blockConsumer.accept(world.getBlockAt(x - 1, finalY, z));
          }
        }
      }, (long) delay * (y - (int) box.getMinY()));
    }
  }

  public void open(int delay) {
    World world = worldRef.get();
    if (world == null) {
      return;
    }
    forEachBlock(this.boundingBox, world, block -> {
      block.setType(Material.AIR);
      Location location = block.getLocation().add(0.5, 0.5, 0.5);
      world.spawnParticle(Particle.BLOCK_CRACK, location, 5, 0.1, 0.1, 0.1, 0.1, Material.SPRUCE_PLANKS.createBlockData());
      Sound sound = Sound.sound(org.bukkit.Sound.BLOCK_WOODEN_DOOR_OPEN, Sound.Source.BLOCK, 1.2F, 1.2F);
      world.playSound(sound, location.x(), location.y(), location.z());
    }, delay);
  }

  public void build() {
    World world = worldRef.get();
    if (world == null) {
      return;
    }
    forEachBlock(this.boundingBox, world, block -> {
      block.setType(Material.SPRUCE_FENCE);
      Location location = block.getLocation().add(0.5, 0.5, 0.5);
      Sound sound = Sound.sound(org.bukkit.Sound.BLOCK_WOOD_PLACE, Sound.Source.BLOCK, 1.2F, 1.2F);
      world.playSound(sound, location.x(), location.y(), location.z());
    }, 2);
  }

}
