package il.org.spartan.spartanizer.tippers;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;

public class Issue835 {
  Tipper<Block> t = new CastBlockSingletonVariableDefinition();

  @Test public void descriptionNotNull() {
    assertNotNull(t.description());
  }

  @Test public void descriptionPrintBlockNotNull() {
    assertNotNull(t.description(az.block(wizard.ast("{int x;}"))));
  }

  @SuppressWarnings("static-method") @Test public void emptyBlock1() {
    assertEquals(az.block(wizard.ast("{}")).statements().size(), 0);
  }

  @SuppressWarnings("static-method") @Test public void emptyBlock2() {
    assertEquals(az.block(wizard.ast("\n{\n}\n")).statements().size(), 0);
  }

  @SuppressWarnings("static-method") @Test public void emptyBlock3() {
    assertEquals(az.block(wizard.ast("\n{int a;}\n")).statements().size(), 1);
  }
  // use t.tip instead of trimmer cause tip is probably unused by the
  // spartanizer at the moment
  @Test public void returnNullOnEmptyBlock1() {
    assertNull(t.tip(az.block(wizard.ast("{}"))));
  }
  @Test public void returnNullOnEmptyBlock2() {
    assertNull(t.tip(az.block(wizard.ast("\n{}\n"))));
  }
  @Test public void returnNullIfBlockIfNotSingleVarDef() {
    assertNull(t.tip(az.block(wizard.ast("{while(true){}}"))));
  }
  @Test public void returnNullIfBlockIfNotSingleVarDef2() {
    assertNull(t.tip(az.block(wizard.ast("{return 1;}"))));
  }
}
