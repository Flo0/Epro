package com.gestankbratwurst.epro.tasks;

public class AverageContinuous {

  private final double perTick;
  private double current = 0;

  public AverageContinuous(double perTick) {
    this.perTick = perTick;
  }

  public int next() {
    this.current += this.perTick;
    int result = (int) this.current;
    this.current -= result;
    return result;
  }

}
