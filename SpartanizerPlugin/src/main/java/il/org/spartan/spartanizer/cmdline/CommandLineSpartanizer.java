package il.org.spartan.spartanizer.cmdline;

import java.io.*;

/** A configurable version of the GUIBatchLaconizer that relies on
 * {@link CommandLineApplicator} and {@link CommandLineSelection}
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLineSpartanizer extends AbstractCommandLineSpartanizer2 {
  private String name;

  CommandLineSpartanizer(final String path) {
    this(path, system.folder2File(path));
  }

  CommandLineSpartanizer(final String inputPath, final String name) {
    this.inputPath = inputPath;
    this.name = name;
  }

  @Override public void apply() {
    try {
      Reports.initializeFile(folder + name + ".before.java", "before");
      Reports.initializeFile(folder + name + ".after.java", "after");
      Reports.intializeReport(folder + name + ".CSV", "metrics");
      Reports.intializeReport(folder + name + ".spectrum.CSV", "spectrum");
      CommandLineApplicator.defaultApplicator()
                         .passes(20)
                         .selection(CommandLineSelection.of(CommandLineSelection.Util
                                                                                .getAllCompilationUnit(inputPath)))
                         .go();
      Reports.close("metrics");
      Reports.close("spectrum");
      Reports.closeFile("before");
      Reports.closeFile("after");
    } catch (IOException x) {
      x.printStackTrace();
    }
    
  }

//  @Override void go(@SuppressWarnings("unused") final String javaCode) {
//    // TODO Matteo: take or remove this?
//  }
}