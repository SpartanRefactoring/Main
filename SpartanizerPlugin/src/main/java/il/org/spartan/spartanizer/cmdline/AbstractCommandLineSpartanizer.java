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

  // running report
  public void runReport(final Consumer<CSVStatistics> __) {
    for (NamedFunction ¢ : functions) {
      ¢.function().run(null);
      ¢.name();
    }
  }

  @FunctionalInterface public interface ToInt<R> {
    int run(R r);
  }

  static NamedFunction m(final String name, final ToInt<ASTNode> f) {
    return new NamedFunction(name, f);
  }

  static class NamedFunction {
    NamedFunction(final String name, final ToInt<ASTNode> f) {
      this.name = name;
      this.f = f;
    }

    final String name;
    final ToInt<ASTNode> f;
    
    public String name(){
      return this.name;
    }
    
    public ToInt<ASTNode> function(){
      return this.f;
    }
  }

  NamedFunction functions[] = as.array(//
      m("seventeeen", (¢) -> 17), //
      m("length", (¢) -> (¢ + "").length()), //
      m("essence", (¢) -> Essence.of(¢ + "").length()), m("tokens", (¢) -> metrics.tokens(¢ + "")), m("nodes", (¢) -> count.nodes(¢)), //
      m("body", (¢) -> metrics.bodySize(¢)),
      m("methodDeclaration", (¢) -> az.methodDeclaration(¢) == null ? -1 : extract.statements(az.methodDeclaration(¢).getBody()).size()),
      m("tide", (¢) -> clean(¢ + "").length()));
}