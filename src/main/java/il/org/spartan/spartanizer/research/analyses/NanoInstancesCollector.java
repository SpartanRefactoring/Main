/** TODO: orimarco <marcovitch.ori@gmail.com> please add a description
 * @author orimarco <marcovitch.ori@gmail.com>
 * @since Jan 10, 2017 */
package il.org.spartan.spartanizer.research.analyses;

import java.io.*;
import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.analyses.util.*;
import il.org.spartan.spartanizer.research.nanos.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.tables.*;
import org.jetbrains.annotations.NotNull;

public class NanoInstancesCollector extends FolderASTVisitor {
  static final NanoPatternTipper<EnhancedForStatement> nano = new HoldsForAny();
  static final InteractiveSpartanizer spartanalyzer = new InteractiveSpartanizer();
  static final File out = new File(Table.temporariesFolder + system.fileSeparator + nano.getClass().getSimpleName() + ".txt");

  public static void main(@NotNull final String[] args)
      throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    clazz = NanoInstancesCollector.class;
    spartanalyzer.add(EnhancedForStatement.class, new NanoPatternTipper<EnhancedForStatement>() {
      @Override @NotNull public Tip pattern(@NotNull final EnhancedForStatement ¢) {
        return new Tip("", ¢, getClass()) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            Files.appendFile(out, ¢ + "_________________\n");
            nano.tip(¢).go(r, g);
          }
        };
      }

      @Override public boolean canTip(final EnhancedForStatement ¢) {
        return nano.canTip(¢);
      }

      @Override public String description(final EnhancedForStatement ¢) {
        return nano.description(¢);
      }
    });
    FolderASTVisitor.main(args);
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
