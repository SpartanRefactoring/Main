package il.org.spartan.spartanizer.cmdline;

import java.io.*;

import il.org.spartan.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;

/** A command line client to apply the spartanization process on a bunch of
 * projects together.
 * @author Yossi Gil
 * @author Matteo Orru'
 * @since 2016 */

public final class Spartanizer extends AbstractSpartanizer{
  
  public static void main(final String[] args) {
    for (final String ¢ : args.length != 0 ? args : new String[] { "." })
      new Spartanizer(¢).fire();
  }

  @Override public void apply() {
    for (final File ¢ : new FilesGenerator(".java").from(inputPath)) {
      System.out.println("Free memory (bytes): " + Unit.BYTES.format(Runtime.getRuntime().freeMemory()));
      go(¢);
    }
  }
  
  Spartanizer(final String path) {
    this(path, system.folder2File(path));
  }

  Spartanizer(final String inputPath, final String name) {
    this.inputPath = inputPath;
    beforeFileName = folder + name + ".before.java";
    afterFileName = folder + name + ".after.java";
    reportFileName = folder + name + ".CSV";
    spectrumFileName = folder + name + ".spectrum.CSV";
//    setUpPrintWriters(); // Note: temporarily removed
    try {
      befores = new PrintWriter(beforeFileName);
      afters = new PrintWriter(afterFileName);
    } catch (FileNotFoundException x) {
      x.printStackTrace();
    }    
//    coverageFileName = folder + name + ".coverage.CSV"; // Note: temporarily removed
    try {
      report = new CSVStatistics(reportFileName, "property");
      spectrumStats = new CSVStatistics(spectrumFileName, "property");
    } catch (IOException x) {
      x.printStackTrace();
      System.err.println("problem in setting up reports");
    }
  }
}
