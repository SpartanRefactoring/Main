package il.org.spartan.spartanizer.cmdline.good;

import java.io.*;

import fluent.ly.*;
import il.org.spartan.external.*;

/** TODO Matteo Orru' <matteo.orru@cs.technion.ac.il> please add a description
 * @author Matteo Orru' <matteo.orru@cs.technion.ac.il>
 * @since Jan 15, 2017 */
abstract class AbstractCommandLineProcessor {
  @External(alias = "i", value = "input folder") String inputFolder = ".";
  @External(alias = "o", value = "output folder") final String outputFolder = "/tmp";
  @SuppressWarnings("CanBeFinal") private String presentSourceName;

  public abstract void apply();
  protected String makeFile(final String fileName) {
    return outputFolder + File.separator + presentSourceName + "." + fileName;
  }
  
  public static void main(final String[] args) {
    if (args.length != 0)
      as.list(args).forEach(λ -> new BatchSpartanizer(λ).fire());
    else
      new BatchSpartanizer(".", "current-working-directory").fire();
  }
}
