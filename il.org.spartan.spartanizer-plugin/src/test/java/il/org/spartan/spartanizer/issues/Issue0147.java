package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Kfir Marx
 * @since 2016-11-26 */
@SuppressWarnings("static-method")
public class Issue0147 {
  private static final Statement INPUT = parse.s("for(int i=0; i<5;++i){x.fuanc(); continue;}");
  private static final Statement INPUT1 = parse.s("for(int i=0; i<5;++i){x.fuanc(); if(bool) continue;}");
  private static final ForStatement FOR = findFirst.forStatement(INPUT);
  private static final ForStatement FOR1 = findFirst.forStatement(INPUT1);
  private static final ForRedundantContinue TIPPER = new ForRedundantContinue();

  @Test public void a$() {
    trimmingOf("for(int ¢=0; ¢<5;++¢){x.fuanc(); if(bool) continue;}")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("for (final Object o : os) {if (bool) return; continue;}")//
        .gives("for (final Object o : os) {if (bool) return; }");//
  }
  @Test public void b$() {
    trimmingOf("for(final Object o : os){x.fuanc(); if(bool) continue;}")//
        .stays();
  }
  @Test public void eligible() {
    assert TIPPER.check(FOR);
  }
  @Test public void extractFirstIf() {
    assert FOR != null;
  }
  @Test public void inputType() {
    azzert.that(FOR, instanceOf(ForStatement.class));
  }
  @Test public void notEligible() {
    assert !TIPPER.check(FOR1);
  }
  @Test public void a() {
    trimmingOf("for(int ¢=0; ¢<5;++¢){++¢; continue;}")//
        .gives("for(int ¢=0; ¢<5;++¢){++¢;}")//
        .gives("for(int ¢=0; ¢<5;++¢)++¢;")//
        .stays();
  }
}
