package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.spartanizations.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class IfEmptyThenEmptyElseTest {
  private static final Statement INPUT = into.s("{if (b) ; else ;}");
  private static final IfStatement IF = extract.firstIfStatement(INPUT);
  private static final IfEmptyThenEmptyElse WRING = new IfEmptyThenEmptyElse();

  @Test public void eligible() {
    azzert.aye(WRING.canMake(IF));
  }

  @Test public void emptyElse() {
    assert iz.vacuousElse(IF);
  }

  @Test public void emptyThen() {
    assert iz.vacuousThen(IF);
  }

  @Test public void extractFirstIf() {
    assert IF != null;
  }

  @Test public void inputType() {
    azzert.that(INPUT, instanceOf(Block.class));
  }

  @Test public void runGo() throws IllegalArgumentException, MalformedTreeException, BadLocationException {
    final String input = Wrap.STATEMENT_OR_SOMETHING_THAT_MAY_APPEAR_IN_A_METHOD.on(INPUT + "");
    final Document d = new Document(input);
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(d.get());
    final IfStatement s = extract.firstIfStatement(u);
    azzert.that(s, iz("if(b);else;"));
    final ASTRewrite r = ASTRewrite.create(u.getAST());
    final Rewrite t = WRING.make(s);
    t.go(r, null);
    final TextEdit e = r.rewriteAST(d, null);
    assert e != null;
    azzert.that(e.getChildren().length, greaterThan(0));
    e.apply(d);
    azzert.isNull(extract.firstIfStatement(makeAST.COMPILATION_UNIT.from(d.get())));
  }

  @Test public void scopeIncludes() {
    azzert.aye(WRING.claims(IF));
  }
}
