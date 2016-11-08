package il.org.spartan.spartanizer.cmdline;

import il.org.spartan.*;
// import il.org.spartan.external.*;

/** Simplified version of command line client that uses spartanizer applicator
 * @author Matteo Orru' */
public class CommandLineClient {
  // TODO Matteo: try to fix compilation errors - matteo
  // @External
  static String inputDir = "";
  // @External
  private static String outputDir = "";

  public static void main(final String[] args) {
    if (args.length == 0)
      // usageErrorExit("name(s)", new CommandLineClient());
      parseCommandLineArgs(args);
    for (final String ¢ : args.length != 0 ? args : as.array("."))
      new CommandLineSpartanizer(¢).apply();
  }
  static void printExternals() {
    System.out.println("Externals after processing command line arguments:");
    System.out.println("==================================================");
    System.out.println("outputDir: " + Base.outputDir());
    System.out.println("inputDir: " + Base.inputDir());
    System.out.println();
  }
  @SuppressWarnings({ "unused", "static-method" }) private String inputDir() {
    return inputDir;
  }
  @SuppressWarnings({ "static-method", "unused" }) private String outputDir() {
    return outputDir;
  }
  private static void parseCommandLineArgs(final String[] args) {
    if (args.length == 0)
      printPrompt();
    for (int ¢ = 0; ¢ < args.length;)
      if ("-o".equals(args[¢]))
        ¢ += 2;
      else if ("-i".equals(args[¢])) {
        inputDir = args[¢ + 1];
        ¢ += 2;
      } else if ("-d".equals(args[¢])) {
        inputDir = ".";
        ¢ += 1;
      } else {
        System.out.println(args[¢]);
        System.out.println("[ERROR]: Something went wrong! Parameter or switch not allowed");
        ++¢;
      }
  }
  static void printPrompt() {
    System.out.println("Help");
    System.out.println("");
    System.out.println("Options:");
    System.out.println("  -d       default directory: use the current directory for the analysis");
    System.out.println("  -o       output directory: here go the results of the analysis");
    System.out.println("  -i       input directory: place here the projects that you want to analyze.");
    System.out.println("");
  }
}

class Base {
  // @External(alias = "i")
  private static String inputDir;
  // @External(alias = "o")
  private static String outputDir;

  static String inputDir() {
    return inputDir;
  }
  static String outputDir() {
    return outputDir;
  }
}
