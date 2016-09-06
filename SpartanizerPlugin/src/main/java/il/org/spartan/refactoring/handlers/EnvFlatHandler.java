package il.org.spartan.refactoring.handlers;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.java.*;
import il.org.spartan.refactoring.java.Environment.*;

/*
 * Implements the handler of flatEnv outer annotation.
 */
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

  @Override protected Set<Entry<String, Information>> buildEnvironmentSet(final BodyDeclaration d) {
    return Environment.uses(d);
  }

  /** Parse the outer annotation to get the inner ones. Add to the flat Set.
   * Compare uses() and declares() output to the flat Set.
   * @param $ JD */
  @Override protected void handler(final SingleMemberAnnotation a) {
    assert a != null && !"@OutOfOrderflatENV".equals(a.getTypeName() + "");
    if (!"OutOfOrderflatENV".equals(a.getTypeName() + ""))
      return;
    foundTestedAnnotation = true;
    a.accept(new ASTVisitor() {
      @SuppressWarnings("unchecked") List<MemberValuePair> values(final NormalAnnotation ¢) {
        return ¢.values();
      }

      // runs on the Ids
      @Override public boolean visit(final NormalAnnotation ¢) {
        if (isNameId(¢.getTypeName()))
          addTestSet(values(¢));
        return true;
      }
    });
  }
}
