package il.org.spartan.spartanizer.utils;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;

/** Tests of {@link TraversalMonitor}
 * @author AnnaBel7
 * @author michalcohen
 * @since Nov 10, 2016 */
@SuppressWarnings("static-method") //
public class TestOperandTest {
  @Test public void test02() {
    final TestOperand o = trimmingOf("new Integer(3)");
    final String wrap = WrapIntoComilationUnit.find(o.get()).on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final IDocument d = new Document(wrap);
    assert d != null;
    final TraversalImplementation a = new TraversalImplementation();
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
    final TestOperand o = trimmingOf("for(int i=0; i <100; i++){\n\tpr(i);\n}");
    final String wrap = WrapIntoComilationUnit.find(o.get()).on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final IDocument d = new Document(wrap);
    assert d != null;
    final TraversalImplementation a = new TraversalImplementation();
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
    final TestOperand o = trimmingOf("for(int i=0; i <100; i++){\n\tpr(i);\n}");
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(WrapIntoComilationUnit.find(o.get()).on(o.get()));
    assert u != null;
    assert u.getJavaElement() == null;
  }
}
