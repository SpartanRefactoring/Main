/* TODO Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 *
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 *
 * @since Sep 25, 2016 */
package il.org.spartan.spartanizer.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.testing.*;

@SuppressWarnings("static-method") //
public class TrimmerLogTest {
  /** Tests of {@link cmdline.TrimmerLog}
   * @author AnnaBel7
   * @author michalcohen
   * @since Nov 10, 2016 */
  @Test public void a() {
    TrimmerLog.setMaxApplications(50);
    azzert.that(TrimmerLog.getMaxApplications(), is(50));
  }

  @Test public void b() {
    TrimmerLog.setMaxTips(50);
    azzert.that(TrimmerLog.getMaxTips(), is(50));
  }

  @Test public void c() {
    TrimmerLog.setMaxVisitations(50);
    azzert.that(TrimmerLog.getMaxVisitations(), is(50));
  }

  @Test public void test02() {
    @NotNull final TrimmingOperand o = trimmingOf("new Integer(3)");
    @NotNull final String wrap = Wrap.find(o.get()).on(o.get());
    @NotNull final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    @NotNull final IDocument d = new Document(wrap);
    assert d != null;
    @NotNull final Trimmer a = new Trimmer();
    try {
      @NotNull final IProgressMonitor pm = wizard.nullProgressMonitor;
      pm.beginTask("Creating rewrite operation...", IProgressMonitor.UNKNOWN);
      final ASTRewrite $ = ASTRewrite.create(u.getAST());
      a.consolidateTips($, u, null);
      pm.done();
      $.rewriteAST(d, null).apply(d);
    } catch (@NotNull MalformedTreeException | BadLocationException ¢) {
      throw new AssertionError(¢);
    }
    assert d != null;
    if (wrap.equals(d.get()))
      azzert.fail("Nothing done on " + o.get());
  }

  @Test public void test03() {
    @NotNull final TrimmingOperand o = trimmingOf("for(int i=0; i <100; i++){\n\tpr(i);\n}");
    @NotNull final String wrap = Wrap.find(o.get()).on(o.get());
    @NotNull final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    @NotNull final IDocument d = new Document(wrap);
    assert d != null;
    @NotNull final Trimmer a = new Trimmer();
    try {
      @NotNull final IProgressMonitor pm = wizard.nullProgressMonitor;
      pm.beginTask("Creating rewrite operation...", IProgressMonitor.UNKNOWN);
      final ASTRewrite $ = ASTRewrite.create(u.getAST());
      a.consolidateTips($, u, null);
      pm.done();
      $.rewriteAST(d, null).apply(d);
    } catch (@NotNull MalformedTreeException | BadLocationException ¢) {
      throw new AssertionError(¢);
    }
    assert d != null;
    if (wrap.equals(d.get()))
      azzert.fail("Nothing done on " + o.get());
  }

  @Test public void test04() {
    @NotNull final TrimmingOperand o = trimmingOf("for(int i=0; i <100; i++){\n\tpr(i);\n}");
    @NotNull final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(Wrap.find(o.get()).on(o.get()));
    assert u != null;
    assert u.getJavaElement() == null;
  }
}
