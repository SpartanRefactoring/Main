package il.org.spartan.spartanizer.cmdline;

public abstract class AbstractCommandLineProcessor {
  protected String folder = "/tmp/";
  protected String presentSourcePath;

  public abstract void apply();

  public static void main(final String[] args) {
    if (args.length == 0)
      new BatchSpartanizer(".", "current-working-directory").fire();
    else
      for (final String ¢ : args)
        new BatchSpartanizer(¢).fire();
  }
}