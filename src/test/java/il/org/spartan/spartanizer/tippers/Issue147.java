package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Kfir Marx
 * @since 2016-11-26 */
@SuppressWarnings("static-method")
@Ignore
public class Issue147 {
  private static final Statement INPUT = into.s("for(int i=0; i<5;++i){x.fuanc(); continue;}");
  private static final Statement INPUT1 = into.s("for(int i=0; i<5;++i){x.fuanc(); if(bool) continue;}");
  private static final ForStatement FOR = findFirst.forStatement(INPUT);
  private static final ForStatement FOR1 = findFirst.forStatement(INPUT1);
  private static final ForRedundantContinue TIPPER = new ForRedundantContinue();

  @Test public void a() {
    trimmingOf("for(int ¢=0; ¢<5;++¢){++¢; continue;}")//
        .gives("for(int ¢=0; ¢<5;++¢){++¢; }");//
  }

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
    assert TIPPER.canTip(FOR);
  }

  @Test public void extractFirstIf() {
    assert FOR != null;
  }

  @Test public void inputType() {
    azzert.that(FOR, instanceOf(ForStatement.class));
  }

  @Test public void notEligible() {
    assert !TIPPER.canTip(FOR1);
  }
}
