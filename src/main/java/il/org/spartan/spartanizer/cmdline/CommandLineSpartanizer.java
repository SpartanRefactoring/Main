package il.org.spartan.spartanizer.cmdline;

import java.io.*;

/** A configurable version of the CommandLineSpartanizer that relies on
 * {@link CommandLineApplicator} and {@link CommandLineSelection}
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLineSpartanizer extends AbstractCommandLineProcessor {
  private final String name;
  private boolean commandLineApplicator;
  private final boolean collectApplicator = true;

  CommandLineSpartanizer(final String path) {
    this(path, system.folder2File(path));
  }
  CommandLineSpartanizer(final String presentSourcePath, final String name) {
    this.presentSourcePath = presentSourcePath;
    this.name = name;
  }
  @Override public void apply() {
    System.out.println(presentSourcePath);
    try {
      if (collectApplicator) {
        Reports.initializeReport(folder + name + ".tips.CSV", "tips");
        CollectApplicator.defaultApplicator().selection(CommandLineSelection.of(CommandLineSelection.Util.getAllCompilationUnit(presentSourcePath)))
            .go();
        Reports.close("tips");
        System.err.println("CollectApplicator: " + "Done!");
      }
      if (commandLineApplicator) {
        Reports.initializeFile(folder + name + ".before.java", "before");
        Reports.initializeFile(folder + name + ".after.java", "after");
        Reports.initializeReport(folder + name + ".CSV", "metrics");
        Reports.initializeReport(folder + name + ".spectrum.CSV", "spectrum");
        CommandLineApplicator.defaultApplicator().passes(20)
            .selection(CommandLineSelection.of(CommandLineSelection.Util.getAllCompilationUnit(presentSourcePath))).go();
        Reports.close("metrics");
        Reports.close("spectrum");
        Reports.closeFile("before");
        Reports.closeFile("after");
        System.err.println("commandLineApplicator: " + "Done!");
      }
    } catch (final IOException x) {
      x.printStackTrace();
    }
  }
}