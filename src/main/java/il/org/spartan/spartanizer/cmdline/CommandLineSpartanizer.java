package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.function.*;

import il.org.spartan.plugin.*;

/** A configurable version of the CommandLineSpartanizer that relies on
 * {@link CommandLineApplicator} and {@link CommandLineSelection}
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLineSpartanizer extends AbstractCommandLineProcessor {
  private final String name;
  private boolean commandLineApplicator = true;
  private final boolean collectApplicator = false;
  private boolean selection = false;

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
        // ---
        CommandLineApplicator.defaultApplicator()
                             .defaultListenerNoisy()
                             .defaultRunAction(new CommandLine$Applicator())
                             .go();
        // ---
        Reports.close("metrics");
        Reports.close("spectrum");
        Reports.closeFile("before");
        Reports.closeFile("after");
        System.err.println("commandLineApplicator: " + "Done!");
      }
      if(selection)
        CommandLineApplicator.defaultApplicator().defaultListenerNoisy()
            .defaultSelection(CommandLineSelection.of(CommandLineSelection.Util.getAllCompilationUnit(presentSourcePath)))
            .defaultRunAction(new CommandLine$Applicator()).go();
    } catch (final IOException x) {
      x.printStackTrace();
    }
  }

  @SuppressWarnings("unused") private Function<WrappedCompilationUnit, Integer> getSpartanizer() {
    return (u -> Integer.valueOf(
        (new CommandLine$Applicator()).apply(CommandLineSelection.of(CommandLineSelection.Util.getAllCompilationUnit(presentSourcePath))) ? 1 : 0));
  }
}