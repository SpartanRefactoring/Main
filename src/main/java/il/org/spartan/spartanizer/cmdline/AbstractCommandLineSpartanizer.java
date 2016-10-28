package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

@SuppressWarnings("unused") public abstract class AbstractCommandLineSpartanizer {
  static List<Class<? extends BodyDeclaration>> selectedNodeTypes = as.list(MethodDeclaration.class);

  static AbstractGUIApplicator getSpartanizer(final String tipperName) {
    return Tips2.get(tipperName);
  }

  protected String folder = "/tmp/";
  protected String inputPath;

  public abstract void apply();

  void fire() {
    apply();
    // reportSpectrum();
    // reportCoverage();
    // runEssence();
    // runWordCount();
  }
}