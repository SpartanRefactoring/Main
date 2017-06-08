package il.org.spartan.athenizer.zoom;

import java.io.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.utils.*;

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
    new ASTInFilesVisitor(new String[] { "-i", ".", "." }) {
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
