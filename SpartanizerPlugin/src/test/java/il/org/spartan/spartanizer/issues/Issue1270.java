package il.org.spartan.spartanizer.issues;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** Unit test for {@link ParameterAbbreviate}.
 * @author Ori Roth
 * @since 2017-04-21 */
public class Issue1270 extends TipperTest<SingleVariableDeclaration> {
  @Override public Tipper<SingleVariableDeclaration> tipper() {
    return new ParameterAbbreviate();
  }
  @Override public Class<SingleVariableDeclaration> tipsOn() {
    return SingleVariableDeclaration.class;
  }
  @Test public void a() {
    trimmingOf("IMoney addMoneyBag(MoneyBag s);").gives("IMoney addMoneyBag(MoneyBag b);");
  }
  @Test public void b() {
    trimmingOf("public abstract List<ReguessableValue> reguesses(AssumptionViolatedException a);") //
        .gives("public abstract List<ReguessableValue> reguesses(AssumptionViolatedException x);");
  }
}
