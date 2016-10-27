package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.cmdline.system.*;
import static il.org.spartan.tide.*;

import java.io.*;
import java.util.*;
import java.util.Map.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

@SuppressWarnings("unused") 
public abstract class AbstractCommandLineSpartanizer2 {
  
  static List<Class<? extends BodyDeclaration>> selectedNodeTypes = as.list(MethodDeclaration.class);

  static AbstractGUIApplicator getSpartanizer(final String tipperName) {
    return Tips2.get(tipperName);
  }

  protected String folder = "/tmp/";
  protected String inputPath;


  public abstract void apply();

  void fire() {
    run();
//    reportSpectrum();
//    reportCoverage();
//    runEssence();
//    runWordCount();
  }


  private void run() {
     apply();
  }

  public void runReport(final Consumer<CSVStatistics> __) {
    // TODO Matteo: implement this if we need it; found in random scan
  }

  @FunctionalInterface public interface ToInt<R> {
    int f(R r);
  }

  static NamedFunction m(String name, ToInt<String> f) {
    return new NamedFunction(name, f);
  }

  static class NamedFunction {
    NamedFunction(String name, ToInt<String> f) {
      this.name = name;
      this.f = f;
    }

    final String name;
    final ToInt<String> f;
  }

  NamedFunction functions[] = as.array(//
      m("seventeeen", (¢) -> 17), //
      m("length", (¢) -> ¢.length()), //
      m("essence", (¢) -> Essence.of(¢).length())//
      );
}