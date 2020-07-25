package il.org.spartan.spartanizer.cmdline.runnables;

import static il.org.spartan.tide.clean;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

import org.eclipse.jdt.core.dom.ASTNode;

import fluent.ly.as;
import il.org.spartan.CSVStatistics;
import il.org.spartan.Essence;
import il.org.spartan.spartanizer.ast.navigate.countOf;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metric;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.cmdline.library.FileHeuristics;
import il.org.spartan.spartanizer.plugin.Listener;

/** Configurable Report that uses {@link Listener.S}
 * @author Yossi Gil
 * @author Matteo Orru'
 * @since Nov 14, 2016 */
public interface ConfigurableReport {
  class Settings extends Listener.S {
    private static final long serialVersionUID = 0x28DACCB0125DA79L;
    private static String reportFileName;
    private static String header;
    private static List<ASTNode> inputList = an.empty.list();
    private static List<ASTNode> outputList = an.empty.list();

    public static void addInput(final ASTNode input) {
      getInputList().add(input);
    }
    public static void addOutput(final ASTNode output) {
      getOutputList().add(output);
    }
    public static Metric.Integral[] functions(final String id) {
      return as.array(Metric.named("length" + id).of((ToIntFunction<ASTNode>) λ -> (λ + "").length()),
          Metric.named("essence" + id).of((ToIntFunction<ASTNode>) λ -> Essence.of(λ + "").length()),
          Metric.named("tokens" + id).of((ToIntFunction<ASTNode>) λ -> Metrics.tokens(λ + "")),
          Metric.named("nodes" + id).of((ToIntFunction<ASTNode>) countOf::nodes),
          Metric.named("body" + id).of((ToIntFunction<ASTNode>) Metrics::bodySize),
          Metric.named("methodDeclaration" + id).of((ToIntFunction<ASTNode>) λ -> az.methodDeclaration(λ) == null ? -1
              : extract.statements(az.methodDeclaration(λ).getBody()).size()),
          Metric.named("tide" + id).of((ToIntFunction<ASTNode>) λ -> clean(λ + "").length()));//
    }
    public static String getFileName() {
      return reportFileName;
    }
    static String getHeader() {
      return header;
    }
    public static List<ASTNode> getInputList() {
      return inputList;
    }
    public static List<ASTNode> getOutputList() {
      return outputList;
    }
    public static void setFileName(final String ¢) {
      reportFileName = ¢;
    }
    public static void setHeader(final String ¢) {
      header = ¢;
    }
    public static void setInputList(final ArrayList<ASTNode> inputList) {
      Settings.inputList = inputList;
    }
    public static void setOutputList(final ArrayList<ASTNode> outputList) {
      Settings.outputList = outputList;
    }

    String outputFolder = "/tmp/"; // default modifier
    String inputFolder; // default modifier
    CSVStatistics report;
    private ASTNode input;
    private ASTNode output;
    private boolean robustMode;

    public Action getAction() {
      return new Action();
    }
    public ASTNode getInput() {
      return input;
    }
    public String getInputFolder() {
      return inputFolder;
    }
    public ASTNode getOutput() {
      return output;
    }
    public String getOutputFolder() {
      return outputFolder;
    }
    public boolean isRobustMode() {
      return robustMode;
    }
    public CSVStatistics report() {
      return report;
    }
    public void setInput(final ASTNode ¢) {
      input = ¢;
    }
    public void setInputFolder(final String inputFolder) {
      this.inputFolder = inputFolder;
    }
    public void setOutput(final ASTNode ¢) {
      output = ¢;
    }
    public void setOutputFolder(final String outputFolder) {
      this.outputFolder = outputFolder;
    }
    public void setReport(final String reportFilename, final String header) {
      report = new CSVStatistics(reportFilename, header);
    }
    public void setRobustMode(final boolean robustMode) {
      this.robustMode = robustMode;
    }

    /** Action provide services
     * @see #go()
     * @author Yossi Gil
     * @author Matteo Orru' */
    @SuppressWarnings("TooBroadScope")
    public class Action extends Settings {
      private static final long serialVersionUID = 0x7C6A72AC052B852FL;

      /** real serialVersionUID comes much later in production code */
      public void close() {
        report().close();
      }
      private int defaultValue() {
        return hashCode();
      }
      int go() {
        // listeners().push("Initializing the " + getFileName() + " report.");
        if (Settings.this.isRobustMode()) {
          listeners().pop("we dare do nothing in robust mode");
          return 0;
        }
        // listeners().tick("generate summary file name");
        summaryFileName();
        for (int i = 0; i < getInputList().size(); ++i) {
          // write
          // listeners().tick("writing basic data");
          name(getInputList().get(i));
          // write
          // listeners().tick("writing metrics");
          write(getInputList().get(i), getOutputList().get(i));
          // write
          // listeners().tick("writing differences");
          write(getInputList().get(i), getOutputList().get(i), "Δ ", (n1, n2) -> (n1 - n2));
          // write
          // listeners().tick("writing delta");
          write(getInputList().get(i), getOutputList().get(i), "δ ", FileHeuristics::d);
          // write
          // listeners().tick("writing perc");
          writePerc(getInputList().get(i), getOutputList().get(i), "δ ");
          // write
          // listeners().tick("writing ratio");
          // not yet
          report.nl();
        }
        // listeners().pop("exhausted");
        return defaultValue();
      }
      public void initialize() {
        report = new CSVStatistics(getFileName(), getHeader());
      }
      private void name(final ASTNode i) {
        report().put("name", extract.name(i));
        report().put("category", extract.category(i));
      }
      private void summaryFileName() {
        report().summaryFileName();
      }
      // running report
      private void write(final ASTNode i, final ASTNode n) {
        for (final Metric.Integral ¢ : ReportGenerator.metrics) {
          report().put(¢.name + "1", ¢.apply(i));
          report().put(¢.name + "2", ¢.apply(n));
        }
      }
      private void write(final ASTNode i, final ASTNode n, final String id, final ToDoubleFromTwoIntegers bf) {
        if (bf == null && id == null) {
          write(i, n);
          return;
        }
        assert bf != null;
        assert id != null;
        as.list(ReportGenerator.metrics).forEach(λ -> report().put(id + λ.name, bf.apply(λ.apply(i), λ.apply(n))));
      }
      private void writePerc(final ASTNode n1, final ASTNode n2, final String id) {
        for (final Metric.Integral ¢ : ReportGenerator.integralMetrics())
          report().put(id + ¢.name + " %", FileHeuristics.p(¢.apply(n1), ¢.apply(n2)));
      }
    }
  }
}
