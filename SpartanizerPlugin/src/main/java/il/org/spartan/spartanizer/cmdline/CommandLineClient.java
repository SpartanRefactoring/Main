package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.external.External.Introspector.*;

import il.org.spartan.spartanizer.cmdline.report.*;

/** Simplified version of command line client that uses spartanizer applicator
 * @author Matteo Orru' */
public final class CommandLineClient extends HeadlessSpartanizer {
  public static void main(final String[] args) {
    new CommandLineClient().go(args);
  }

  private void go(final String[] args) {
    // TODO Yossi, the instruction
    // External.Introspector.extract(args, this).isEmpty()
    // returns true (an empty list) even if args.length() > 0
    // I changed it
    if (args.length == 0) {
      System.err.println(usage(this, args, this));
      return;
    }
    extract(args, this);
    ReportGenerator.generate("metrics");
    ReportGenerator.setOutputFolder(outputDir);
    ReportGenerator.setInputFolder(inputDir);
    run();
  }

  @SuppressWarnings("static-method") private void run() {
    name(system.folder2File(inputDir));
    apply();
  }

  static void printPrompt() {
    System.out.println(" to be completed ... ");
  }
}
