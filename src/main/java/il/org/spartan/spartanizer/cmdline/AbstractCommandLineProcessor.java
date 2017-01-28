/** TODO: Matteo Orru' <matteo.orru@cs.technion.ac.il> please add a description
 * @author Matteo Orru' <matteo.orru@cs.technion.ac.il>
 * @since Jan 15, 2017 */
package il.org.spartan.spartanizer.cmdline;
import il.org.spartan.*;
import il.org.spartan.external.*;

abstract class AbstractCommandLineProcessor {
  @External(alias = "i", value = "input folder") String inputFolder = ".";
  @External(alias = "o", value = "output folder") String outputFolder = "/tmp";
  private String presentSourceName;

  public abstract void apply();

  protected String makeFile(final String fileName) {
    return outputFolder + system.fileSeparator + presentSourceName + "." + fileName;
  }

  public static void main(final String[] args) {
    if (args.length != 0)
      as.list(args).forEach(λ -> new BatchSpartanizer(λ).fire());
    else
      new BatchSpartanizer(".", "current-working-directory").fire();
  }
}
