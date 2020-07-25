package il.org.spartan.spartanizer.research.codeclones;

import java.io.File;

import org.eclipse.jdt.core.dom.ASTVisitor;

import fluent.ly.note;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.research.analyses.NoBrainDamagedTippersSpartanizer;
import il.org.spartan.utils.FileUtils;

/** Spartanaize a given folder
 * @author oran1248
 * @since 2017-04-11 */
class FolderSpartanaizer {
  public static void main(final String[] args) {
    new GrandVisitor(args) {
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
