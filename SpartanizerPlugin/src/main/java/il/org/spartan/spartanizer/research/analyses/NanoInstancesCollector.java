/* TODO orimarco <marcovitch.ori@gmail.com> please add a description
 *
 * @author orimarco <marcovitch.ori@gmail.com>
 *
 * @since Jan 10, 2017 */
package il.org.spartan.spartanizer.research.analyses;

import java.io.*;
import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.analyses.util.*;
import il.org.spartan.spartanizer.research.nanos.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.util.*;

public class NanoInstancesCollector extends DeprecatedFolderASTVisitor {
  static final NanoPatternTipper<EnhancedForStatement> nano = new HoldsForAny();
  static final InteractiveSpartanizer nanonizer = new InteractiveSpartanizer();
  static final File out = new File(system.tmp + File.separator + nano.className() + ".txt");

  public static void main(final String[] args)
      throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    clazz = NanoInstancesCollector.class;
    nanonizer.add(EnhancedForStatement.class, new NanoPatternTipper<EnhancedForStatement>() {
      static final long serialVersionUID = -0x6FC51F2AB6A11A88L;

      @Override public Tip pattern(final EnhancedForStatement ¢) {
        return new Tip("", getClass(), ¢) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            Files.appendFile(out, ¢ + "_________________\n");
            nano.tip(¢).go(r, g);
          }
        };
      }
      @Override public boolean canTip(final EnhancedForStatement ¢) {
        return nano.check(¢);
      }
      @Override public String description(final EnhancedForStatement ¢) {
        return nano.description(¢);
      }
    });
    DeprecatedFolderASTVisitor.main(args);
  }
  @Override public boolean visit(final CompilationUnit ¢) {
    ¢.accept(new CleanerVisitor());
    nanonizer.fixedPoint(¢);
    return true;
  }
  @Override protected void visit(final String path) {
    Files.appendFile(out, "-------" + path + "-------\n");
    super.visit(path);
  }
}
