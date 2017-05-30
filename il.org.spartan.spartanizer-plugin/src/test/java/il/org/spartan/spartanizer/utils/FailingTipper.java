package il.org.spartan.spartanizer.utils;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Fails on every operation, used to test testing mechanisms.
 * @author Ori Roth
 * @since 2017-04-13 */
@SuppressWarnings("unused")
public class FailingTipper<N extends ASTNode> extends Tipper<N> implements Nominal.Abbreviation {
  private static final long serialVersionUID = 0x7FE33D08914707AFL;

  @Override public boolean canTip(final N __) {
    return true;
  }
  @Override public String description(final N __) {
    return "tipper that always apply and always fail, used for testing";
  }
  @Override public Tip tip(final N ¢) {
    throw new RuntimeException("thown on purpose, see FailingTipper");
  }
}
