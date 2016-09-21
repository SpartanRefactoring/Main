package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;
import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.spartanizations.*;
import il.org.spartan.spartanizer.wringing.*;
import il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;
import il.org.spartan.utils.*;

public class TrimmerLogTest {
  private Map prop;

  @Ignore @Test public void test01() {
    Wring<ASTNode> w = null;
    ASTNode n = null;
    TrimmerLog.suggestion(w, n);
    assertTrue(false);
  }
 
  @Test public void test02() {
    final Operand o = trimmingOf("new Integer(3)");
    final Wrap w = Wrap.find(o.get());
    final String wrap = w.on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final Document d = new Document(wrap);
    assert d != null;
    final Trimmer a = new Trimmer();
    try {
      final IProgressMonitor pm = wizard.nullProgressMonitor;
      pm.beginTask("Creating rewrite operation...", IProgressMonitor.UNKNOWN);
      final ASTRewrite $ = ASTRewrite.create(u.getAST());
      a.consolidateSuggestions($, u, (IMarker) null);
      pm.done();
      final ASTRewrite x = $;
      x.rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException e) {
      throw new AssertionError(e);
    }
    assert d != null;
    final String unpeeled = d.get();
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + o.get());
  }
 
  @Test public void test03() {
    final Operand o = trimmingOf("for(int i=0; i < 100; i++){\n\tSystem.out.prinln(i);\n}");
    final Wrap w = Wrap.find(o.get());
    final String wrap = w.on(o.get());
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    final Document d = new Document(wrap);
    assert d != null;
    final Trimmer a = new Trimmer();
    try {
      final IProgressMonitor pm = wizard.nullProgressMonitor;
      pm.beginTask("Creating rewrite operation...", IProgressMonitor.UNKNOWN);
      final ASTRewrite $ = ASTRewrite.create(u.getAST());
      a.consolidateSuggestions($, u, (IMarker) null);
      pm.done();
      final ASTRewrite x = $;
      x.rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException e) {
      throw new AssertionError(e);
    }
    assert d != null;
    final String unpeeled = d.get();
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + o.get());
  }
 
 
  @Test public void test04(){
    final Operand o = trimmingOf("for(int i=0; i < 100; i++){\n\tSystem.out.prinln(i);\n}");
    final Wrap w = Wrap.find(o.get());
    System.out.println(w);
    final String wrap = w.on(o.get());
    System.out.println(wrap);
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(wrap);
    assert u != null;
    IJavaElement je = u.getJavaElement();
    assert je != null;

  }
 
  @Ignore @Test public void test05(){
    TrimmerLog.fileProperties();

  }

}
