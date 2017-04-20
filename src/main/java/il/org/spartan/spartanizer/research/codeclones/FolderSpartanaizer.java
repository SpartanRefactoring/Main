package il.org.spartan.spartanizer.research.codeclones;

import java.io.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** Spartanaize a given folder
 * @author oran1248
 * @since 2017-04-11 */
class FolderSpartanaizer {
  public static void main(final String[] args) {
    new ASTInFilesVisitor(args) {
      @Override public void visitFile(final File f) {
        try {
          FileUtils.writeToFile(f.getAbsolutePath(), new NoBrainDamagedTippersSpartanizer().fixedPoint(FileUtils.read(f)));
        } catch (final Exception ¢) {
          note.bug(¢);
        }
      }
    }.visitAll(new ASTVisitor() {/**/});
  }
}
