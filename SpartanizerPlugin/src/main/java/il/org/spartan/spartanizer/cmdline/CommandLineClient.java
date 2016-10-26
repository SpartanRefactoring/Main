package il.org.spartan.spartanizer.cmdline;

/**
 * Simplified version of command line client that uses spartizer applicator
 * @author Matteo Orru'
 */

public class CommandLineClient { // extends AbstractCommandLineSpartanizer{
  
//  private Spartanizer $ = new Spartanizer();
  
  public static void main(final String[] args) {
    for (final String ¢ : args.length != 0 ? args : new String[] { "." })
      new CommandLineSpartanizer(¢).fire();
  }
  
//  CommandLineClient(final String path) {
//    this(path, system.folder2File(path));
//  }

//  CommandLineClient(final String inputPath, final String name) {
//    this.inputPath = inputPath;
//    beforeFileName = folder + name + ".before.java";
//    afterFileName = folder + name + ".after.java";
//    reportFileName = folder + name + ".CSV";
//    spectrumFileName = folder + name + ".spectrum.CSV";
//    try {
//      befores = new PrintWriter(beforeFileName);
//      afters = new PrintWriter(afterFileName);
//    } catch (final FileNotFoundException x) {
//      x.printStackTrace();
//    }
//    
//    // this is needed here, for the moment, otherwise report is not printed
//    
//    try {
//      report = new CSVStatistics(reportFileName, "property");
//      spectrumStats = new CSVStatistics(spectrumFileName, "property");
//    } catch (IOException x) {
//      x.printStackTrace();
//      System.err.println("problem in setting up reports");
//    }
//  }
  
//  @Override public void apply() {
//    
//    CommandLineApplicator.defaultApplicator()
//               .passes(20)
//               .selection(CommandLineSelection.of(CommandLineSelection.Util.getAllCompilationUnit(inputPath)))
//               .go();
//    
//  }
  
//  public void method() {
//    
//    System.err.println("------------------");
//    System.err.println(GUIBatchLaconizer.defaultApplicator()
//               .defaultListenerNoisy()
//               .passes(20)
//               .selection(CommandLineSelection.of(CommandLineSelection.Util.getAllCompilationUnit(inputPath))));
//  }

}
