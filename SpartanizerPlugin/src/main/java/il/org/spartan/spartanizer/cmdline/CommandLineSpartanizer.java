package il.org.spartan.spartanizer.cmdline;

import java.io.*;

import il.org.spartan.*;

/** A configurable version of the GUIBatchLaconizer that relies on
 * {@link CommandLineApplicator} and {@link CommandLineSelection}
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLineSpartanizer extends AbstractCommandLineSpartanizer {
  private CommandLineSelection selection;
  // private final boolean shouldRun = false;
  private final boolean applyToEntireProject = false;
  private final boolean entireProject = true;
  private final boolean specificTipper = false;

  // public static void main(final String[] args) {
  // for (final String ¢ : args.length != 0 ? args : new String[] { "." })
  // new CommandLineSpartanizer(¢).fire();
  // }
  CommandLineSpartanizer(final String path) {
    this(path, system.folder2File(path));
  }

  CommandLineSpartanizer(final String inputPath, final String name) {
    this.inputPath = inputPath;
    beforeFileName = folder + name + ".before.java";
    afterFileName = folder + name + ".after.java";
    reportFileName = folder + name + ".CSV";
    spectrumFileName = folder + name + ".spectrum.CSV";
    try {
      befores = new PrintWriter(beforeFileName);
      afters = new PrintWriter(afterFileName);
    } catch (final FileNotFoundException x) {
      x.printStackTrace();
    }
    // Matteo: Please do not delete the following instructions.
    // They are needed to instantiate report in commandline classes
    try {
      report = new CSVStatistics(reportFileName, "property");
      spectrumStats = new CSVStatistics(spectrumFileName, "property");
    } catch (final IOException x) {
      x.printStackTrace();
      System.err.println("problem in setting up reports");
    }
  }

  @Override public void apply() {
    CommandLineApplicator.defaultApplicator().passes(20)
        .selection(CommandLineSelection.of(CommandLineSelection.Util.getAllCompilationUnit(inputPath))).go();
  }
  // if (applyToEntireProject) {
  // selection = new CommandLineSelection(new
  // ArrayList<WrappedCompilationUnit>(), "project");
  // selection.createSelectionFromProjectDir(inputPath);
  // }
  // if (entireProject)
  // CommandLineApplicator.defaultApplicator().runAction();
  // if (specificTipper)
  // CommandLineApplicator.defaultApplicator();
  // }
}
