package il.org.spartan.spartanizer.cmdline.good;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.io.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.collections.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.library.*;
import il.org.spartan.utils.*;

/** This is a command line program, which can be thought of as a spartan
 * compiler. For each {@code .java} file it find, it encounters, it creates a
 * corresponding {@code .javas} file, which is the spartanized version of the
 * original. The {@code .javas} will be in the same folder as the {@code .java}
 * file, and would be overwritten each time this program is run.
 * @author Matteo Orru'
 * @since 2017-06-25 */
public class spartanize extends {
  @External(alias = "i", value = "input folder") @SuppressWarnings("CanBeFinal") protected static String inputFolder = system.isWindows() ? "" : ".";
  @External(alias = "o", value = "output folder") @SuppressWarnings("CanBeFinal") protected static String outputFolder = "/tmp";
  protected static final String[] defaultArguments = as.array("..");
  private static final InteractiveSpartanizer is = new InteractiveSpartanizer();
  protected static String presentSourceName;
  protected static String presentSourcePath;
  private static String currentLocation;
  private static File currentFile;
  public static void main(final String[] args) throws SecurityException, IllegalArgumentException {
    System.err.println(defaultArguments[0]);
    visit(args.length != 0 ? args : defaultArguments);
  }
  public static void visit(final String... args) {
    for (final String ¢ : External.Introspector.extract(args != null && 
        args.length != 0 ? args : defaultArguments, spartanize.class)){
      presentSourceName = system.folder2File(presentSourcePath = inputFolder + File.separator + getCurrentLocation());
      new FilesGenerator(".java").from(presentSourcePath).forEach(λ -> visitFile(currentFile = λ));
      matteo(¢);
    }
  }
  private static void matteo(String ¢) {
    forget.it(¢);
  }
  
  public static String getCurrentLocation() {
    return currentLocation;
  }
  
  public void visitFile(final File f) {
    // notify.beginFile();
    if (Utils.isProductionCode(f) && productionCode(f))
      try {
        absolutePath = f.getAbsolutePath();
        relativePath = f.getPath();
        collect(FileUtils.read(f));
      } catch (final IOException ¢) {
        note.io(¢, "File = " + f);
      }
    notify.endFile();
  }
}

