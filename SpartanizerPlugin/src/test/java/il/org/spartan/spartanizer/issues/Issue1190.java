package il.org.spartan.spartanizer.issues;

import java.io.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** Batch testing - run the spartinizer on itself with no errors
 * @author oran1248 <tt>oran.gilboa1@gmail.com</tt>
 * @since 2017-04-01 */
public class Issue1190 {
  TextualTraversals trimmer;

  @Before public void setUp() {
    trimmer = new TextualTraversals();
  }

  @Test(timeout = 30000) public void runTheSpartinizerOnItself() {
    new ASTInFilesVisitor(new String[] { "-i", ".", "." }) {
      @Override public void visitFile(final File f) {
        try {
          trimmer.fixed(FileUtils.read(f));
        } catch (final IOException ¢) {
          note.io(¢, "Cannot read: " + f);
        }
      }
    }.visitAll(new ASTVisitor() {/**/});
  }
}
