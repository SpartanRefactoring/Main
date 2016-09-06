package il.org.spartan.refactoring.handlers;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.java.*;
import il.org.spartan.refactoring.java.Environment.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.engine.*;

public class EnvFlatHandler extends ENVTestEngineAbstract{
  Set<Entry<String, Environment.Information>> testSet;
  
  public EnvFlatHandler(final ASTNode $) {
    n = $;
    testSet = generateSet();
  }

  public EnvFlatHandler(final String ¢) {
    n = getCompilationUnit(¢);
    testSet = generateSet();
  }
  // TODO: Information should be instantiated with PrudentType
  @Override protected void annotationToSet(final List<MemberValuePair> ps) {
    testSet.add(new MapEntry<>(wizard.asString(ps.get(0).getValue()), new Information()));
  }


  /** Compares output Set (testFlatENV) with provided set, that will be the
   * result of the flat version of defines.
   * @param $ */
  @Override protected void compareInOrder(final Set<Entry<String, Information>> $) {
    // Go over both sets in serial manner, and make sure every two members are
    // equal.
    // Also, check size, to avoid the case Set A is contained in B.
    // azzert.fail Otherwise.
  }

  /** Compares flat output Set (flat) with provided Set, that will be the result
   * of the flat version of defines.
   * @param $ */
  @Override protected void compareOutOfOrder(final Set<Entry<String, Information>> $) {
    // Check that each member of $ is contained in FlatENV, and that the size is
    // equal.
    // azzert.fail Otherwise.
  }
  
  /** Parse the outer annotation to get the inner ones. Add to the flat Set.
   * Compare uses() and declares() output to the flat Set.
   * @param $ JD */
  @Override protected void handler(final SingleMemberAnnotation a) {
    if (a == null)
      return;
    a.accept(new ASTVisitor() {
       @Override public boolean visit(NormalAnnotation ¢){
         /** Set<Entry<String, Information>> useSet = Environment.uses(getDefinition(a));
          * Set<Entry<String, Information>> declareSet = Environment.declares(getDefinition(a));
          * TODO: Run the code above and compare to the corresponding testSets */
         if (isNameId(¢.getTypeName()))
          addValueToSet(values(¢));
       return true;
       }

      @SuppressWarnings("unchecked")
      List<MemberValuePair> values(NormalAnnotation ¢) {
        return ¢.values();
      }
    });
  }
}
