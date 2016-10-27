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
public abstract class AbstractCommandLineSpartanizer {
  
  static List<Class<? extends BodyDeclaration>> selectedNodeTypes = as.list(MethodDeclaration.class);

  static AbstractGUIApplicator getSpartanizer(final String tipperName) {
    return Tips2.get(tipperName);
  }

  protected String folder = "/tmp/";
  protected String inputPath;

  public abstract void apply();

  void fire() {
    apply();
//    reportSpectrum();
//    reportCoverage();
//    runEssence();
//    runWordCount();
  }

  // running report

  public void runReport(final Consumer<CSVStatistics> __) {
    // TODO Matteo: implement this if we need it; found in random scan
  }

  @FunctionalInterface public interface ToInt<R> {
    int f(R r);
  }

  static NamedFunction m(String name, ToInt<ASTNode> f) {
    return new NamedFunction(name, f);
  }

  static class NamedFunction {
    NamedFunction(String name, ToInt<ASTNode> f) {
      this.name = name;
      this.f = f;
    }

    final String name;
    final ToInt<ASTNode> f;
  }

  NamedFunction functions[] = as.array(//
      m("seventeeen", (¢) -> 17), //
      m("length", (¢) -> (¢+"").length()), //
      m("essence", (¢) -> Essence.of(¢+"").length()),
      m("tokens", (¢) -> metrics.tokens(¢+"")),
      m("nodes", (¢) -> count.nodes(¢)),//
      m("body", (¢) -> metrics.bodySize(¢)),
      m("methodDeclaration", (¢) -> az.methodDeclaration(¢) == null ? -1 : 
        extract.statements(az.methodDeclaration(¢).getBody()).size()),
      m("tide", (¢) -> clean(¢+"").length())                                        
      );

}