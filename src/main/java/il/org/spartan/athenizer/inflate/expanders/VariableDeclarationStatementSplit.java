package il.org.spartan.athenizer.inflate.expanders;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.tipping.*;

public class VariableDeclarationStatementSplit extends CarefulTipper<VariableDeclarationStatement> {
  @Override public String description(@SuppressWarnings("unused") VariableDeclarationStatement __) {
    return "Split initialization statement";
  }

  @SuppressWarnings("unchecked") @Override protected boolean prerequisite(VariableDeclarationStatement s) {
    int assignmentsCounter = 0;
    for (VariableDeclarationFragment ¢ : (List<VariableDeclarationFragment>) s.fragments())
      if (isFragmentApplicable(¢))
        ++assignmentsCounter;
    return assignmentsCounter >= 2;
  }

  private static boolean isFragmentApplicable(VariableDeclarationFragment ¢) {
    return ¢.getInitializer() != null;
  }
}
