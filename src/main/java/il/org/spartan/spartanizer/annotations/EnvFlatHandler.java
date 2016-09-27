package il.org.spartan.spartanizer.annotations;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.Environment.*;

/* Implements the handler of flatEnv outer annotation. */
public final class EnvFlatHandler extends ENVTestEngineAbstract {
  // TODO: Dan, I (Alex) changed the constructors by pulling out some similar
  // code to initializers,
  // we shouldn't forget to test it as you tested the engine last time...
  // TODO: Dan, (Alex) we can leave comments like this to mark changes we want
  // the other to know.
  // before starting work we should run threw "TODO: myName"s, and afterwards
  // remove it them:)
  static {
    testSetFlat = generateSet();
  }
  {
    assert testSetFlat != null;
  }

  public EnvFlatHandler(final ASTNode $) {
    n = $;
    runTest();
  }

  public EnvFlatHandler(final String ¢) {
    n = getCompilationUnit(¢);
    runTest();
  }

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
      @SuppressWarnings("unchecked") List<MemberValuePair> values(final NormalAnnotation ¢) {
        return ¢.values();
      }

      @Override public boolean visit(final NormalAnnotation ¢) {
        if (isNameId(¢.getTypeName()))
          addTestSet(values(¢));
        return true;
      }
    });
  }
}
