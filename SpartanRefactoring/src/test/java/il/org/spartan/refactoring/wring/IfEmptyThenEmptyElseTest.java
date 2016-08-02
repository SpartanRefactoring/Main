package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;
import il.org.spartan.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;

@SuppressWarnings({ "javadoc", "static-method" }) //
public class IfEmptyThenEmptyElseTest {
  private static final IfEmptyThenEmptyElse WRING = new IfEmptyThenEmptyElse();
  private static final Statement INPUT = Into.s("{if (b) ; else ;}");
  private static final IfStatement IF = extract.firstIfStatement(INPUT);

  @Test public void eligible() {
    azzert.aye(WRING.eligible(IF));
  }
  @Test public void emptyElse() {
    azzert.aye(Is.vacuousElse(IF));
  }
  @Test public void emptyThen() {
    azzert.aye(Is.vacuousThen(IF));
  }
  @Test public void extractFirstIf() {
    azzert.notNull(IF);
  }
  @Test public void inputType() {
    org.hamcrest.MatcherAssert.assertThat(INPUT, instanceOf(Block.class));
  }
  @Test public void runGo() throws IllegalArgumentException, MalformedTreeException, BadLocationException {
    final String input = Wrap.Statement.on(INPUT + "");
    final Document d = new Document(input);
    final CompilationUnit u = (CompilationUnit) MakeAST.COMPILATION_UNIT.from(d.get());
    final IfStatement s = extract.firstIfStatement(u);
    azzert.that(s, iz("if(b);else;"));
    final ASTRewrite r = ASTRewrite.create(u.getAST());
    final Rewrite t = WRING.make(s);
    t.go(r, null);
    final TextEdit e = r.rewriteAST(d, null);
    azzert.notNull(e);
    azzert.that(e.getChildren().length, greaterThan(0));
    e.apply(d);
    azzert.isNull(extract.firstIfStatement(MakeAST.COMPILATION_UNIT.from(d.get())));
  }
  @Test public void scopeIncludes() {
    azzert.aye(WRING.scopeIncludes(IF));
  }
}
