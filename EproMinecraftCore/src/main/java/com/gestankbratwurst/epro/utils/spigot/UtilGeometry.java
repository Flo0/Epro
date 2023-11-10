package com.gestankbratwurst.epro.utils.spigot;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class UtilGeometry {

  public static Location getRandomLocationOnCircleXY(Location center, double radius) {
    double angle = Math.random() * 2 * Math.PI;
    double x = Math.cos(angle) * radius;
    double z = Math.sin(angle) * radius;
    return center.clone().add(x, 0, z);
  }

  public static Location getRandomLocationOnCircleFacing(Location location, double radius, Vector circleDirection) {
    Vector circleNormal = circleDirection.clone().crossProduct(new Vector(0, 1, 0)).normalize();
    double angle = Math.random() * 2 * Math.PI;
    double x = Math.cos(angle) * radius;
    double z = Math.sin(angle) * radius;
    return location.clone().add(circleNormal.clone().multiply(x)).add(circleDirection.clone().multiply(z));
  }

}
