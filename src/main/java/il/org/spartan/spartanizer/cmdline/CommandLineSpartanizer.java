package il.org.spartan.spartanizer.cmdline;

import java.io.*;

import il.org.spartan.*;

/** A configurable version of the GUIBatchLaconizer that relies on
 * {@link CommandLineApplicator} and {@link CommandLineSelection}
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLineSpartanizer extends AbstractCommandLineSpartanizer2 {
  private final boolean applyToEntireProject = false;
  private final boolean entireProject = true;
  private final boolean specificTipper = false;
  private String name;

  CommandLineSpartanizer(final String path) {
    this(path, system.folder2File(path));
  }

  CommandLineSpartanizer(final String inputPath, final String name) {
    this.inputPath = inputPath;
    this.name = name;
//    Reports.initializeFile(folder + name + ".before.java", "before");
//    Reports.initializeFile(folder + name + ".after.java", "after");
//    beforeFileName = folder + name + ".before.java";
//    afterFileName = folder + name + ".after.java";
//    Reports.intializeReport(folder + name + ".CSV", "metrics");
//    Reports.intializeReport(folder + name + ".spectrum.CSV", "spectrum");
//    reportFileName = folder + name + ".CSV";
//    spectrumFileName = folder + name + ".spectrum.CSV";
//    try {
//      befores = new PrintWriter(beforeFileName);
//      afters = new PrintWriter(afterFileName);
//    } catch (final FileNotFoundException x) {
//      x.printStackTrace();
//    }
    // Matteo: Please do not delete the following instructions.
    // They are needed to instantiate report in commandline classes
//    try {
//      report = new CSVStatistics(reportFileName, "property");
//      spectrumStats = new CSVStatistics(spectrumFileName, "property");
//    } catch (final IOException x) {
//      x.printStackTrace();
//      System.err.println("problem in setting up reports");
//    }
  }

  @Override public void apply() {
    Reports.initializeFile(folder + name + ".before.java", "before");
    Reports.initializeFile(folder + name + ".after.java", "after");
//    beforeFileName = folder + name + ".before.java";
//    afterFileName = folder + name + ".after.java";
    Reports.intializeReport(folder + name + ".CSV", "metrics");
    Reports.intializeReport(folder + name + ".spectrum.CSV", "spectrum");
    CommandLineApplicator.defaultApplicator().passes(20)
        .selection(CommandLineSelection.of(CommandLineSelection.Util.getAllCompilationUnit(inputPath))).go();
    Reports.close("metrics");
    Reports.close("spectrum");
    Reports.closeFile("before");
    Reports.closeFile("after");
  }

  @Override void go(@SuppressWarnings("unused") final String javaCode) {
    // TODO Matteo: take or remove this?
  }
}