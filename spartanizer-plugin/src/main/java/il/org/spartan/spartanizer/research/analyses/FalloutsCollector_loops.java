package il.org.spartan.spartanizer.research.analyses;

import static il.org.spartan.spartanizer.research.analyses.util.Files.appendFile;
import static il.org.spartan.spartanizer.research.analyses.util.Files.blank;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnhancedForStatement;

import fluent.ly.note;
import fluent.ly.system;
import il.org.spartan.spartanizer.ast.navigate.descendants;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.cmdline.good.DeprecatedFolderASTVisitor;
import il.org.spartan.spartanizer.engine.parse;
import il.org.spartan.spartanizer.research.util.CleanerVisitor;

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
          .from(parse.cu(nanonizer.fixedPoint(u)))//
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
