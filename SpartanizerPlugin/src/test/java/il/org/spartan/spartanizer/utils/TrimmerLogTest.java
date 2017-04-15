/* TODO Yossi Gil LocalVariableInitializedStatement description
 *
 * @author Yossi Gil
 *
 * @since Sep 25, 2016 */
package il.org.spartan.spartanizer.utils;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.trimming.*;

@SuppressWarnings("static-method") //
public class TrimmerLogTest {
  /** Tests of {@link TrimmerMonitor.TrimmerLog}
   * @author AnnaBel7
   * @author michalcohen
   * @since Nov 10, 2016 */
  @Test public void test02() {
    final TrimmingOperand o = trimminKof("new Integer(3)");
    final String wrap = WrapIntoComilationUnit.find(o.get()).on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final IDocument d = new Document(wrap);
    assert d != null;
    final TrimmerImplementation a = new TrimmerImplementation();
    try {
      a.go(u).rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException ¢) {
      throw new AssertionError(¢);
    }
    assert d != null;
    if (wrap.equals(d.get()))
      azzert.fail("Nothing done on " + o.get());
  }

  @Test public void test03() {
    final TrimmingOperand o = trimminKof("for(int i=0; i <100; i++){\n\tpr(i);\n}");
    final String wrap = WrapIntoComilationUnit.find(o.get()).on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final IDocument d = new Document(wrap);
    assert d != null;
    final TrimmerImplementation a = new TrimmerImplementation();
    try {
      a.go(u).rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException ¢) {
      throw new AssertionError(¢);
    }
    assert d != null;
    if (wrap.equals(d.get()))
      azzert.fail("Nothing done on " + o.get());
  }

  @Test public void test04() {
    final TrimmingOperand o = trimminKof("for(int i=0; i <100; i++){\n\tpr(i);\n}");
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(WrapIntoComilationUnit.find(o.get()).on(o.get()));
    assert u != null;
    assert u.getJavaElement() == null;
  }
}
