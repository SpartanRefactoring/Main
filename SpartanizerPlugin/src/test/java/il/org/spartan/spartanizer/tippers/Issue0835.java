/** TODO: Yossi Gil please add a description
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since Jan 6, 2017 */
package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

public class Issue0835 {
  final Tipper<Block> t = new BlockSingletonVariableDeclarationStatementRemoveBuggy();

  @Test public void descriptionNotNull() {
    assert t.description() != null;
  }

  @Test public void descriptionPrintBlockNotNull() {
    assert t.description(az.block(wizard.ast("{int x;}"))) != null;
  }

  @Test @SuppressWarnings("static-method") public void emptyBlock1() {
    azzert.that(0, is(statements(az.block(wizard.ast("{}"))).size()));
  }

  @Test @SuppressWarnings("static-method") public void emptyBlock2() {
    azzert.that(0, is(statements(az.block(wizard.ast("\n{\n}\n"))).size()));
  }

  @Test @SuppressWarnings("static-method") public void emptyBlock3() {
    azzert.that(1, is(statements(az.block(wizard.ast("\n{int a;}\n"))).size()));
  }

  @Test public void returnNotNullNonEmptyBlock() {
    assert t.tip(az.block(wizard.ast("{int x;}"))) != null;
  }

  @Test public void returnNullIfBlockIfNotSingleVarDef() {
    azzert.isNull(t.tip(az.block(wizard.ast("{while(true){}}"))));
  }

  @Test public void returnNullIfBlockIfNotSingleVarDef2() {
    azzert.isNull(t.tip(az.block(wizard.ast("{return 1;}"))));
  }

  // use t.tip instead of trimmer cause tip is probably unused by the
  // spartanizer at the moment
  @Test public void returnNullOnEmptyBlock1() {
    azzert.isNull(t.tip(az.block(wizard.ast("{}"))));
  }

  @Test public void returnNullOnEmptyBlock2() {
    azzert.isNull(t.tip(az.block(wizard.ast("\n{}\n"))));
  }
}
