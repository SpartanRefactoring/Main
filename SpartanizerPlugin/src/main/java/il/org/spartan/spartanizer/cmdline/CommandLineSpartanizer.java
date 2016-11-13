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
  private final boolean CommandLine$Applicator = true;
  private boolean Spartanizer$Applicator;
  private boolean DefaultApplicator;
  private final CommandLineApplicator c = new CommandLineApplicator();
  private String[] clazzes;
  private String[] tipperGroups;

  CommandLineSpartanizer(final String path) {
    this(path, system.folder2File(path));
  }
  CommandLineSpartanizer(final String presentSourcePath, final String name) {
    this.presentSourcePath = presentSourcePath;
    this.name = name;
  }
  public CommandLineSpartanizer() {
    this(".");
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
      System.out.println("Reports.getOutputFolder(): " + ReportGenerator.getOutputFolder());
      ReportGenerator.initializeFile(ReportGenerator.getOutputFolder() + "/" + name + ".before.java", "before");
      ReportGenerator.initializeFile(ReportGenerator.getOutputFolder() + "/" + name + ".after.java", "after");
      ReportGenerator.initializeReport(ReportGenerator.getOutputFolder() + "/" + name + ".CSV", "metrics");
      ReportGenerator.initializeReport(ReportGenerator.getOutputFolder() + "/" + name + ".spectrum.CSV", "spectrum");
      ReportGenerator.initializeReport(ReportGenerator.getOutputFolder() + "/" + name + ".tips.CSV", "tips");
      if (DefaultApplicator) {
        c.listener(¢ -> System.out.println("ok" + ¢));
        CommandLineApplicator.defaultApplicator().defaultSelection(CommandLineSelection.Util.get(ReportGenerator.getInputFolder()))
            .defaultListenerNoisy().go();
      }
      if (Spartanizer$Applicator)
        CommandLineApplicator.defaultApplicator().defaultSelection(CommandLineSelection.Util.get(ReportGenerator.getInputFolder()))
            .defaultRunAction(new Spartanizer$Applicator()).defaultListenerNoisy().go();
      if (CommandLine$Applicator)
        CommandLineApplicator.defaultApplicator().defaultSelection(CommandLineSelection.Util.get(ReportGenerator.getInputFolder()))
            .defaultRunAction(new CommandLine$Applicator(clazzes, tipperGroups)).defaultListenerNoisy().go();
      //
      ReportGenerator.close("metrics");
      ReportGenerator.close("spectrum");
      ReportGenerator.close("tips");
      ReportGenerator.closeFile("before");
      ReportGenerator.closeFile("after");
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
  public void inputDir(final String ¢) {
    presentSourcePath = ¢;
  }
  public void name(final String ¢) {
    name = ¢;
  }
  public void setClazzes(final String[] ¢) {
    clazzes = ¢;
  }
  public void setTipperGroups(String[] ¢) {
    tipperGroups = ¢;
  }
}