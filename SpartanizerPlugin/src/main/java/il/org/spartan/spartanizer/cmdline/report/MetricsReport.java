package il.org.spartan.spartanizer.cmdline.report;

import static il.org.spartan.tide.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.navigate.countOf;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.report.ConfigurableReport.Settings.*;

/** Collects a set of metrics A wrapper for {@link CSVStatistics}
 * @author Matteo Orru'
 * @since Nov 14, 2016 */
// @SuppressWarnings("unused")
public class MetricsReport implements ConfigurableReport {
  List<ASTNode> l;
  private static final Settings settings = new Settings();

  public static void initialize() {
    if (settings.getInputFolder() == null)
      settings.setInputFolder(".");
    if (settings.getOutputFolder() == null)
      settings.setOutputFolder("/tmp");
    getSettings();
    Settings.setHeader("NEWmetrics");
    getSettings();
    Settings.setFileName("/tmp/NEWmetrics.CSV");
    settings.getAction().initialize();
  }
  public static Settings getSettings() {
    return settings;
  }

  @FunctionalInterface
  public interface ToInt<R> {
    int run(R r);
  }

  @FunctionalInterface
  public interface BiFunction<T, R> {
    double apply(T t, R r);
  }

  static class NamedFunction<R> {
    final String name;
    final ToInt<R> f;

    NamedFunction(final String name, final ToInt<R> f) {
      this.name = name;
      this.f = f;
    }
    public String name() {
      return name;
    }
    public ToInt<R> function() {
      return f;
    }
  }

  @SuppressWarnings("rawtypes") public static NamedFunction[] functions(final String id) {
    return as.array(m("length" + id, λ -> (λ + "").length()), //
        m("essence" + id, λ -> Essence.of(λ + "").length()), //
        m("tokens" + id, λ -> metrics.tokens(λ + "")), //
        m("nodes" + id, countOf::nodes), //
        m("body" + id, metrics::bodySize), //
        m("methodDeclaration" + id, λ -> az.methodDeclaration(λ) == null ? -1 : extract.statements(az.methodDeclaration(λ).getBody()).size()), //
        m("tide" + id, λ -> clean(λ + "").length())); //
  }
  static NamedFunction<ASTNode> m(final String name, final ToInt<ASTNode> f) {
    return new NamedFunction<>(name, f);
  }
  public static void write() {
    final Action wr = settings.getAction();
    wr.initialize();
    wr.go();
  }
  public static void generate() {
    write();
  }
}
