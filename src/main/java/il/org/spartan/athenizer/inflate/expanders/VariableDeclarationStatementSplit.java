package il.org.spartan.athenizer.inflate.expanders;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.tipping.*;

public class VariableDeclarationStatementSplit extends CarefulTipper<VariableDeclarationStatement> {
  @Override public String description(@SuppressWarnings("unused") VariableDeclarationStatement __) {
    return "Split initialization statement";
  }

  @Override @SuppressWarnings("unchecked") protected boolean prerequisite(VariableDeclarationStatement s) {
    int $ = 0;
    for (VariableDeclarationFragment ¢ : (List<VariableDeclarationFragment>) s.fragments())
      if (isFragmentApplicable(¢))
        ++$;
    return $ >= 2;
  }

  private static boolean isFragmentApplicable(VariableDeclarationFragment ¢) {
    return ¢.getInitializer() != null;
  }
}
