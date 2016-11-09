package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.function.*;

import il.org.spartan.plugin.*;

/** A configurable version of the CommandLineSpartanizer that relies on
 * {@link CommandLineApplicator} and {@link CommandLineSelection}
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLineSpartanizer extends AbstractCommandLineProcessor {
  private String name;
  private boolean selection;
  private boolean CommandLine$Applicator;
  private boolean Spartanizer$Applicator = true;
  private boolean DefaultApplicator;

  CommandLineSpartanizer(final String path) {
    this(path, system.folder2File(path));
  }
  CommandLineSpartanizer(final String presentSourcePath, final String name) {
    this.presentSourcePath = presentSourcePath;
    this.name = name;
  }
  @Override public void apply() {
    System.out.println("presentsSourcePath:" + presentSourcePath);
    try {
//      if (collectApplicator) {
//        Reports.initializeReport(folder + name + ".tips.CSV", "tips");
//        CollectApplicator.defaultApplicator().selection(CommandLineSelection.of(CommandLineSelection.Util.getAllCompilationUnit(presentSourcePath)))
//            .go();
//        Reports.close("tips");
//        System.err.println("CollectApplicator: " + "Done!");
//      }
      Reports.initializeFile(Reports.getOutputFolder() + name + ".before.java", "before");
      Reports.initializeFile(Reports.getOutputFolder() + name + ".after.java", "after");
      Reports.initializeReport(Reports.getOutputFolder() + name + ".CSV", "metrics");
      Reports.initializeReport(Reports.getOutputFolder() + name + ".spectrum.CSV", "spectrum");
      
      if (DefaultApplicator)
        CommandLineApplicator.defaultApplicator().defaultListenerNoisy().go();
      if (Spartanizer$Applicator )
        CommandLineApplicator.defaultApplicator().defaultRunAction(new Spartanizer$Applicator()).defaultListenerNoisy().go();
      if (CommandLine$Applicator)
        CommandLineApplicator.defaultApplicator().defaultRunAction(new CommandLine$Applicator()).defaultListenerNoisy().go();
      
      Reports.close("metrics");
      Reports.close("spectrum");
      Reports.closeFile("before");
      Reports.closeFile("after");
      System.err.println("commandLineApplicator: " + "Done!");

      if (selection)
        CommandLineApplicator.defaultApplicator().defaultListenerNoisy()
            .defaultSelection(CommandLineSelection.of(CommandLineSelection.Util.getAllCompilationUnit(presentSourcePath)))
            .defaultRunAction(new CommandLine$Applicator()).go();
    } catch (final IOException x) {
      x.printStackTrace();
    }
  }
  @SuppressWarnings("unused") private Function<WrappedCompilationUnit, Integer> getSpartanizer() {
    return u -> Integer.valueOf(
        new CommandLine$Applicator().apply(CommandLineSelection.of(CommandLineSelection.Util.getAllCompilationUnit(presentSourcePath))) ? 1 : 0);
  }
}