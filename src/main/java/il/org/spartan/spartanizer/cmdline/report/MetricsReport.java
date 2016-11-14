package il.org.spartan.spartanizer.cmdline.report;

import static il.org.spartan.tide.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.report.ConfigurableReport.Settings.*;

/** Collects a set of metrics A wrapper for {@link CSVStatistics}
 * @author Matteo Orru' */
//@SuppressWarnings("unused")
public class MetricsReport implements ConfigurableReport {
  
  List<ASTNode> l;
  
//  private static ArrayList<ASTNode> inputList = new ArrayList<>();
//  private static ArrayList<ASTNode> outputList = new ArrayList<>();
  private static Settings settings = new Settings();
  private static Action writeReport;   

  public static void initialize() {
    if(settings.getInputFolder() == null)
      settings.setInputFolder(".");
    if(settings.getOutputFolder() == null)
      settings.setOutputFolder("/tmp");
    settings.setHeader("metrics");
    settings.setFileName("metrics");
    writeReport = settings.getAction();
    writeReport.initialize();
  }
  
  public MetricsReport(){
    
  }
  
  public static Settings getSettings(){
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
      return this.name;
    }

    public ToInt<R> function() {
      return this.f;
    }
  }

  @SuppressWarnings("rawtypes") public static NamedFunction[] functions(final String id) {
    return as.array(m("length" + id, (¢) -> (¢ + "").length()), //
        m("essence" + id, (¢) -> Essence.of(¢ + "").length()), //
        m("tokens" + id, (¢) -> metrics.tokens(¢ + "")), //
        m("nodes" + id, (¢) -> count.nodes(¢)), //
        m("body" + id, (¢) -> metrics.bodySize(¢)), //
        m("methodDeclaration" + id, (¢) -> az.methodDeclaration(¢) == null ? -1 : extract.statements(az.methodDeclaration(¢).getBody()).size()), //
        m("tide" + id, (¢) -> clean(¢ + "").length())); //
  }

  static NamedFunction<ASTNode> m(final String name, final ToInt<ASTNode> f) {
    return new NamedFunction<>(name, f);
  }

  public static void write() {
    final Action wr = settings.getAction();
    wr.initialize();
    wr.go();
  }


}
