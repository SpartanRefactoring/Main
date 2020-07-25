package il.org.spartan.athenizer.zoom;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fluent.ly.note;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.plugin.TextualTraversals;
import il.org.spartan.utils.FileUtils;

/** Batch testing- run the athenizer on itself with no errors
 * @author tomerdragucki
 * @since 2017-05-27 */
@Ignore
public class Issue1345 {
  TextualTraversals trimmer;

  @Before public void setUp() {
    trimmer = new TextualTraversals();
  }
  @Test(timeout = 30000) public void runTheAthenizerOnItself() {
    new GrandVisitor(new String[] { "-i", ".", "." }) {
      @Override public void visitFile(final File f) {
        try {
          // TODO Tomer Dragucki: replace this line so bloating will be
          // performed and not spartanizing
          trimmer.fixed(FileUtils.read(f));
        } catch (final IOException ¢) {
          note.io(¢, "Cannot read: " + f);
        }
      }
    }.visitAll(new ASTVisitor() {/**/});
  }
}
