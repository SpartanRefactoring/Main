package il.org.spartan.spartanizer.annotations;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.Environment.*;

public final class EnvNestedHandler extends ENVTestEngineAbstract {
  public EnvNestedHandler(final ASTNode $) {
    n = $;
    testSet = generateSet();
    runTest();
  }

  public EnvNestedHandler(final String ¢) {
    n = getCompilationUnit(¢);
    testSet = generateSet();
    runTest();
  }

  /* TODO Update EnvironmentCodeExamples - currently NestedENV does not
   * represent the expected results of neither Environment.uses or
   * Environment.declares. Should be the expected result of
   * Environment.declares.
   *
   * @see
   * il.org.spartan.spartanizer.engine.ENVTestEngineAbstract#buildEnvironmentSet
   * (org.eclipse.jdt.core.dom.BodyDeclaration) */
  @Override protected LinkedHashSet<Entry<String, Information>> buildEnvironmentSet(@SuppressWarnings("unused") final BodyDeclaration __) {
    return null;
  }

  @Override protected void handler(final Annotation ¢) {
    handler(az.singleMemberAnnotation(¢));
  }

  /** Parse the outer annotation to get the inner ones. Add to the flat Set.
   * Compare uses() and declares() output to the flat Set.
   * @param $ JD */
  void handler(final SingleMemberAnnotation a) {
    if (a == null || !"OutOfOrderflatENV".equals(a.getTypeName() + ""))
      return;
    foundTestedAnnotation = true;
    a.accept(new ASTVisitor() {
      // runs on the Ids
      @Override public boolean visit(final NormalAnnotation ¢) {
        if (isNameId(¢.getTypeName()))
          addTestSet(values(¢));
        return true;
      }

      @SuppressWarnings("unchecked") List<MemberValuePair> values(final NormalAnnotation ¢) {
        return ¢.values();
      }
    });
  }
}
