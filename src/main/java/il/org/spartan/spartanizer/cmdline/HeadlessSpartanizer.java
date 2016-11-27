package il.org.spartan.spartanizer.cmdline;

import java.io.*;

import il.org.spartan.external.*;
import il.org.spartan.spartanizer.cmdline.report.*;

/** A configurable version of the HeadlessSpartanizer that relies on
 * {@link CommandLineApplicator} and {@link CommandLineSelection}
 * @author Matteo Orru'
 * @since 2016 */
public class HeadlessSpartanizer extends AbstractCommandLineProcessor {
  @External(alias = "np", value = "Nano Patterns") private static String[] nanoPatterns;
  @External final CommandLineApplicator c = new CommandLineApplicator();
  @External final boolean CommandLine$Applicator = true;
  @External boolean DefaultApplicator;
  @External String name;
  @External boolean selection;
  @External boolean Spartanizer$Applicator;
  @External(alias = "cs", value = "class name on which apply the tippers") String[] clazzes;
  @External(alias = "allnp", value = "Exclude All Nano Patterns") boolean excludeAllNanoPatterns;
  @External(alias = "enp", value = "Exclude Selected Nano Patterns") String[] excludeNanoPatterns;
  @External(alias = "etg", value = "exclude one or more tipper groups") String[] excludeTipperGroups;
  @External(alias = "tg", value = "tipper group to be applied to the clazzes") String[] tipperGroups;

  public HeadlessSpartanizer() {
    this(".");
  }

  HeadlessSpartanizer(final String path) {
    this(path, system.folder2File(path));
  }

  HeadlessSpartanizer(final String presentSourcePath, final String name) {
    inputFolder = presentSourcePath;
    this.name = name;
  }

  @Override public void apply() {
    try {
      ReportGenerator.initializeFile(ReportGenerator.getOutputFolder() + "/" + name + ".before.java", "before");
      ReportGenerator.initializeFile(ReportGenerator.getOutputFolder() + "/" + name + ".after.java", "after");
      ReportGenerator.initializeReport(ReportGenerator.getOutputFolder() + "/" + name + ".CSV", "metrics");
      ReportGenerator.initializeReport(ReportGenerator.getOutputFolder() + "/" + name + ".spectrum.CSV", "spectrum");
      ReportGenerator.initializeReport(ReportGenerator.getOutputFolder() + "/" + name + ".tips.CSV", "tips");
      if (DefaultApplicator) {
        c.listener(¢ -> System.out.println("Running DefaultApplicator ...." + ¢));
        CommandLineApplicator.defaultApplicator().defaultSelection(CommandLineSelection.Util.get(ReportGenerator.getInputFolder()))
            .defaultListenerNoisy().go();
      }
      //
      if (Spartanizer$Applicator)
        CommandLineApplicator.defaultApplicator().defaultSelection(CommandLineSelection.Util.get(ReportGenerator.getInputFolder()))
            .defaultRunAction(new Spartanizer$Applicator()).defaultListenerNoisy().go();
      //
      if (CommandLine$Applicator)
        CommandLineApplicator.defaultApplicator().defaultSelection(CommandLineSelection.Util.get(ReportGenerator.getInputFolder()))
            .defaultRunAction(new CommandLine$Applicator(clazzes, //
                tipperGroups, //
                excludeTipperGroups, //
                excludeNanoPatterns))
            .defaultListenerNoisy().go();
      //
      ReportGenerator.close("metrics");
      ReportGenerator.close("spectrum");
      ReportGenerator.close("tips");
      ReportGenerator.closeFile("before");
      ReportGenerator.closeFile("after");
      //
      System.err.println("commandLineApplicator: " + "Done!");
      if (selection)
        CommandLineApplicator.defaultApplicator().defaultListenerNoisy()
            .defaultSelection(CommandLineSelection.of(CommandLineSelection.Util.getAllCompilationUnits(inputFolder)))
            .defaultRunAction(new CommandLine$Applicator()).go();
    } catch (final IOException ¢) {
      ¢.printStackTrace();
    }
  }

  public void name(final String ¢) {
    name = ¢;
  }
}
