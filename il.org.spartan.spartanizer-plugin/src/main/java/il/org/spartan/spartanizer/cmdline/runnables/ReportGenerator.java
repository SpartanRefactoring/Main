package il.org.spartan.spartanizer.cmdline.runnables;

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
import il.org.spartan.spartanizer.ast.nodes.metrics.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.applicators.*;
import il.org.spartan.spartanizer.cmdline.library.FileHeuristics;
import il.org.spartan.spartanizer.cmdline.metrics.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Generator for reports
 * @author Matteo Orru'
 * @since 2016 */
public class ReportGenerator implements ConfigurableReport {
  public static final Metric.Integral[] metrics = as.array(Metric.named("length").of((ToIntFunction<ASTNode>) λ -> (λ + "").length()),
      Metric.named("essence").of((ToIntFunction<ASTNode>) λ -> Essence.of(λ + "").length()),
      Metric.named("tokens").of((ToIntFunction<ASTNode>) λ -> Metrics.tokens(λ + "")),
      Metric.named("nodes").of((ToIntFunction<ASTNode>) countOf::nodes), Metric.named("body").of((ToIntFunction<ASTNode>) Metrics::bodySize),
      Metric.named("statements").of((ToIntFunction<ASTNode>) λ -> az.methodDeclaration(λ) == null ? -1
          : extract.statements(az.methodDeclaration(λ).getBody()).size()),
      Metric.named("tide").of((ToIntFunction<ASTNode>) λ -> clean(λ + "").length()));
  protected static final Map<String, PrintWriter> files = new HashMap<>();
  protected static String inputFolder;
  protected static String outputFolder = "/tmp/";

  protected static final HashMap<String, CSVStatistics> reports = new HashMap<>();
  public static void close(final String key) {
    report(key).close();
  }
  public static void closeFile(final String key) {
    files(key).flush();
    files(key).close();
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
  public static void generate(final String ¢) {
    initializeReport(¢ + "_metrics.CSV", ¢);
  }
  public static String getInputFolder() {
    return inputFolder;
  }
  public static String getOutputFolder() {
    return outputFolder;
  }
  @SuppressWarnings("resource") public static void initializeFile(final String fileName, final String id) throws IOException {
    files.put(id, new PrintWriter(new FileWriter(fileName)));
  }
  public static void initializeReport(final String reportFileName, final String id) {
    reports.put(id, new CSVStatistics(reportFileName, id));
  }
  public static HashMap<String, Metric.Integral[]> metricsMap() {
    return Util.initialize();
  }
  public static void name(final ASTNode input) {
    report("metrics").put("name", extract.name(input));
    report("metrics").put("category", extract.category(input));
  }
  public static void name(final ASTNode input, final String reportName) {
    report(reportName).put("node", extract.name(input));
    report(reportName).put("category", extract.category(input));
  }
  public static void nl(final String key) {
    report(key).nl();
  }
  public static void printFile(final String input, final String key) {
    assert input != null;
    files(key).print(input);
  }
  public static CSVStatistics report(final String key) {
    return reports.get(key);
  }
  public static void reportDifferences(final ASTNodeMetrics nm1, final ASTNodeMetrics nm2, final String key) {
    report(key) //
        .put("Δ Nodes", nm1.nodes() - nm2.nodes())//
        .put("δ Nodes", FileHeuristics.d(nm1.nodes(), nm2.nodes()))//
        .put("δ Nodes %", FileHeuristics.p(nm1.nodes(), nm2.nodes()))//
        .put("Δ Body", nm1.body() - nm2.body())//
        .put("δ Body", FileHeuristics.d(nm1.body(), nm2.body()))//
        .put("% Body", FileHeuristics.p(nm1.body(), nm2.body()))//
        .put("Δ Tokens", nm1.tokens() - nm2.tokens())//
        .put("δ Tokens", FileHeuristics.d(nm1.tokens(), nm2.tokens()))//
        .put("% Tokens", FileHeuristics.p(nm1.tokens(), nm2.tokens()))//
        .put("Δ Length", nm1.length() - nm2.length())//
        .put("δ Length", FileHeuristics.d(nm1.length(), nm2.length()))//
        .put("% Length", FileHeuristics.p(nm1.length(), nm2.length()))//
        .put("Δ Tide2", nm1.tide() - nm2.tide())//
        .put("δ Tide2", FileHeuristics.d(nm1.tide(), nm2.tide()))//
        .put("δ Tide2", FileHeuristics.p(nm1.tide(), nm2.tide()))//
        .put("Δ Essence", nm1.essence() - nm2.essence())//
        .put("δ Essence", FileHeuristics.d(nm1.essence(), nm2.essence()))//
        .put("% Essence", FileHeuristics.p(nm1.essence(), nm2.essence()))//
        .put("Δ Statement", nm1.statements() - nm2.statements())//
        .put("δ Statement", FileHeuristics.d(nm1.statements(), nm2.statements()))//
        .put("% Statement", FileHeuristics.p(nm1.statements(), nm2.statements()));//
  }
  public static void reportMetrics(final ASTNodeMetrics nm, final String key) {
    report(key)//
        .put("Nodes", nm.nodes())//
        .put("Body", nm.body())//
        .put("Length", nm.length())//
        .put("Tokens", nm.tokens())//
        .put("Tide", nm.tide())//
        .put("Essence", nm.essence())//
        .put("Statements", nm.statements());//
  }
  public static void reportRatio(final ASTNodeMetrics nm, final String key) {
    report(key) //
        // .put("Words)", wordCount).put("R(T/L)", system.ratio(length, tide))
        // //
        .put("R(E/L)", FileHeuristics.ratio(nm.length(), nm.essence())) //
        .put("R(E/T)", FileHeuristics.ratio(nm.tide(), nm.essence())) //
        .put("R(B/S)", FileHeuristics.ratio(nm.nodes(), nm.body())); //
  }
  public static HashMap<String, CSVStatistics> reports() {
    return reports;
  }
  public static void setInputFolder(final String inputFolder) {
    ReportGenerator.inputFolder = inputFolder;
  }
  public static void setOutputFolder(final String outputFolder) {
    ReportGenerator.outputFolder = outputFolder;
  }
  public static void summaryFileName(final String key) {
    report(key).summaryFileName();
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
  public static void write(final ASTNode input, final ASTNode output, final String id,
      final ToDoubleFromTwoIntegers i) {
    as.list(ReportGenerator.metrics)
        .forEach(λ -> ReportGenerator.Util.report("metrics").put(id + λ.name, i.apply(λ.apply(input), λ.apply(output))));
  }
  public static void writeDelta(final ASTNode n1, final ASTNode n2, final String id,
      final ToDoubleFromTwoIntegers i) {
    for (final Metric.Integral ¢ : ReportGenerator.metrics)
      ReportGenerator.Util.report("metrics").put(id + ¢.name, i.apply(¢.apply(n1), ¢.apply(n2)));
  }
  public static void writeDiff(final ASTNode n1, final ASTNode n2, final String id,
      final ToDoubleFromTwoIntegers i) {
    as.list(ReportGenerator.metrics).forEach(λ -> ReportGenerator.Util.report("metrics").put(id + λ.name, (int) i.apply(λ.apply(n1), λ.apply(n2))));
  }
  @SuppressWarnings("unchecked") public static <T> void writeLine(final Consumer<T> ¢) {
    ¢.accept((T) ¢);
  }
  public static void writeMethodMetrics(final ASTNode input, final ASTNode output, final String id) {
    for (final Metric.Integral ¢ : metricsMap().get(id)) {
      Util.report(id).put(¢.name + "1", ¢.apply(input));
      Util.report(id).put(¢.name + "2", ¢.apply(output));
    }
  }
  // running report
  public static void writeMetrics(final ASTNode n1, final ASTNode n2, final String id) {
    for (final Metric.Integral ¢ : ReportGenerator.metrics) {
      ReportGenerator.Util.report(id).put(¢.name + "1", ¢.apply(n1));
      ReportGenerator.Util.report(id).put(¢.name + "2", ¢.apply(n2));
    }
  }
  @SuppressWarnings({ }) public static void writePerc(final ASTNode n1, final ASTNode n2, final String id) {
    for (final Metric.Integral ¢ : ReportGenerator.metrics)
      Util.report("metrics").put(id + ¢.name + " %", FileHeuristics.p(¢.apply(n1), ¢.apply(n2)));
  }
  public static void writePerc(final ASTNode n1, final ASTNode n2, final String id,
      final ToDoubleFromTwoIntegers i) {
    for (final Metric.Integral ¢ : ReportGenerator.metrics)
      Util.report("metrics").put(id + ¢.name + " %", i.apply(¢.apply(n1), ¢.apply(n2)) + "");
  }
  public static void writeRatio(final ASTNode n1, @SuppressWarnings("unused") final ASTNode __, final String id,
      final ToDoubleFromTwoIntegers i) {
    final int ess = Util.find("essence").apply(n1), tide = Util.find("tide").apply(n1), body = Util.find("body").apply(n1),
        nodes = Util.find("nodes").apply(n1);
    Util.report("metrics").put("R(E/L)", i.apply(Util.find("length").apply(n1), ess));
    Util.report("metrics").put("R(E/L)", i.apply(tide, ess));
    Util.report("metrics").put("R(E/L)", i.apply(nodes, body));
  }
  public static void writeTipsLine(@SuppressWarnings("unused") final ASTNode __, final Tip t, final String reportName) {
    // name(n, reportName);
    tip(t);
    report(reportName).nl();
  }

  private static PrintWriter files(final String key) {
    return files.get(key);
  }
  protected String afterFileName;
  protected String beforeFileName;
  protected String spectrumFileName;

  public static class LineWriter implements Consumer<Object> {
    @Override public void accept(@SuppressWarnings("unused") final Object __) {
      // erased
    }
  }

  public enum Util {
    DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
    public static Metric.Integral find(final String ¢) {
      return Stream.of(ReportGenerator.metrics).filter(λ -> Objects.equals(λ.name, ¢)).findFirst().orElse(null);
    }
    public static HashMap<String, Metric.Integral[]> initialize() {
      final HashMap<String, Metric.Integral[]> $ = new HashMap<>();
      $.put("metrics", ReportGenerator.metrics);
      $.put("methods",
          as.array(Metric.named("No of Nodes").of((ToIntFunction<ASTNode>) countOf::nodes), //
              Metric.named("Average Depth").of((ToIntFunction<ASTNode>) λ -> -1000), // (¢)
                                                                                     // ->
                                                                                     // Essence.of(¢
                                                                                     // +
              // "").length()), //
              Metric.named("Average Uncle Depth").of((ToIntFunction<ASTNode>) λ -> -1000), // (¢)
                                                                                           // ->
              // Essence.of(¢ +
              // "").length()), //
              Metric.named("Character Length").of((ToIntFunction<ASTNode>) λ -> -1000) // Essence.of(¢
                                                                                       // +
          // "").length()) //
          // Report Halstead Metrics
          )); //
      return $;
    }
    static CSVStatistics report(final String ¢) {
      assert ¢ != null;
      return reports.get(¢);
    }
  }

  public static Metric.Integral[] integralMetrics() {
    return metrics;
  }
}
