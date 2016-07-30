package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.hamcrest.MatcherAssert.iz;
import static il.org.spartan.hamcrest.OrderingComparison.greaterThan;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.junit.Test;

import il.org.spartan.refactoring.spartanizations.Wrap;
import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Into;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.MakeAST;
import il.org.spartan.refactoring.utils.Rewrite;

@SuppressWarnings({ "javadoc", "static-method" }) //
public class IfEmptyThenEmptyElseTest {
  private static final IfEmptyThenEmptyElse WRING = new IfEmptyThenEmptyElse();
  private static final Statement INPUT = Into.s("{if (b) ; else ;}");
  private static final IfStatement IF = Extract.firstIfStatement(INPUT);
  @Test public void eligible() {
    assertTrue(WRING.eligible(IF));
  }
  @Test public void emptyElse() {
    assertTrue(Is.vacuousElse(IF));
  }
  @Test public void emptyThen() {
    assertTrue(Is.vacuousThen(IF));
  }
  @Test public void extractFirstIf() {
    assertNotNull(IF);
  }
  @Test public void inputType() {
    org.hamcrest.MatcherAssert.assertThat(INPUT, instanceOf(Block.class));
  }
  @Test public void runGo() throws IllegalArgumentException, MalformedTreeException, BadLocationException {
    final String input = Wrap.Statement.on(INPUT + "");
    final Document d = new Document(input);
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(d.get());
    final IfStatement s = Extract.firstIfStatement(u);
    assertThat(s, iz("if(b);else;"));
    final ASTRewrite r = ASTRewrite.create(u.getAST());
    final Rewrite t = WRING.make(s);
    t.go(r, null);
    final TextEdit e = r.rewriteAST(d, null);
    assertNotNull(e);
    assertThat(e.getChildren().length, greaterThan(0));
    e.apply(d);
    assertNull(d.get(), Extract.firstIfStatement(MakeAST.COMPILATION_UNIT.from(d.get())));
  }
  @Test public void scopeIncludes() {
    assertTrue(WRING.scopeIncludes(IF));
  }
}
