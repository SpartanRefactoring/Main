package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.external.External.Introspector.*;

import il.org.spartan.external.*;
import il.org.spartan.spartanizer.cmdline.report.*;

/** Simplified version of command line client that uses spartanizer applicator
 * @author Matteo Orru' */
//<<<<<<< HEAD
public final class CommandLineClient extends HeadlessSpartanizer {
//=======
//public final class CommandLineClient {
//  @External(alias = "i", value = "name of the input directory") static String inputDir = ".";
//  @External(alias = "o", value = "name of the output directory") private static String outputDir = "/tmp";
//  @External(alias = "d", value = "default values for the directories") private static boolean devault;
//  @External(alias = "cs", value = "class name on which apply the tippers") private static String[] clazzes;
//  @External(alias = "tg", value = "tipper group to be applied to the clazzes") private static String[] tipperGroups;
//  @External(alias = "etg", value = "exclude one or more tipper groups") private static String[] excludeTipperGroups;
//  @External(alias = "np", value = "Nano Patterns") private static String[] NanoPatterns;
//  @External(alias = "enp", value = "Exclude Selected Nano Patterns") private static String[] excludeNanoPatterns;
//  @External(alias = "xallnp", value = "Exclude All Nano Patterns") private static boolean excludeAllNanoPatterns;
 private final MetricsReport metricsReport = new MetricsReport();

//>>>>>>> matteo-experimental
  public static void main(final String[] args) {
    new CommandLineClient().go(args);
  }

//<<<<<<< HEAD
//  private void go(final String[] args) {
//    if (External.Introspector.extract(args, this).isEmpty()) {
//      System.err.println(usage(this, args, this));
//      return;
//    }
//    MetricsReport.getSettings().setInputFolder(inputFolder);
//    MetricsReport.getSettings().setOutputFolder(outputFolder);
//    MetricsReport.initialize();
//=======
  private void go(String[] args) {
    
    // TODO Yossi, the instruction
    // External.Introspector.extract(args, this).isEmpty()
    // returns true (an empty list) even if args.length() > 0
    // I changed it

    if (args.length == 0) {
      System.err.println(usage(this, args, this));
      return;
    }
    
    extract(args, this);
 
//    MetricsReport.getSettings().setInputFolder(inputDir);
//    MetricsReport.getSettings().setOutputFolder(outputDir);
//    MetricsReport.initialize();
//>>>>>>> matteo-experimental
    ReportGenerator.generate("metrics");
    ReportGenerator.setOutputFolder(outputFolder);
    ReportGenerator.setInputFolder(inputFolder);
    run();
//    MetricsReport.generate();
  }
//<<<<<<< HEAD

//=======
  
  @SuppressWarnings("static-method")
//>>>>>>> matteo-experimental
  private void run() {
    name(system.folder2File(inputFolder));
    apply();
    // r.printExternals();
  }

  static void printPrompt() {
    System.out.println(" to be completed ... ");
  }
}
