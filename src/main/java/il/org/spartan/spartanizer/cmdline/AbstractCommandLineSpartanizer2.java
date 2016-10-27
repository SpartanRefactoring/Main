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

@SuppressWarnings("unused") public abstract class AbstractCommandLineSpartanizer2 {
  static List<Class<? extends BodyDeclaration>> selectedNodeTypes = as.list(MethodDeclaration.class);

  static AbstractGUIApplicator getSpartanizer(final String tipperName) {
    return Tips2.get(tipperName);
  }

  protected String folder = "/tmp/";
  protected String afterFileName;
  protected String beforeFileName;
  protected String inputPath;
  protected String reportFileName;
  protected String spectrumFileName;
  protected PrintWriter afters;
  protected PrintWriter befores;
  int done;
  CSVStatistics report;
  CSVStatistics spectrumStats;
  final ChainStringToIntegerMap spectrum = new ChainStringToIntegerMap();

  public abstract void apply();

  void fire() {
    run();
    reportSpectrum();
    // reportCoverage();
//    runEssence();
//    runWordCount();
  }


  private void run() {
    System.err.printf( //
        " Input path=%s\n" + //
            "Before path=%s\n" + //
            " After path=%s\n" + //
            "Report path=%s\n" + //
            "\n", //
        inputPath, //
        beforeFileName, //
        afterFileName, //
        reportFileName);
     apply();
    System.err.print("\n Done: " + done + " items processed.");
    System.err.print("\n Summary: " + report.close());
  }

  public void runReport(@SuppressWarnings("unused") final Consumer<CSVStatistics> __) {
    // TODO Matteo: implement this if we need it; found in random scan
  }

  @FunctionalInterface public interface ToInt<R> {
    int f(R r);
  }

  static NamedFunction m(String name, ToInt<String> f) {
    return new NamedFunction(name, f);
  }

  static class NamedFunction {
    NamedFunction(String name, ToInt<String> f) {
      this.name = name;
      this.f = f;
    }

    final String name;
    final ToInt<String> f;
  }

  NamedFunction functions[] = as.array(//
      m("seventeeen", (¢) -> 17), //
      m("length", (¢) -> ¢.length()), //
      m("essence", (¢) -> Essence.of(¢).length())//
      );

abstract void go(final String javaCode); 

  private void reportSpectrum() {
    for (final Entry<String, Integer> ¢ : spectrum.entrySet()) {
      spectrumStats.put("Tipper", ¢.getKey());
      spectrumStats.put("Times", ¢.getValue());
      spectrumStats.nl();
    }
    System.err.print("\n Spectrum: " + spectrumStats.close());
  }

  /** Setup PrintWriters
   * @author matteo */
  protected void setUpPrintWriters() {
    try (PrintWriter b = new PrintWriter(new FileWriter(beforeFileName)); //
        PrintWriter a = new PrintWriter(new FileWriter(afterFileName))) {
      befores = b;
      afters = a;
    } catch (final IOException x) {
      x.printStackTrace();
      System.err.println(done + " items processed; processing of " + inputPath + " failed for some I/O reason");
    }
  }

  /** Setup reports
   * @author matteo */
  protected void setUpReports() {
    try {
      report = new CSVStatistics(reportFileName, "property");
      spectrumStats = new CSVStatistics(spectrumFileName, "property");
    } catch (final IOException x) {
      x.printStackTrace();
      System.err.println("problem in setting up reports");
    }
  }
}