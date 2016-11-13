package il.org.spartan.spartanizer.tippers;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
// import il.org.spartan.spartanizer.ast.navigate.*;
// import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;

// wizard.ast("<some code>"); // creating an ASTNode
// az.block(null); // casting an ASTNode to block
// TrimmerTestsUtils.trimmingOf("if (true) x = x+1;").gives("x += 1;"); //
// checking spartanization
// TrimmerTestsUtils.trimmingOf("<some code>").stays(); // checking no.
// spartanization
public class Issue835 {
  Tipper<Block> t = new CastBlockSingletonVariableDefinition();

  @Test public void descriptionNotNull() {
    assertNotNull(t.description());
  }
  @Test public void descriptionPrintBlockNotNull() {
    assertNotNull(t.description(az.block(wizard.ast("{int x;}"))));
  }
}
