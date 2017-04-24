package il.org.spartan.spartanizer.testing;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.utils.*;

/** Unit tests for {@link IfEmptyThenEmptyElse}
 * @author Yossi Gil
 * @since Sep 25, 2016 */
@SuppressWarnings({ "javadoc", "static-method" })
public final class IfEmptyThenEmptyElseTest {
  private static final Statement INPUT = into.s("{if (b) ; else ;}");
  private static final IfStatement IF = findFirst.ifStatement(INPUT);
  private static final IfEmptyThenEmptyElse TIPPER = new IfEmptyThenEmptyElse();

  @Test public void eligible() {
    assert TIPPER.check(IF);
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

  @Test public void runGo() throws Exception {
    final String input = WrapIntoComilationUnit.Statement.on(INPUT + "");
    final IDocument d = new Document(input);
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(d.get());
    final IfStatement s = findFirst.ifStatement(u);
    azzert.that(s, iz("if(b);else;"));
    final ASTRewrite r = ASTRewrite.create(u.getAST());
    TIPPER.tip(s).go(r, null);
    final TextEdit e = r.rewriteAST(d, null);
    assert e != null;
    azzert.that(e.getChildren().length, greaterThan(0));
    e.apply(d);
    azzert.isNull(findFirst.ifStatement(makeAST.COMPILATION_UNIT.from(d.get())));
  }

  @Test public void scopeIncludes() {
    assert TIPPER.check(IF);
  }
}
