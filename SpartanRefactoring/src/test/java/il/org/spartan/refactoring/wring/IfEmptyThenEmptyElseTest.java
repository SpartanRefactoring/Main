package il.org.spartan.refactoring.wring;

import il.org.spartan.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;

import static il.org.spartan.azzert.*;
import static org.junit.Assert.*;

@SuppressWarnings({ "javadoc", "static-method" })//
public class IfEmptyThenEmptyElseTest {
  private static final IfEmptyThenEmptyElse WRING = new IfEmptyThenEmptyElse();
  private static final Statement INPUT = Into.s("{if (b) ; else ;}");
  private static final IfStatement IF = extract.firstIfStatement(INPUT);

  @Test public void eligible() {
    azzert.that(WRING.eligible(IF), is(true));
  }
  @Test public void emptyElse() {
    azzert.that(Is.vacuousElse(IF), is(true));
  }
  @Test public void emptyThen() {
    azzert.that(Is.vacuousThen(IF), is(true));
  }
  @Test public void extractFirstIf() {
    azzert.that(IF, notNullValue());
  }
  @Test public void inputType() {
    azzert.that(INPUT, instanceOf(Block.class));
  }
  @Test public void runGo() throws IllegalArgumentException, MalformedTreeException, BadLocationException {
    final String input = Wrap.Statement.on(INPUT + "");
    final Document d = new Document(input);
    final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from(d.get());
    final IfStatement s = extract.firstIfStatement(u);
    azzert.that(s, iz("if(b);else;"));
    final ASTRewrite r = ASTRewrite.create(u.getAST());
    final Suggestion t = WRING.make(s);
    t.go(r, null);
    final TextEdit e = r.rewriteAST(d, null);
    azzert.that(e, notNullValue());
    azzert.that(e.getChildren().length, greaterThan(0));
    e.apply(d);
    assertThat(d.get(), extract.firstIfStatement(ast.COMPILIATION_UNIT.from(d.get())), nullValue());
  }
  @Test public void scopeIncludes() {
    azzert.that(WRING.scopeIncludes(IF), is(true));
  }
}
