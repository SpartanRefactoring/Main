package il.org.spartan.spartanizer.annotations;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.Environment.*;

/* Implements the handler of flatEnv outer annotation. */
public final class EnvFlatHandler extends ENVTestEngineAbstract {
  
  // TODO: Dan, I (Alex) changed the constructors by pulling out some similar code to initializers,
  // we shouldn't forget to test it as you tested the engine last time...
  static {
    testSet = generateSet();
  }
  
  {
    assert testSet != null;
  }
  
  public EnvFlatHandler(final ASTNode $) {
    n = $;
    runTest();
  }

  public EnvFlatHandler(final String ¢) {
    n = getCompilationUnit(¢);
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
    if (a == null || !"FlatEnvUse".equals(a.getTypeName() + ""))
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
