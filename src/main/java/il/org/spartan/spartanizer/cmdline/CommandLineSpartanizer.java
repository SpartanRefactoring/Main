package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.*;

import il.org.spartan.plugin.*;

/** A configurable version of the GUIBatchLaconizer that relies on
 * {@link CommandLineApplicator} and {@link CommandLineSelection}
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLineSpartanizer extends AbstractSpartanizer {
  private CommandLineSelection selection;
  // private final boolean shouldRun = false;
  private final boolean applyToEntireProject = false;
  private final boolean entireProject = true;
  private final boolean specificTipper = false;

  public static void main(final String[] args) {
    for (final String ¢ : args.length != 0 ? args : new String[] { "." })
      new CommandLineSpartanizer(¢).fire();
  }

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
  }

  @Override public void apply() {
    if (applyToEntireProject) {
      selection = new CommandLineSelection(new ArrayList<WrappedCompilationUnit>(), "project");
      selection.createSelectionFromProjectDir(inputPath);
    }
    if (entireProject)
      CommandLineApplicator.defaultApplicator().defaultRunAction();
    if (specificTipper)
      CommandLineApplicator.defaultApplicator();
  }
}
