package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

public final class SwitchEmpty extends ReplaceCurrentNode<SwitchStatement> implements TipperCategory.Collapse {

  @Override public ASTNode replacement(@SuppressWarnings("unused") SwitchStatement __) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public String description(@SuppressWarnings("unused") SwitchStatement __) {
    // TODO Auto-generated method stub
    return null;
  }

}
