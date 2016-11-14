package il.org.spartan.spartanizer.cmdline.report;

import static il.org.spartan.tide.*;

/** Generates reports
 * @author Matteo Orru'
 * @since 2016 */

import java.io.*;
import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;

/**
 * Generator for reports
 * @author Matteo Orru'
 * @since 2016
 */

public class ReportGenerator {
  protected static String outputFolder = "/tmp/";
  protected static String inputFolder;
  protected String afterFileName;
  protected String beforeFileName;
  protected String spectrumFileName;
  
//  protected static MetricsReport metric_report = new MetricsReport(); // temporarily disabled
  
  public static void main(String[] args){
    //
  }
  
  protected static HashMap<String, CSVStatistics> reports = new HashMap<>();
  protected static HashMap<String, PrintWriter> files = new HashMap<>();

  public static class Util {
    @SuppressWarnings("rawtypes") public static NamedFunction[] functions(final String id) {
      return as.array(m("length" + id, (¢) -> (¢ + "").length()), m("essence" + id, (¢) -> Essence.of(¢ + "").length()),
          m("tokens" + id, (¢) -> metrics.tokens(¢ + "")), m("nodes" + id, (¢) -> count.nodes(¢)), m("body" + id, (¢) -> metrics.bodySize(¢)),
          m("methodDeclaration" + id, (¢) -> az.methodDeclaration(¢) == null ? -1
              : extract.statements(az.methodDeclaration(¢).getBody()).size()),
          m("tide" + id, (¢) -> clean(¢ + "").length()));//
    }
    static NamedFunction<ASTNode> m(final String name, final ToInt<ASTNode> f) {
      return new NamedFunction<>(name, f);
    }
    static CSVStatistics report(final String ¢) {
      assert ¢ != null;
      return reports.get(¢);
    }
    @SuppressWarnings({ "unchecked", "rawtypes" }) public static NamedFunction<ASTNode> find(final String ¢) {
      for (final NamedFunction $ : ReportGenerator.Util.functions(""))
        if ($.name() == ¢)
          return $;
      return null;
    }
  }

  // running report
  @SuppressWarnings({ "unused", "unchecked", "rawtypes" }) public static void writeMetrics(final ASTNode n1, final ASTNode n2, final String id) {
    for (final NamedFunction ¢ : ReportGenerator.Util.functions("")) {
      ReportGenerator.Util.report("metrics").put(¢.name() + "1", ¢.function().run(n1));
      ReportGenerator.Util.report("metrics").put(¢.name() + "2", ¢.function().run(n2));
    }
  }
  public static String getOutputFolder() {
    return outputFolder;
  }
  public static void setOutputFolder(final String outputFolder) {
    ReportGenerator.outputFolder = outputFolder;
  }
  public static String getInputFolder() {
    return inputFolder;
  }
  public static void setInputFolder(final String inputFolder) {
    ReportGenerator.inputFolder = inputFolder;
  }

  @FunctionalInterface public interface BiFunction<T, R> {
    double apply(T t, R r);
  }

  @SuppressWarnings({ "boxing", "unchecked", "rawtypes" }) public static void write(final ASTNode input, final ASTNode output, final String id,
      final BiFunction<Integer, Integer> i) {
    for (final NamedFunction ¢ : ReportGenerator.Util.functions(""))
      ReportGenerator.Util.report("metrics").put(id + ¢.name(), i.apply(¢.function().run(input), ¢.function().run(output)));
  }
  @SuppressWarnings({ "boxing", "unchecked", "rawtypes" }) public static void writeDiff(final ASTNode n1, final ASTNode n2, final String id,
      final BiFunction<Integer, Integer> i) {
    for (final NamedFunction ¢ : ReportGenerator.Util.functions(""))
      ReportGenerator.Util.report("metrics").put(id + ¢.name(), (int) i.apply(¢.function().run(n1), ¢.function().run(n2)));
  }
  @SuppressWarnings({ "boxing", "unchecked", "rawtypes" }) public static void writeDelta(final ASTNode n1, final ASTNode n2, final String id,
      final BiFunction<Integer, Integer> i) {
    double a;
    for (final NamedFunction ¢ : ReportGenerator.Util.functions("")) {
      a = i.apply(¢.function().run(n1), ¢.function().run(n2)); // system.d(¢.function().run(n1),
                                                               // ¢.function().run(n2));
      ReportGenerator.Util.report("metrics").put(id + ¢.name(), a);
    }
  }
  @SuppressWarnings({ "boxing", "unchecked", "rawtypes" }) public static void writePerc(final ASTNode n1, final ASTNode n2, final String id,
      final BiFunction<Integer, Integer> i) {
    String a; // TODO Matteo: to be converted to double or float? -- Matteo
    for (final NamedFunction ¢ : ReportGenerator.Util.functions("")) {
      a = i.apply(¢.function().run(n1), ¢.function().run(n2)) + ""; // system.p(¢.function().run(n1),
                                                                    // ¢.function().run(n2));
      ReportGenerator.Util.report("metrics").put(id + ¢.name() + " %", a);
    }
  }
  @SuppressWarnings({ "unchecked", "rawtypes" }) public static void writePerc(final ASTNode n1, final ASTNode n2, final String id) {
    String a; // TODO Matteo: to be converted to double or float? -- Matteo
    for (final NamedFunction ¢ : ReportGenerator.Util.functions("")) {
      a = system.p(¢.function().run(n1), ¢.function().run(n2));
      ReportGenerator.Util.report("metrics").put(id + ¢.name() + " %", a);
    }
  }
  /** @param nm */
  @SuppressWarnings({ "unused", "boxing" }) public static void writeRatio(final ASTNode n1, final ASTNode __, final String id,
      final BiFunction<Integer, Integer> i) {
    final int len = ReportGenerator.Util.find("length").function().run(n1);
    final int ess = ReportGenerator.Util.find("essence").function().run(n1);
    final int tide = ReportGenerator.Util.find("tide").function().run(n1);
    final int body = ReportGenerator.Util.find("body").function().run(n1);
    final int nodes = ReportGenerator.Util.find("nodes").function().run(n1);
    ReportGenerator.Util.report("metrics").put("R(E/L)", i.apply(len, ess));
    ReportGenerator.Util.report("metrics").put("R(E/L)", i.apply(tide, ess));
    ReportGenerator.Util.report("metrics").put("R(E/L)", i.apply(nodes, body));
  }

  @FunctionalInterface public interface ToInt<R> {
    int run(R r);
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

  @SuppressWarnings("resource") public static void initializeFile(final String fileName, final String id) throws IOException {
    files.put(id, new PrintWriter(new FileWriter(fileName)));
  }
  public static void initializeReport(final String reportFileName, final String id) {
    try {
      reports.put(id, new CSVStatistics(reportFileName, id));
    } catch (final IOException x) {
      x.printStackTrace();
    }
  }
  private static CSVStatistics report(final String key) {
    return reports.get(key);
  }
  private static PrintWriter files(final String key) {
    return files.get(key);
  }
  public static void reportMetrics(final ASTNodeMetrics nm, final String id, final String key) {
    report(key)//
        .put("Nodes" + id, nm.nodes())//
        .put("Body" + id, nm.body())//
        .put("Length" + id, nm.length())//
        .put("Tokens" + id, nm.tokens())//
        .put("Tide" + id, nm.tide())//
        .put("Essence" + id, nm.essence())//
        .put("Statements" + id, nm.statements());//
  }
  /** @param nm1
   * @param nm2 */
  public static void reportDifferences(final ASTNodeMetrics nm1, final ASTNodeMetrics nm2, final String key) {
    report(key) //
        .put("Δ Nodes", nm1.nodes() - nm2.nodes())//
        .put("δ Nodes", system.d(nm1.nodes(), nm2.nodes()))//
        .put("δ Nodes %", system.p(nm1.nodes(), nm2.nodes()))//
        .put("Δ Body", nm1.body() - nm2.body())//
        .put("δ Body", system.d(nm1.body(), nm2.body()))//
        .put("% Body", system.p(nm1.body(), nm2.body()))//
        .put("Δ Tokens", nm1.tokens() - nm2.tokens())//
        .put("δ Tokens", system.d(nm1.tokens(), nm2.tokens()))//
        .put("% Tokens", system.p(nm1.tokens(), nm2.tokens()))//
        .put("Δ Length", nm1.length() - nm2.length())//
        .put("δ Length", system.d(nm1.length(), nm2.length()))//
        .put("% Length", system.p(nm1.length(), nm2.length()))//
        .put("Δ Tide2", nm1.tide() - nm2.tide())//
        .put("δ Tide2", system.d(nm1.tide(), nm2.tide()))//
        .put("δ Tide2", system.p(nm1.tide(), nm2.tide()))//
        .put("Δ Essence", nm1.essence() - nm2.essence())//
        .put("δ Essence", system.d(nm1.essence(), nm2.essence()))//
        .put("% Essence", system.p(nm1.essence(), nm2.essence()))//
        .put("Δ Statement", nm1.statements() - nm2.statements())//
        .put("δ Statement", system.d(nm1.statements(), nm2.statements()))//
        .put("% Statement", system.p(nm1.statements(), nm2.statements()));//
  }
  /** @param nm */
  public static void reportRatio(final ASTNodeMetrics nm, final String id, final String key) {
    report(key) //
        // .put("Words)", wordCount).put("R(T/L)", system.ratio(length, tide))
        // //
        .put("R(E/L)" + id, system.ratio(nm.length(), nm.essence())) //
        .put("R(E/T)" + id, system.ratio(nm.tide(), nm.essence())) //
        .put("R(B/S)" + id, system.ratio(nm.nodes(), nm.body())); //
  }
  public static void close(final String key) {
    report(key).close();
  }
  public static void summaryFileName(final String key) {
    report(key).summaryFileName();
  }
  public static void nl(final String key) {
    report(key).nl();
  }
  public static void printFile(final String input, final String key) {
    assert input != null;
    files(key).print(input);
  }
  public static void closeFile(final String key) {
    files(key).flush();
    files(key).close();
  }
  public static HashMap<String, CSVStatistics> reports() {
    return reports;
  }
  public static void name(final ASTNode input) {
    ReportGenerator.report("metrics").put("name", extract.name(input));
    ReportGenerator.report("metrics").put("category", extract.category(input));
  }
  public static void name(final ASTNode input, final String reportName) {
    ReportGenerator.report(reportName).put("node", extract.name(input));
    ReportGenerator.report(reportName).put("category", extract.category(input));
  }
  public static void tip(final Tip ¢) {
    ReportGenerator.report("tips").put("tipName", ¢.getClass());
    ReportGenerator.report("tips").put("description", ¢.description);
    ReportGenerator.report("tips").put("LineNumber", ¢.lineNumber);
    ReportGenerator.report("tips").put("from", ¢.from);
    ReportGenerator.report("tips").put("to", ¢.to);
    ReportGenerator.report("tips").put("tipperClass", ¢.tipperClass);
  }
  public static void writeTipsLine(final ASTNode n, final Tip t, final String reportName) {
    name(n, reportName);
    tip(t);
    ReportGenerator.report(reportName).nl();
  }

  public class LineWriter implements Consumer<Object> {
    @Override public void accept(@SuppressWarnings("unused") final Object __) {
      // erased
    }
  }

  @SuppressWarnings("unchecked") public static <T> void writeLine(final Consumer<T> ¢) {
    ¢.accept((T) ¢);
  }
  public static void generate(final String ¢) {
    initializeReport(¢ + "_metrics.CSV", ¢);
  }
}
