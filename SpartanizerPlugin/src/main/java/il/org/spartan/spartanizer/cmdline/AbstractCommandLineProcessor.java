package il.org.spartan.spartanizer.cmdline;

import il.org.spartan.external.*;

public abstract class AbstractCommandLineProcessor {
  @External(alias = "i", value = "name of the input directory") protected String inputFolder = ".";
  @External(alias = "o", value = "name of the output directory") protected String outputFolder = "/tmp";

  public abstract void apply();

  public static void main(final String[] args) {
    if (args.length == 0)
      new BatchSpartanizer(".", "current-working-directory").fire();
    else
      for (final String ¢ : args)
        new BatchSpartanizer(¢).fire();
  }
}