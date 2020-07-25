package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.is;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.tippers.BlockSingletonEliminate;
import il.org.spartan.spartanizer.tippers.LocalUninitializedDead;
import il.org.spartan.spartanizer.tippers.WhileDeadRemove;
import il.org.spartan.spartanizer.tipping.Tipper;

/** Various tests upgraded from an old class replaced by
 * {@link LocalUninitializedDead}
 * @author Yossi Gil
 * @since 2017-03-26 */
@SuppressWarnings("static-method")
public class Issue0835 {
  final Tipper<VariableDeclarationFragment> tipper = new LocalUninitializedDead();

  @Test public void descriptionNonNull() {
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
  @Test public void returnNonNullNonEmptyBlock() {
    trimmingOf("{int x;}")//
        .gives("int x;")//
        .gives("")//
        .stays();
  }
  /** Introduced by Yossi on Sun-Mar-26-18:52:18-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_whileTrue() {
    trimmingOf("{ while (true) { } }") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("while(true){}") //
        .using(new WhileDeadRemove(), WhileStatement.class) //
        .gives("{}") //
        .gives("") //
        .stays() //
    ;
  }
  @Test public void returnNullIfBlockIfNotSingleVarDef2() {
    trimmingOf("{return 1;}").gives("return 1;").stays();
  }
  @Test public void returnNullOnEmptyBlock1() {
    trimmingOf("{}").gives("");
  }
  @Test public void returnNullOnEmptyBlock2() {
    trimmingOf("\n{}\n").gives("");
  }
}
