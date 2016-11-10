package il.org.spartan.spartanizer.cmdline;

//import static il.org.spartan.external.External.Introspector.*;

import java.util.*;

import il.org.spartan.*;
//import il.org.spartan.external.*;

/** Simplified version of command line client that uses spartanizer applicator
 * @author Matteo Orru' */
public class CommandLineClient {
  // TODO Matteo: try to fix compilation errors - matteo
//  @External(alias = "i", value = "name of the input directory")  
  static String inputDir = ".";
//  @External(alias = "o", value = "name of the output directory")  
  private static String outputDir = "/tmp";
//  @External(alias = "d", value = "default values for the directories")  
  private static boolean devault;

  public static void main(final String[] args) {
//    for (final String ¢ : args.length != 0 ? args : as.array("."))
//      new CommandLineSpartanizer(¢).apply();
    if(args.length == 0)
//      usageErrorExit("name(s)", new CommandLineClient());
    processCommandLine(args);
  }
  
  @SuppressWarnings("unused") private static void processCommandLine(final String[] args) {
    CommandLineClient r = new CommandLineClient();
//    final List<String> remaining = extract(args, r);
    //
    Reports.setOutputFolder(outputDir);
    Reports.setInputFolder(inputDir);
    //
    new CommandLineSpartanizer(inputDir).apply();
//    r.printExternals();
  }
  
  private void printExternals() {
//    System.out.println(usage(this));
    System.out.println("Externals after processing command line arguments:");
    System.out.println("==================================================");
    System.out.println("outputDir: " + outputDir());
    System.out.println("inputDir: " + inputDir());
    System.out.println();
} 

  @SuppressWarnings({ "static-method" }) 
  private String inputDir() {
    return inputDir;
  }

  @SuppressWarnings({ "static-method" }) 
  private String outputDir() {
    return outputDir;
  }

  @SuppressWarnings("unused") private static void parseCommandLineArgs(final String[] args) {
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
