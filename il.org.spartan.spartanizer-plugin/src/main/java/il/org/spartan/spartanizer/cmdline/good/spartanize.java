package il.org.spartan.spartanizer.cmdline.good;

import java.io.*;

import fluent.ly.*;
import il.org.spartan.collections.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.cmdline.*;

/** This is a command line program, which can be thought of as a spartan
 * compiler. For each {@code .java} file it find, it encounters, it creates a
 * corresponding {@code .javas} file, which is the spartanized version of the
 * original. The {@code .javas} will be in the same folder as the {@code .java}
 * file, and would be overwritten each time this program is run.
 * @author Matteo Orru'
 * @since 2017-06-25 */
public class spartanize extends ASTInFilesVisitor {
  
  public static void main(final String[] args) throws SecurityException, IllegalArgumentException {
    visit(args.length != 0 ? args : defaultArguments);
  }
  public static void visit(final String... args) {
    for (final String ¢ : External.Introspector.extract(args != null && 
                  args.length != 0 ? args : defaultArguments, spartanize.class))
      matteo(¢);
  }
  private static void matteo(String ¢) {
    forget.it(¢);
  }
  
  protected void visitLocation(final String location) {
    // notify.beginLocation();
    presentSourceName = system.folder2File(presentSourcePath = location + File.separator + getCurrentLocation());
    new FilesGenerator(".java").from(presentSourcePath).forEach(λ -> visitFile(currentFile = λ));
    // notify.endLocation();
  }


}

