package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.function.*;

import org.eclipse.ui.internal.handlers.WizardHandler.*;

import il.org.spartan.plugin.*;

/** A configurable version of the CommandLineSpartanizer that relies on
 * {@link CommandLineApplicator} and {@link CommandLineSelection}
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLineSpartanizer extends AbstractCommandLineProcessor {
  private final String name;
  private boolean selection;
  private final boolean CommandLine$Applicator = true;
  private boolean Spartanizer$Applicator;
  private boolean DefaultApplicator;
  
  private CommandLineApplicator c = new CommandLineApplicator();

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
      // if (collectApplicator) {
      // Reports.initializeReport(folder + name + ".tips.CSV", "tips");
      // CollectApplicator.defaultApplicator().selection(CommandLineSelection.of(CommandLineSelection.Util.getAllCompilationUnit(presentSourcePath)))
      // .go();
      // Reports.close("tips");
      // System.err.println("CollectApplicator: " + "Done!");
      // }
      System.out.println("Reports.getOutputFolder(): " + Reports.getOutputFolder());
      Reports.initializeFile(Reports.getOutputFolder() + "/" + name + ".before.java", "before");
      Reports.initializeFile(Reports.getOutputFolder() + "/" + name + ".after.java", "after");
      Reports.initializeReport(Reports.getOutputFolder() + "/" + name + ".CSV", "metrics");
      Reports.initializeReport(Reports.getOutputFolder() + "/" + name + ".spectrum.CSV", "spectrum");
      
      if (DefaultApplicator)
        c.listener(new Listener() {
          @Override public void tick(Object... ¢) {
                System.out.println("ok" + ¢);        
          }
        });
                
        CommandLineApplicator.defaultApplicator().defaultSelection(CommandLineSelection.Util.get(Reports.getInputFolder())).defaultListenerNoisy()
            .go();
      
      if (Spartanizer$Applicator)
        CommandLineApplicator.defaultApplicator().defaultSelection(CommandLineSelection.Util.get(Reports.getInputFolder()))
            .defaultRunAction(new Spartanizer$Applicator()).defaultListenerNoisy().go();
      
      if (CommandLine$Applicator)
        CommandLineApplicator.defaultApplicator().defaultSelection(CommandLineSelection.Util.get(Reports.getInputFolder()))
            .defaultRunAction(new CommandLine$Applicator()).defaultListenerNoisy().go();
      
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