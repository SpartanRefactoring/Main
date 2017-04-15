package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** Various tests upgraded from an old class replaced by
 * {@link LocalVariableUninitializedDead}
 * @author Yossi Gil
 * @since 2017-03-26 */
@SuppressWarnings("static-method")
public class Issue0835 {
  final Tipper<VariableDeclarationFragment> tipper = new LocalVariableUninitializedDead();

  @Test public void descriptionNotNull() {
    assert tipper.description() != null;
  }

  @Test public void emptyBlock1() {
    azzert.that(0, is(statements(az.block(make.ast("{}"))).size()));
  }

  @Test public void emptyBlock2() {
    azzert.that(0, is(statements(az.block(make.ast("\n{\n}\n"))).size()));
  }

  @Test public void emptyBlock3() {
    azzert.that(1, is(statements(az.block(make.ast("\n{int a;}\n"))).size()));
  }

  @Test public void returnNotNullNonEmptyBlock() {
    trimminKof("{int x;}")//
        .gives("int x;")//
        .gives("")//
        .stays();
  }

  /** Introduced by Yossi on Sun-Mar-26-18:52:18-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_whileTrue() {
    trimminKof("{ while (true) { } }") //
        .using(new BlockSingleton(), Block.class) //
        .gives("while(true){}") //
        .using(new WhileDeadRemove(), WhileStatement.class) //
        .gives("{}") //
        .gives("") //
        .stays() //
    ;
  }

  @Test public void returnNullIfBlockIfNotSingleVarDef2() {
    trimminKof("{return 1;}").gives("return 1;").stays();
  }

  @Test public void returnNullOnEmptyBlock1() {
    trimminKof("{}").gives("");
  }

  @Test public void returnNullOnEmptyBlock2() {
    trimminKof("\n{}\n").gives("");
  }
}
