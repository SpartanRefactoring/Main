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
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.analyses.util.*;
import il.org.spartan.spartanizer.research.nanos.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.utils.*;

public class NanoInstancesCollector extends DeprecatedFolderASTVisitor {
  static final NanoPatternTipper<EnhancedForStatement> nano = new HoldsForAny();
  static final InteractiveSpartanizer spartanalyzer = new InteractiveSpartanizer();
  static final File out = new File(system.tmp + File.separator + nano.nanoName() + ".txt");

  public static void main(@NotNull final String[] args)
      throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    clazz = NanoInstancesCollector.class;
    spartanalyzer.add(EnhancedForStatement.class, new NanoPatternTipper<EnhancedForStatement>() {
      static final long serialVersionUID = -8053877776935099016L;

      @NotNull @Override public Fragment pattern(@NotNull final EnhancedForStatement ¢) {
        return new Fragment("", ¢, getClass()) {
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

  @Override public boolean visit(@NotNull final CompilationUnit ¢) {
    ¢.accept(new CleanerVisitor());
    spartanalyzer.fixedPoint(¢);
    return true;
  }

  @Override protected void visit(final String path) {
    Files.appendFile(out, "-------" + path + "-------\n");
    super.visit(path);
  }
}
