package il.org.spartan.spartanizer.research.codeclones;

import java.io.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.utils.*;

/** __ this class you can spartanize a directory easily. Or you can extends this
 * class and configure it to fit to your own needs.
 * @author oran1248
 * @since 2017-04-11 */
public class HeadlesSpartanizer extends GrandVisitor {
  public final TextualTraversals traversals = new TextualTraversals();

  protected void setUp() {
    /**/
  }
  protected void tearDown() {
    /**/
  }
  @SuppressWarnings("static-method") protected boolean spartanize(@SuppressWarnings("unused") final File __) {
    return true;
  }
  @SuppressWarnings("static-method") protected ASTVisitor astVisitor() {
    return new ASTVisitor() {/**/};
  }
  protected String perform(final String fileContent) {
    return fixedPoint(fileContent);
  }
  protected void analyze(@SuppressWarnings("unused") final String before, final String after) {
    try {
      FileUtils.writeToFile(v.current.absolutePath, after);
    } catch (final FileNotFoundException ¢) {
      note.io(¢);
    }
  }
  JavaProductionFilesVisitor v;
  public final void go(final String dirPath) {
    setUp();
    (v = new GrandVisitor(new String[] { dirPath }) {
      @Override public void visitFile(final File f) {
        if (!spartanize(f))
          return;
        String beforeChange;
        try {
          beforeChange = FileUtils.read(f);
          analyze(beforeChange, perform(beforeChange));
        } catch (final IOException ¢) {
          note.io(¢);
        }
      }
    }).visitAll(astVisitor());
    tearDown();
  }
  public final String fixedPoint(final String from) {
    return traversals.fixed(from);
  }
}
