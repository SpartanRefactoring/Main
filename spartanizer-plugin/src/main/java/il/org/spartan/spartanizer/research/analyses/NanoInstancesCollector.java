/* TODO orimarco <marcovitch.ori@gmail.com> please add a description
 *
 * @author orimarco <marcovitch.ori@gmail.com>
 *
 * @since Jan 10, 2017 */
package il.org.spartan.spartanizer.research.analyses;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.system;
import il.org.spartan.spartanizer.cmdline.good.DeprecatedFolderASTVisitor;
import il.org.spartan.spartanizer.cmdline.good.InteractiveSpartanizer;
import il.org.spartan.spartanizer.research.analyses.util.Files;
import il.org.spartan.spartanizer.research.nanos.HoldsForAny;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.research.util.CleanerVisitor;
import il.org.spartan.spartanizer.tipping.Tip;

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
