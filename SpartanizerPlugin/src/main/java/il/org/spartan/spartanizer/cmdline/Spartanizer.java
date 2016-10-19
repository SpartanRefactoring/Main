package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;

/** A configurable object capable of making a scan.
 * @author Yossi Gil
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
//    coverageFileName = folder + name + ".coverage.CSV";
  }

}
