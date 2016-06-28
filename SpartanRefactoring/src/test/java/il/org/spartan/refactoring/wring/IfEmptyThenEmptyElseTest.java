package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.MatcherAssert.*;
import static il.org.spartan.hamcrest.JunitHamcrestWrappper.*;
import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import il.org.spartan.hamcrest.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;

@SuppressWarnings({ "javadoc", "static-method" })//
public class IfEmptyThenEmptyElseTest {
  private static final IfEmptyThenEmptyElse WRING = new IfEmptyThenEmptyElse();
  private static final Statement INPUT = Into.s("{if (b) ; else ;}");
  private static final IfStatement IF = Extract.firstIfStatement(INPUT);

  @Test public void eligible() {
    JunitHamcrestWrappper.assertTrue(WRING.eligible(IF));
  }
  @Test public void emptyElse() {
    JunitHamcrestWrappper.assertTrue(Is.vacuousElse(IF));
  }
  @Test public void emptyThen() {
    JunitHamcrestWrappper.assertTrue(Is.vacuousThen(IF));
  }
  @Test public void extractFirstIf() {
    JunitHamcrestWrappper.assertNotNull(IF);
  }
  @Test public void inputType() {
    org.hamcrest.MatcherAssert.assertThat(INPUT, instanceOf(Block.class));
  }
  @Test public void runGo() throws IllegalArgumentException, MalformedTreeException, BadLocationException {
    final String input = Wrap.Statement.on(INPUT + "");
    final Document d = new Document(input);
    final CompilationUnit u = (CompilationUnit) As.COMPILIATION_UNIT.ast(d.get());
    final IfStatement s = Extract.firstIfStatement(u);
    assertThat(s, iz("if(b);else;"));
    final ASTRewrite r = ASTRewrite.create(u.getAST());
    final Rewrite t = WRING.make(s);
    t.go(r, null);
    final TextEdit e = r.rewriteAST(d, null);
    JunitHamcrestWrappper.assertNotNull(e);
    assertThat(e.getChildren().length, greaterThan(0));
    e.apply(d);
    assertThat(d.get(), Extract.firstIfStatement(As.COMPILIATION_UNIT.ast(d.get())), nullValue());
  }
  @Test public void scopeIncludes() {
    JunitHamcrestWrappper.assertTrue(WRING.scopeIncludes(IF));
  }
}
