package il.org.spartan.athenizer.inflate.expanders;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
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

  @Override @SuppressWarnings("unchecked") public Tip tip(VariableDeclarationStatement ¢) {
    VariableDeclarationStatement $ = duplicate.of(¢), first = duplicate.of(¢);
    VariableDeclarationFragment fs = getFirstAssignment($);
    VariableDeclarationFragment ff = (VariableDeclarationFragment) first.fragments().get($.fragments().indexOf(fs));
    $.fragments().remove(fs);
    first.fragments().clear();
    first.fragments().add(ff);
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        @SuppressWarnings("static-access") ListRewrite l = r.getListRewrite(¢.getParent(), az.block(¢.getParent()).STATEMENTS_PROPERTY);
        l.insertAfter($, ¢, g);
        l.insertAfter(first, ¢, g);
        l.remove(¢, g);
      }
    };
  }

  @SuppressWarnings("unchecked") private static VariableDeclarationFragment getFirstAssignment(VariableDeclarationStatement ¢) {
    for (VariableDeclarationFragment $ : (List<VariableDeclarationFragment>) ¢.fragments())
      if (isFragmentApplicable($))
        return $;
    return null;
  }

  private static boolean isFragmentApplicable(VariableDeclarationFragment ¢) {
    return ¢.getInitializer() != null;
  }
}
