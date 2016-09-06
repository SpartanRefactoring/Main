package il.org.spartan.refactoring.handlers;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.java.*;
import il.org.spartan.refactoring.java.Environment.*;
import il.org.spartan.refactoring.utils.*;

public class EnvFlatHandler extends ENVTestEngineAbstract {
  Set<Entry<String, Environment.Information>> testSet;

  public EnvFlatHandler(final ASTNode $) {
    n = $;
    testSet = generateSet();
  }

  public EnvFlatHandler(final String ¢) {
    n = getCompilationUnit(¢);
    testSet = generateSet();
  }

  /** Parse the outer annotation to get the inner ones. Add to the flat Set.
   * Compare uses() and declares() output to the flat Set.
   * @param $ JD */
  @Override protected void handler(final SingleMemberAnnotation a) {
    if (a == null)
      return;
    a.accept(new ASTVisitor() {
      @SuppressWarnings("unchecked") List<MemberValuePair> values(final NormalAnnotation ¢) {
        return ¢.values();
      }

      @Override public boolean visit(final NormalAnnotation ¢) {
        /** Set<Entry<String, Information>> useSet =
         * Environment.uses(getDefinition(a)); Set<Entry<String, Information>>
         * declareSet = Environment.declares(getDefinition(a)); TODO: Run the
         * code above and compare to the corresponding testSets */
        if (isNameId(¢.getTypeName()))
          addValueToSet(values(¢));
        return true;
      }
    });
  }
}
