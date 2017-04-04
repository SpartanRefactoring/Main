package il.org.spartan.spartanizer.issues;

import java.io.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.utils.*;

/** Batch testing - run the spartinizer on itself with no errors
 * @author oran1248 <tt>oran.gilboa1@gmail.com</tt>
 * @since 2017-04-01 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Issue1190 {
  Trimmer trimmer;

  @Before public void setUp() {
    trimmer = new Trimmer(Toolbox.defaultInstance());
    trimmer.setExceptionListener(λ -> {
      λ.printStackTrace();
      assert false;
    });
  }

  @Test(timeout = 30000) public void runTheSpartinizerOnItself() {
    new ASTInFilesVisitor(new String[] { "-i", ".", "." }) {
      @Override protected void visit(final File f) {
        try {
          trimmer.fixed(FileUtils.read(f));
        } catch (final IOException ¢) {
          monitor.infoIOException(¢, "Cannot read: " + f);
        }
      }
    }.fire(new ASTVisitor() {/**/});
  }
}
