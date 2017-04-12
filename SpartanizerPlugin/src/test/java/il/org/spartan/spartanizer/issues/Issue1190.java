package il.org.spartan.spartanizer.issues;

import java.io.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.dispatch.Configurations;
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
    exceptionsCounter = 1;
    trimmer = new Trimmer(Configurations.defaultConfiguration());
    trimmer.onException(new TrimmerExceptionListener() {
      @Override @SuppressWarnings("boxing") public void accept(final Exception x, final Tipper<? extends ASTNode> t, final ASTNode n) {
        System.err.printf("%d. Intercepted %s with message '%s'\n", exceptionsCounter++, English.indefinite(x), x.getMessage());
        System.err.printf("in tipper %s applied on %s:\n%s\n\n", English.name(t), English.indefinite(n), n);
      }

      @Override public void accept(final Exception ¢) {
        monitor.bug(this, ¢);
      }
    });
  }

  @Test(timeout = 30000) public void runTheSpartinizerOnItself() {
    new ASTInFilesVisitor(new String[] { "-i", ".", "." }) {
      @Override protected void visit(final File f) {
        try {
          trimmer.fixed(FileUtils.read(f));
        } catch (final IOException ¢) {
          monitor.config(¢, "Cannot read: " + f);
        }
      }
    }.fire(new ASTVisitor() {/**/});
  }
}
