package il.org.spartan.spartanizer.issues;

import java.io.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Batch testing - run the spartinizer on itself with no errors
 * @author oran1248 <tt>oran.gilboa1@gmail.com</tt>
 * @since 2017-04-01 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Issue1190 {
  Trimmer trimmer;
  int exceptionsCounter;

  @Before public void setUp() {
    exceptionsCounter=1;
    trimmer = new Trimmer(Toolbox.defaultInstance());
    trimmer.setExceptionListener(new TrimmerExceptionListener() {
      
      @Override public void accept(Exception x, Tipper<? extends ASTNode> t, ASTNode n) {
        System.err.println("Exception #" + exceptionsCounter++);
        System.err.println("Tipper Type: " + t.getClass());
        System.err.println("Node Type: " + n.getClass());
        System.err.println("Node Text: '" + n + "'");
        System.err.println();
      }
      
      @Override public void accept(Exception ¢) {
        ¢.printStackTrace();
      }
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
