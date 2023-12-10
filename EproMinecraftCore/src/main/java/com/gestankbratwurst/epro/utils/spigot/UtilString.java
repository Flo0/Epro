package com.gestankbratwurst.epro.utils.spigot;

public class UtilString {

  public static String progressBar(double progress, int length, char symbol, char completedColor, char uncompletedColor) {
    int completed = (int) (progress * length);
    int uncompleted = length - completed;
    return "§7[" +
        '§' + completedColor +
        String.valueOf(symbol).repeat(Math.max(0, completed)) +
        '§' + uncompletedColor +
        String.valueOf(symbol).repeat(Math.max(0, uncompleted)) +
        "§7]";
  }

}
