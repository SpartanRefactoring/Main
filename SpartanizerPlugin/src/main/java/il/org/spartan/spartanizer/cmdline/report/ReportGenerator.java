package il.org.spartan.spartanizer.cmdline.report;

import static il.org.spartan.tide.*;

/* Generates reports
 *
 * @author Matteo Orru'
 *
 * @since 2016 */

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.navigate.count;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.Utils;
import il.org.spartan.spartanizer.cmdline.applicator.*;
import il.org.spartan.spartanizer.engine.*;

/** Generator for reports
 * @author Matteo Orru'
 * @since 2016 */
public class ReportGenerator implements ConfigurableReport {
  protected static String outputFolder = "/tmp/";
  protected static String inputFolder;
  protected String afterFileName;
  protected String beforeFileName;
  protected String spectrumFileName;
  protected static final HashMap<String, CSVStatistics> reports = new HashMap<>();
  protected static final Map<String, PrintWriter> files = new HashMap<>();
  @SuppressWarnings("rawtypes") protected static final HashMap<String, NamedFunction[]> metricsMap = Util.initialize();

  @SuppressWarnings("rawtypes") public static HashMap<String, NamedFunction[]> metricsMap() {
    return metricsMap;
  }

  public enum Util {
    DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
    @SuppressWarnings("rawtypes") public static NamedFunction[] functions(final String id) {
      return as.array(m("length" + id, λ -> (λ + "").length()), m("essence" + id, λ -> Essence.of(λ + "").length()),
          m("tokens" + id, λ -> metrics.tokens(λ + "")), m("nodes" + id, count::nodes), m("body" + id, metrics::bodySize),
          m("methodDeclaration" + id, λ -> az.methodDeclaration(λ) == null ? -1 : extract.statements(az.methodDeclaration(λ).getBody()).size()),
          m("tide" + id, λ -> clean(λ + "").length()));//
    }

    @SuppressWarnings("rawtypes") public static HashMap<String, NamedFunction[]> initialize() {
      final HashMap<String, NamedFunction[]> $ = new HashMap<>();
      $.put("metrics", functions(""));
      $.put("methods",
          as.array(m("N. of Nodes", count::nodes), //
              m("Average Depth", λ -> -1000), // (¢) -> Essence.of(¢ +
                                              // "").length()), //
              m("Average Uncle Depth", λ -> -1000), // (¢) -> Essence.of(¢ +
                                                    // "").length()), //
              m("Character Length", λ -> -1000) // Essence.of(¢ +
                                                // "").length()) //
          // Report Halstead Metrics
          )); //
      return $;
    }

    static NamedFunction<ASTNode> m(final String name, final ToInt<ASTNode> f) {
      return new NamedFunction<>(name, f);
    }

    static CSVStatistics report(final String ¢) {
      assert ¢ != null;
      return reports.get(¢);
    }

    @SuppressWarnings("unchecked") public static NamedFunction<ASTNode> find(final String ¢) {
      return Stream.of(ReportGenerator.Util.functions("")).filter(λ -> Objects.equals(λ.name(), ¢)).findFirst().orElse(null);
    }
  }

  // running report
  @SuppressWarnings({ "unchecked", "rawtypes" }) public static void writeMetrics(final ASTNode n1, final ASTNode n2, final String id) {
    for (final NamedFunction ¢ : ReportGenerator.Util.functions("")) {
      ReportGenerator.Util.report(id).put(¢.name() + "1", ¢.function().run(n1));
      ReportGenerator.Util.report(id).put(¢.name() + "2", ¢.function().run(n2));
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

  @FunctionalInterface
  public interface BiFunction<T, R> {
    double apply(T t, R r);
  }

  @SuppressWarnings({ "boxing", "unchecked" }) public static void write(final ASTNode input, final ASTNode output, final String id,
      final BiFunction<Integer, Integer> i) {
    as.list(ReportGenerator.Util.functions(""))
        .forEach(λ -> ReportGenerator.Util.report("metrics").put(id + λ.name(), i.apply(λ.function().run(input), λ.function().run(output))));
  }

  @SuppressWarnings({ "boxing", "unchecked" }) public static void writeDiff(final ASTNode n1, final ASTNode n2, final String id,
      final BiFunction<Integer, Integer> i) {
    as.list(ReportGenerator.Util.functions(""))
        .forEach(λ -> ReportGenerator.Util.report("metrics").put(id + λ.name(), (int) i.apply(λ.function().run(n1), λ.function().run(n2))));
  }

  @SuppressWarnings({ "boxing", "unchecked", "rawtypes" }) public static void writeDelta(final ASTNode n1, final ASTNode n2, final String id,
      final BiFunction<Integer, Integer> i) {
    for (final NamedFunction ¢ : Util.functions(""))
      ReportGenerator.Util.report("metrics").put(id + ¢.name(), i.apply(¢.function().run(n1), ¢.function().run(n2)));
  }

  @SuppressWarnings({ "boxing", "unchecked", "rawtypes" }) public static void writePerc(final ASTNode n1, final ASTNode n2, final String id,
      final BiFunction<Integer, Integer> i) {
    for (final NamedFunction ¢ : Util.functions(""))
      Util.report("metrics").put(id + ¢.name() + " %", i.apply(¢.function().run(n1), ¢.function().run(n2)) + "");
  }

  @SuppressWarnings({ "unchecked", "rawtypes" }) public static void writePerc(final ASTNode n1, final ASTNode n2, final String id) {
    for (final NamedFunction ¢ : Util.functions(""))
      Util.report("metrics").put(id + ¢.name() + " %", Utils.p(¢.function().run(n1), ¢.function().run(n2)));
  }

  @SuppressWarnings({ "unused", "boxing" }) public static void writeRatio(final ASTNode n1, final ASTNode __, final String id,
      final BiFunction<Integer, Integer> i) {
    final int ess = Util.find("essence").function().run(n1), tide = Util.find("tide").function().run(n1), body = Util.find("body").function().run(n1),
        nodes = Util.find("nodes").function().run(n1);
    Util.report("metrics").put("R(E/L)", i.apply(Util.find("length").function().run(n1), ess));
    Util.report("metrics").put("R(E/L)", i.apply(tide, ess));
    Util.report("metrics").put("R(E/L)", i.apply(nodes, body));
  }

  @FunctionalInterface
  public interface ToInt<R> {
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
      return name;
    }

    public ToInt<R> function() {
      return f;
    }
  }

  @SuppressWarnings("resource") public static void initializeFile(final String fileName, final String id) throws IOException {
    files.put(id, new PrintWriter(new FileWriter(fileName)));
  }

  public static void initializeReport(final String reportFileName, final String id) {
    try {
      reports.put(id, new CSVStatistics(reportFileName, id));
    } catch (final IOException ¢) {
      note.io(¢, id);
    }
  }

  public static CSVStatistics report(final String key) {
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

  public static void reportDifferences(final ASTNodeMetrics nm1, final ASTNodeMetrics nm2, final String key) {
    report(key) //
        .put("Δ Nodes", nm1.nodes() - nm2.nodes())//
        .put("δ Nodes", Utils.d(nm1.nodes(), nm2.nodes()))//
        .put("δ Nodes %", Utils.p(nm1.nodes(), nm2.nodes()))//
        .put("Δ Body", nm1.body() - nm2.body())//
        .put("δ Body", Utils.d(nm1.body(), nm2.body()))//
        .put("% Body", Utils.p(nm1.body(), nm2.body()))//
        .put("Δ Tokens", nm1.tokens() - nm2.tokens())//
        .put("δ Tokens", Utils.d(nm1.tokens(), nm2.tokens()))//
        .put("% Tokens", Utils.p(nm1.tokens(), nm2.tokens()))//
        .put("Δ Length", nm1.length() - nm2.length())//
        .put("δ Length", Utils.d(nm1.length(), nm2.length()))//
        .put("% Length", Utils.p(nm1.length(), nm2.length()))//
        .put("Δ Tide2", nm1.tide() - nm2.tide())//
        .put("δ Tide2", Utils.d(nm1.tide(), nm2.tide()))//
        .put("δ Tide2", Utils.p(nm1.tide(), nm2.tide()))//
        .put("Δ Essence", nm1.essence() - nm2.essence())//
        .put("δ Essence", Utils.d(nm1.essence(), nm2.essence()))//
        .put("% Essence", Utils.p(nm1.essence(), nm2.essence()))//
        .put("Δ Statement", nm1.statements() - nm2.statements())//
        .put("δ Statement", Utils.d(nm1.statements(), nm2.statements()))//
        .put("% Statement", Utils.p(nm1.statements(), nm2.statements()));//
  }

  public static void reportRatio(final ASTNodeMetrics nm, final String id, final String key) {
    report(key) //
        // .put("Words)", wordCount).put("R(T/L)", system.ratio(length, tide))
        // //
        .put("R(E/L)" + id, Utils.ratio(nm.length(), nm.essence())) //
        .put("R(E/T)" + id, Utils.ratio(nm.tide(), nm.essence())) //
        .put("R(B/S)" + id, Utils.ratio(nm.nodes(), nm.body())); //
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
    report("metrics").put("name", extract.name(input));
    report("metrics").put("category", extract.category(input));
  }

  public static void name(final ASTNode input, final String reportName) {
    report(reportName).put("node", extract.name(input));
    report(reportName).put("category", extract.category(input));
  }

  public static void tip(final Tip ¢) {
    report("tips").put("FileName", CommandLine$Applicator.presentFileName);
    report("tips").put("Path", CommandLine$Applicator.presentFilePath);
    report("tips").put("tipName", ¢.getClass());
    // report("tips").put("description", ¢.description);
    report("tips").put("LineNumber", ¢.lineNumber);
    report("tips").put("from", ¢.highlight.from);
    report("tips").put("to", ¢.highlight.to);
    report("tips").put("Ranger-from", ¢.span.from);
    report("tips").put("Ranger-to", ¢.span.to);
    // report("tips").put("tipperClass", ¢.tipperClass);
    final long time = new Date().getTime();
    report("tips").put("time", time);
    report("tips").put("startTimeDiff", time - CommandLine$Applicator.startingTime);
    report("tips").put("startTimeDiffPerFile", time - CommandLine$Applicator.startingTimePerFile);
    report("tips").put("lastTimeDiff", time - CommandLine$Applicator.lastTime);
  }

  public static void writeTipsLine(@SuppressWarnings("unused") final ASTNode __, final Tip t, final String reportName) {
    // name(n, reportName);
    tip(t);
    report(reportName).nl();
  }

  public static class LineWriter implements Consumer<Object> {
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

  public static void emptyTipsLine() {
    report("tips").put("tipName", "");
    // report("tips").put("description", ¢.description);
    report("tips").put("LineNumber", "");
    report("tips").put("from", "");
    report("tips").put("to", "");
    // report("tips").put("tipperClass", ¢.tipperClass);
    // long time = new Date().getTime();
    report("tips").put("time", "");
    report("tips").put("startTimeDiff", "");
    report("tips").put("startTimeDiffPerFile", "");
    report("tips").put("lastTimeDiff", "");
  }

  @SuppressWarnings({ "rawtypes", "unchecked" }) public static void writeMethodMetrics(final ASTNode input, final ASTNode output, final String id) {
    for (final NamedFunction ¢ : metricsMap().get(id)) {
      Util.report(id).put(¢.name() + "1", ¢.function().run(input));
      Util.report(id).put(¢.name() + "2", ¢.function().run(output));
    }
  }
}
