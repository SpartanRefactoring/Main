package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.external.External.Introspector.*;

import il.org.spartan.external.*;
import il.org.spartan.spartanizer.cmdline.report.*;

/** Simplified version of command line client that uses spartanizer applicator
 * @author Matteo Orru' */
public final class CommandLineClient extends HeadlessSpartanizer {
  public static void main(final String[] args) {
    new CommandLineClient().go(args);
  }

  private void go(final String[] args) {
    if (External.Introspector.extract(args, this).isEmpty()) {
      System.err.println(usage(this, args, this));
      return;
    }
    MetricsReport.getSettings().setInputFolder(inputFolder);
    MetricsReport.getSettings().setOutputFolder(outputFolder);
    MetricsReport.initialize();
    ReportGenerator.generate("metrics");
    ReportGenerator.setOutputFolder(outputFolder);
    ReportGenerator.setInputFolder(inputFolder);
    run();
    MetricsReport.generate();
  }

  private void run() {
    name(system.folder2File(inputFolder));
    apply();
    // r.printExternals();
  }

  static void printPrompt() {
    System.out.println(" to be completed ... ");
  }
}
