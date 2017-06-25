package il.org.spartan.spartanizer.cmdline.good;

import static il.org.spartan.external.External.Introspector.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.cmdline.runnables.*;

/** Simplified version of command line client that uses spartanizer applicator
 * @author Matteo Orru'
 * @since Oct 26, 2016 */
final class CommandLineClient extends HeadlessSpartanizer {
  public static void main(final String[] args) {
    new CommandLineClient().go(args);
  }
  private void go(final String... args) {
    if (args.length == 0) {
      System.err.println(usage(this, args, this));
      return;
    }
    extract(args, this);
    ReportGenerator.generate("metrics");
    ReportGenerator.generate("methods");
    ReportGenerator.setOutputFolder(outputFolder);
    ReportGenerator.setInputFolder(inputFolder);
    run();
  }
  private void run() {
    name(system.folder2File(inputFolder));
    apply();
  }
  static void printPrompt() {
    System.out.println(" to be completed ... ");
  }
}
