package il.org.spartan.spartanizer.cmdline.applications;

import il.org.spartan.external.*;
import il.org.spartan.spartanizer.cmdline.*;

abstract class AbstractCommandLineProcessor {
  @External(alias = "i", value = "input folder") String inputFolder = ".";
  @External(alias = "o", value = "output folder") final String outputFolder = "/tmp";
  private String presentSourceName;

  public abstract void apply();

  protected String makeFile(final String fileName) {
    return outputFolder + "/" + presentSourceName + "." + fileName;
  }

  public static void main(final String[] args) {
    if (args.length == 0)
      new BatchSpartanizer(".", "current-working-directory").fire();
    else
      for (final String ¢ : args)
        new BatchSpartanizer(¢).fire();
  }
}