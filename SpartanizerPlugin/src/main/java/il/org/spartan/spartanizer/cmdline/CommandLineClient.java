package il.org.spartan.spartanizer.cmdline;

import il.org.spartan.spartanizer.cmdline.report.*;

/** Simplified version of command line client that uses spartanizer applicator
 * @author Matteo Orru' */
public class CommandLineClient {
  // TODO Matteo: try to fix compilation errors - matteo
  static String inputDir = ".";
  private static String outputDir = "/tmp";
  @SuppressWarnings("unused") private static MetricsReport metricsReport = new MetricsReport();

  public static void main(final String[] args) {
    if (args.length == 0)
      processCommandLine(args);
  }

  @SuppressWarnings({ "unused" }) private static void processCommandLine(final String[] args) {
    new CommandLineClient();
    // final List<String> remaining = extract(args, r);
    //
    MetricsReport.getSettings().setInputFolder(inputDir);
    MetricsReport.getSettings().setOutputFolder(outputDir);
    MetricsReport.initialize();
    // ReportGenerator.setOutputFolder(outputDir);
    // ReportGenerator.setInputFolder(inputDir);
    //
    new CommandLineSpartanizer(inputDir).apply();
    // r.printExternals();
    MetricsReport.write();
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
