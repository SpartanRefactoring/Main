package il.org.spartan.spartanizer.cmdline.report;

import static il.org.spartan.tide.*;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.Utils;
import il.org.spartan.spartanizer.cmdline.report.ReportGenerator.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Configurable Report that uses {@link Listener.S}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @author Matteo Orru'
 * @since Nov 14, 2016 */
public interface ConfigurableReport {
  class Settings extends Listener.S {
    private static final long serialVersionUID = 1L;
    private static String reportFileName;
    private static String header;
    private static ArrayList<ASTNode> inputList = new ArrayList<>();
    private static ArrayList<ASTNode> outputList = new ArrayList<>();

    public static void addInput(final ASTNode input) {
      getInputList().add(input);
    }

    public static void addOutput(final ASTNode output) {
      getOutputList().add(output);
    }

    @SuppressWarnings("rawtypes") public static NamedFunction[] functions(final String id) {
      return as.array(m("length" + id, λ -> (λ + "").length()), m("essence" + id, λ -> Essence.of(λ + "").length()),
          m("tokens" + id, λ -> metrics.tokens(λ + "")), m("nodes" + id, count::nodes), m("body" + id, metrics::bodySize),
          m("methodDeclaration" + id, λ -> az.methodDeclaration(λ) == null ? -1 : extract.statements(az.methodDeclaration(λ).getBody()).size()),
          m("tide" + id, λ -> clean(λ + "").length()));//
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

    @NotNull static NamedFunction<ASTNode> m(final String name, final ToInt<ASTNode> f) {
      return new NamedFunction<>(name, f);
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

    @NotNull public Action getAction() {
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

    public void setReport(@NotNull final String reportFilename, final String header) {
      try {
        report = new CSVStatistics(reportFilename, header);
      } catch (@NotNull final IOException ¢) {
        monitor.infoIOException(¢, header);
      }
    }

    public void setRobustMode(final boolean robustMode) {
      this.robustMode = robustMode;
    }

    /** Action provide services
     * @see #go()
     * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
     * @author Matteo Orru' */
    @SuppressWarnings("TooBroadScope")
    public class Action extends Settings {
      /** real serialVersionUID comes much later in production code */
      private static final long serialVersionUID = 1L;

      public void close() {
        report().close();
      }

      private int defaultValue() {
        return hashCode();
      }

      @SuppressWarnings("boxing") int go() {
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
          write(getInputList().get(i), getOutputList().get(i), "δ ", Utils::d);
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
        try {
          report = new CSVStatistics(getFileName(), getHeader());
        } catch (@NotNull final IOException ¢) {
          monitor.infoIOException(¢);
        }
      }

      private void name(@NotNull final ASTNode i) {
        report().put("name", extract.name(i));
        report().put("category", extract.category(i));
      }

      private void summaryFileName() {
        report().summaryFileName();
      }

      // running report
      @SuppressWarnings({ "unchecked", "rawtypes" }) private void write(final ASTNode i, final ASTNode n) {
        for (@NotNull final NamedFunction ¢ : ReportGenerator.Util.functions("")) {
          report().put(¢.name() + "1", ¢.function().run(i));
          report().put(¢.name() + "2", ¢.function().run(n));
        }
      }

      @SuppressWarnings({ "boxing", "unchecked" }) private void write(final ASTNode i, final ASTNode n, @Nullable final String id,
          @Nullable final BiFunction<Integer, Integer> bf) {
        if (bf == null && id == null) {
          write(i, n);
          return;
        }
        assert bf != null;
        assert id != null;
        as.list(ReportGenerator.Util.functions("")).forEach(λ -> report().put(id + λ.name(), bf.apply(λ.function().run(i), λ.function().run(n))));
      }

      @SuppressWarnings({ "unchecked", "rawtypes" }) private void writePerc(final ASTNode n1, final ASTNode n2, final String id) {
        String a; // TODO Matteo: to be converted to double or float? -- Matteo
        for (@NotNull final NamedFunction ¢ : ReportGenerator.Util.functions("")) {
          a = Utils.p(¢.function().run(n1), ¢.function().run(n2));
          report().put(id + ¢.name() + " %", a);
        }
      }
    }
  }
}
