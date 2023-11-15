package com.gestankbratwurst.epro.gui.baseimpl;


public abstract non-sealed class DynamicGUI extends GuiHandler {

  @Override
  public void decorate() {
    this.clearButtons();
    this.setupButtons();
    super.decorate();
  }

  protected abstract void setupButtons();

}
