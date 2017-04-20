package il.org.spartan.spartanizer.research.analyses;

import static il.org.spartan.spartanizer.research.analyses.util.Files.*;

import java.io.*;
import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** Find all loops not matched by a nano pattern
 * @author orimarco <marcovitch.ori@gmail.com>
 * @since Jan 11, 2017 */
public class FalloutsCollector_loops extends DeprecatedFolderASTVisitor {
  private static final Nanonizer nanonizer = new Nanonizer();
  private static final File out = new File(system.tmp + File.separator + "loops.txt");

  public static void main(final String[] args)
      throws SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    clazz = FalloutsCollector_loops.class;
    blank(out);
    DeprecatedFolderASTVisitor.main(args);
  }

  @Override public boolean visit(final CompilationUnit u) {
    u.accept(new CleanerVisitor());
    try {
      descendants//
          .whoseClassIs(EnhancedForStatement.class)//
          .from(into.cu(nanonizer.fixedPoint(u)))//
          .stream()//
          .filter(iz::simpleLoop)//
          .forEach(λ -> appendFile(out, λ + ""));
    } catch (final AssertionError | IllegalArgumentException ¢) {
      note.bug(¢);
    }
    return true;
  }

  @Override protected void visit(final String path) {
    appendFile(out, "-------" + path + "-------\n");
    super.visit(path);
  }
}
