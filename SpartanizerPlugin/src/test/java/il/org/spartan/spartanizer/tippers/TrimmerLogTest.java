package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

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
    final Operand o = trimmingOf("new Integer(3)");
    final String wrap = Wrap.find(o.get()).on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final Document d = new Document(wrap);
    assert d != null;
    final Trimmer a = new Trimmer();
    try {
      final IProgressMonitor pm = wizard.nullProgressMonitor;
      pm.beginTask("Creating rewrite operation...", IProgressMonitor.UNKNOWN);
      final ASTRewrite $ = ASTRewrite.create(u.getAST());
      a.consolidateTips($, u, (IMarker) null);
      pm.done();
      $.rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException ¢) {
      throw new AssertionError(¢);
    }
    assert d != null;
    if (wrap.equals(d.get()))
      azzert.fail("Nothing done on " + o.get());
  }

  @Test public void test03() {
    final Operand o = trimmingOf("for(int i=0; i <100; i++){\n\tSystem.out.prinln(i);\n}");
    final String wrap = Wrap.find(o.get()).on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final Document d = new Document(wrap);
    assert d != null;
    final Trimmer a = new Trimmer();
    try {
      final IProgressMonitor pm = wizard.nullProgressMonitor;
      pm.beginTask("Creating rewrite operation...", IProgressMonitor.UNKNOWN);
      final ASTRewrite $ = ASTRewrite.create(u.getAST());
      a.consolidateTips($, u, (IMarker) null);
      pm.done();
      $.rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException ¢) {
      throw new AssertionError(¢);
    }
    assert d != null;
    if (wrap.equals(d.get()))
      azzert.fail("Nothing done on " + o.get());
  }

  @Test public void test04() {
    final Operand o = trimmingOf("for(int i=0; i <100; i++){\n\tSystem.out.prinln(i);\n}");
    final Wrap w = Wrap.find(o.get());
    System.out.println(w);
    final String wrap = w.on(o.get());
    System.out.println(wrap);
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    assert u.getJavaElement() == null;
  }
}
