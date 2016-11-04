package il.org.spartan.spartanizer.cmdline;

import il.org.spartan.*;

/** Simplified version of command line client that uses spartanizer applicator
 * @author Matteo Orru' */
@SuppressWarnings("unused") public class CommandLineClient { private static String outputDir;
private static String folder;

  public static void main(final String[] args) {
    parseCommandLineArgs(args);
    for (final String ¢ : args.length != 0 ? args : as.array("."))
      new CommandLineSpartanizer(¢).apply();
  }

  static String inputDir;

  private static void parseCommandLineArgs(String[] args) {
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