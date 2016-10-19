package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.cmdline.system.*;
import static il.org.spartan.tide.*;

import java.io.*;
import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** A configurable version of the Spartanizer that relies on
 * {@link CommandLineApplicator} and {@link CommandLineSelection}
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLineSpartanizer extends AbstractSpartanizer {
  
  private CommandLineSelection selection;
  
//  private final boolean shouldRun = false;
  private final boolean applyToEntireProject = true;
  private final boolean runApplicator = true;
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
  }

  @Override public void apply() {
    if (applyToEntireProject) {
      selection = new CommandLineSelection(new ArrayList<WrappedCompilationUnit>(), "project");
      selection.createSelectionFromProjectDir(inputPath);
    }
    
    if (runApplicator) {
      if (entireProject)
        CommandLineApplicator.defaultApplicator().defaultRunAction();
      // .selection(CommandLineSelection.Util.getAllCompilationUnits()
      // .buildAll())
      // .go();
      if (specificTipper)
        CommandLineApplicator.defaultApplicator();
      // .defaultRunAction(getSpartanizer(""));
    }
  }
}
