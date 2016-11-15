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
import il.org.spartan.spartanizer.cmdline.report.ReportGenerator.*;

/** Demo of recommended use of {@link Listener.S}
 * <p>
 * Copy the code, changing the name {@link ConfigurableReport} to whatever you
 * need. provide.
 * @author Yossi Gil
 * @year 2016 */
public interface ConfigurableReport {
  /** [[SuppressWarningsSpartan]] */
  class Settings extends Listener.S {
    private static final long serialVersionUID = 1L;
    String outputFolder = "/tmp/"; // default modifier
    String inputFolder; // default modifier
    private String reportFileName;
    private String header;
    CSVStatistics report;
    private static ArrayList<ASTNode> inputList = new ArrayList<>();
    private static ArrayList<ASTNode> outputList = new ArrayList<>();

    public static void addInput(final ASTNode input) {
      getInputList().add(input);
    }

    public static void addOutput(final ASTNode output) {
      getOutputList().add(output);
    }

    public CSVStatistics report() {
      return report;
    }

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

  ////@formatter:off
  /* default access */ int howMany;
  /* required here! */ boolean robustMode;

  private ASTNode input;
  private ASTNode output;

  public ASTNode getInput() {
    return input;
  }

  public ASTNode getOutput() {
    return output;
  }

  public String getOutputFolder() {
    return outputFolder;
  }

  public void setOutputFolder(final String outputFolder) {
    this.outputFolder = outputFolder;
  }

  public String getInputFolder() {
    return inputFolder;
  }

  public void setInputFolder(final String inputFolder) {
    this.inputFolder = inputFolder;
  }

  public void setReport(final String reportFilename, final String header) {
    try {
     report = new CSVStatistics(reportFilename, header);
   } catch (final IOException x) {
     x.printStackTrace();
   }
  }

  String getHeader() {
    return header;
  }

  /** Demo of the implementation. Don't change the name. Just change services
   * @see #go() the only service provided by this template
   * @author Yossi Gil
   * @year 2016 */
  class Action extends Settings {
    /** real serialVersionUID comes much later in production code */
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("boxing")
    int go() {

      listeners().push("Initializing the " + getFileName() + " report.");

      if (Settings.this.robustMode) {
        listeners().pop("we dare do nothing in robust mode");
        return 0;
      }

      listeners().tick("generate summary file name");
      summaryFileName();

      for (int i = 0; i < getInputList().size(); i++){
        // write
        listeners().tick("writing basic data");
//        name(getInput());
        name(getInputList().get(i));
        // write
        listeners().tick("writing metrics");
//        write(getInput(), getOutput());
        write(getInputList().get(i), getOutputList().get(i));
        // write
        listeners().tick("writing differences");
//        write(getInput(), getOutput(), "Δ ", (n1, n2) -> (n1 - n2));
        write(getInputList().get(i), getOutputList().get(i), "Δ ", (n1, n2) -> (n1 - n2));
        // write
        listeners().tick("writing delta");
//        write(getInput(), getOutput(), "δ ", (n1, n2) -> system.d(n1, n2));
        write(getInputList().get(i), getOutputList().get(i), "δ ", (n1, n2) -> system.d(n1, n2));
        // write
        listeners().tick("writing perc");
//        writePerc(getInput(), getOutput(), "δ ");
        writePerc(getInputList().get(i), getOutputList().get(i), "δ ");
        // write
        listeners().tick("writing ratio");
        // not yet
        report.nl();
      }
      listeners().pop("exhausted");
      return defaultValue();
    }

    public void name(final ASTNode i) {
      report().put("name", extract.name(i));
      report().put("category", extract.category(i));
    }

    public void summaryFileName() {
      report().summaryFileName();
    }

    public void close(){
      report().close();
    }

    private int defaultValue() {
      return hashCode();
    }

    // running report
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void write(final ASTNode i, final ASTNode o) {
      for (final NamedFunction ¢ : ReportGenerator.Util.functions("")) {
//        ReportGenerator.Util.report("metrics").put(¢.name() + "1", ¢.function().run(i));
//        ReportGenerator.Util.report("metrics").put(¢.name() + "2", ¢.function().run(o));
        report().put(¢.name() + "1", ¢.function().run(i));
        report().put(¢.name() + "2", ¢.function().run(o));
      }
    }

    @SuppressWarnings({ "boxing", "unchecked", "rawtypes" })
    public void write(final ASTNode i, final ASTNode o, final String id,
        final BiFunction<Integer, Integer> bf) {
      if(bf == null && id == null){
        write(i, o);
        return;
      }
      assert bf != null;
      assert id != null;
      for (final NamedFunction ¢ : ReportGenerator.Util.functions(""))
//        ReportGenerator.Util.report("metrics").put(id + ¢.name(), bf.apply(¢.function().run(i), ¢.function().run(o)));
        report().put(id + ¢.name(), bf.apply(¢.function().run(i), ¢.function().run(o)));

    }

    @SuppressWarnings({ "unchecked", "rawtypes" }) public void writePerc(final ASTNode n1, final ASTNode n2, final String id) {
      String a; // TODO Matteo: to be converted to double or float? -- Matteo
      for (final NamedFunction ¢ : ReportGenerator.Util.functions("")) {
        a = system.p(¢.function().run(n1), ¢.function().run(n2));
//        ReportGenerator.Util.report("metrics").put(id + ¢.name() + " %", a);
        report().put(id + ¢.name() + " %", a);
      }
    }

    public void initialize() {
      try {
        report = new CSVStatistics(getFileName(), getHeader());
      } catch (final IOException x) {
        x.printStackTrace();
      }
    }
  }
    class Configuration { /**/
      private String outFolder = "/tmp/"; // default modifier
      private String inFolder; // default modifier

      public String outFolder() {
        return outFolder;
      }
      @SuppressWarnings("hiding") public void outFolder(final String outFolder) {
        this.outFolder = outFolder;
      }
      public String inFolder() {
        return inFolder;
      }
      @SuppressWarnings("hiding") public void inFolder(final String inFolder) {
        this.inFolder = inFolder;
      }
    }

    public Action getAction() {
      return new Action();
    }

    public void setHeader(final String ¢) {
      header = ¢;
    }

    public void setFileName(final String ¢) {
      reportFileName = ¢;
    }

    public String getFileName() {
      return reportFileName;
    }

    public void setInput(final ASTNode n) {
      input = n;
    }

    public void setOutput(final ASTNode n) {
      output = n;
    }

    public static ArrayList<ASTNode> getInputList() {
      return inputList;
    }

    public static void setInputList(final ArrayList<ASTNode> inputList) {
      Settings.inputList = inputList;
    }

    public static ArrayList<ASTNode> getOutputList() {
      return outputList;
    }

    public static void setOutputList(final ArrayList<ASTNode> outputList) {
      Settings.outputList = outputList;
    }
  }
}
