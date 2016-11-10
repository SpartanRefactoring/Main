package il.org.spartan.spartanizer.cmdline;

/** Simplified version of command line client that uses spartanizer applicator
 * @author Matteo Orru' */
public class CommandLineClient {
  // TODO Matteo: try to fix compilation errors - matteo
  static String inputDir = ".";
  private static String outputDir = "/tmp";
public static void main(final String[] args) {
    if(args.length == 0)
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
