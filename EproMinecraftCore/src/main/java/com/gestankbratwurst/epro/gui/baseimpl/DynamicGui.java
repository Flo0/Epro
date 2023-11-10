package com.gestankbratwurst.epro.gui.baseimpl;


import com.gestankbratwurst.epro.gui.abstraction.GuiHandler;

public abstract class DynamicGui extends GuiHandler {

  @Override
  public void decorate() {
    this.setupButtons();
    super.decorate();
  }

  protected abstract void setupButtons();

}
