package il.org.spartan.spartanizer.engine;

import static il.org.spartan.lisp.*;

import java.io.*;
import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.annotations.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.java.Environment.*;
import il.org.spartan.spartanizer.utils.*;

/** Abstract class for implementing specific Environment annotation based
 * testers.
 * @see EnvFlatHandler
 * @see EnvNestedHandler
 * @author Dan Greenstein
 * @author Alex Kopzon */
public abstract class ENVTestEngineAbstract {
  protected static LinkedHashSet<Entry<String, Information>> testSetFlat;
  protected static LinkedHashSet<Entry<String, Information>> testSetNested;

  /** Adds a new Entry to testSet from the inner annotation.
   * @param ps JD. */
  public static void addTestSet(final List<MemberValuePair> ps) {
    final String s = wizard.condense(first(ps).getValue());
    /* A call to an inner function of PrudentType that calls
     * typeSwitch(s,PrudentType.NOTHING) would be an improvement over the
     * current situation, but not ideal.
     *
     * An Ideal solution would be to add a
     * "boolean contains(PrudentType t1,PrudentType t2)" function, that will
     * return true iff type t1 is contained in type t2 - for example,
     * PrudentType.NUMERIC is contained in PrudentType.NOTNULL.
     *
     * Returning a direct comparison is far too error prone, and would be a bad
     * idea for a debug tool. */
    // add returns true iff the element did not exist in the set already.
    if (!testSetFlat.add(new MapEntry<>(s.substring(1, s.length() - 1), new Information(type.baptize(wizard.condense(second(ps).getValue()))))))
      azzert.fail("Bad test file - an entity appears twice.");
  }

  // change visibility to private.
  public static void compareFlat(final LinkedHashSet<Entry<String, Information>> ¢) {
    compareOutOfOrder(¢, testSetFlat);
    compareInOrder(¢, testSetFlat);
  }

  // only for testing.
  public static void compareFlatI(final LinkedHashSet<Entry<String, Information>> ¢) {
    compareInOrder(¢, testSetFlat);
  }

  // only for testing.
  public static void compareFlatO(final LinkedHashSet<Entry<String, Information>> ¢) {
    compareOutOfOrder(¢, testSetFlat);
  }

  /** Compares the given {@link LinkedHashSet} with the inner testSet.
   * Comparison done in-order. Assertion fails <b>iff</b> testSet is not
   * contained in the same order in the provided set.
   * @param $ JD
   * @return true iff the sets specified, are equally the same. */
  public static void compareInOrder(final LinkedHashSet<Entry<String, Information>> $, final LinkedHashSet<Entry<String, Information>> testSet) {
    assert testSet != null;
    assert $ != null;
    boolean entryFound = true;
    // TODO: Dan Greenstein: use or each loop here.
    final Iterator<Entry<String, Information>> j = $.iterator();
    for (final Entry<String, Information> ¢ : testSet) {
      entryFound = false;
      while (j.hasNext())
        if (¢.equals(j.next())) {
          entryFound = true;
          break;
        }
      assert entryFound : "some entry not found in order!";
    }
  }

  /** Compares the given {@link LinkedHashSet} with the inner testSet.
   * Comparison done out-of-order. Assertion fails <b>iff</b> testSet is not
   * contained in the provided set.
   * @param $ JD
   * @return true iff the specified {@link LinkedHashSet} contains testSet. */
  // TODO: Dan Greeenstein: once the method is determined to be working, change
  // to visibility
  // to
  // protected.
  public static void compareOutOfOrder(final LinkedHashSet<Entry<String, Information>> $, final LinkedHashSet<Entry<String, Information>> testSet) {
    assert $ != null;
    assert testSet != null;
    assert $.containsAll(testSet) : "some entry not found out of order!";
  }

  /** @param from - file path
   * @return CompilationUnit of the code written in the file specified. */
  public static ASTNode getCompilationUnit(final String from) {
    assert from != null;
    final String ROOT = "./src/test/java/il/org/spartan/spartanizer/java/";
    assert ROOT != null;
    final File f = new File(ROOT + from);
    assert f != null;
    assert f.exists();
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(f);
    assert $ != null;
    // azzert.that($, instanceOf(CompilationUnit.class));
    return $;
  }

  /** Determines that we have got to the correct Annotation
   * @param n1
   * @return */
  public static boolean isNameId(final Name n1) {
    assert !"@Id".equals(n1 + ""); // To find the bug, if it appears as @Id, and
                                   // not Id.
    return "Id".equals(n1 + "");
  }

  public static void testSetsReset() {
    if (testSetFlat != null)
      testSetFlat.clear();
    if (testSetNested != null)
      testSetNested.clear();
  }

  protected static LinkedHashSet<Entry<String, Environment.Information>> generateSet() {
    return new LinkedHashSet<>();
  }

  protected boolean foundTestedAnnotation; // Global flag, used to
  // determine when to system the
  // test on a node with
  // potential annotations.
  protected ASTNode n;

  /** define: outer annotation = OutOfOrderNestedENV, InOrderFlatENV, Begin,
   * End. define: inner annotation = Id. ASTVisitor that goes over the ASTNodes
   * in which annotations can be defined, and checks if the annotations are of
   * the kind that interests us. An array of inner annotations is defined inside
   * of each outer annotation of interest. I think it will be less error prone
   * and more scalable to implement another, internal, ASTVisitor that goes over
   * each inner annotation node, and send everything to an outside function to
   * add to the Sets as required. That means that each inner annotation will be
   * visited twice from the same outer annotation, but that should not cause
   * worry, since the outside visitor will do nothing. */
  public void runTest() {
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final AnnotationTypeDeclaration ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      @Override public boolean visit(final AnnotationTypeMemberDeclaration ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      @Override public boolean visit(final EnumConstantDeclaration ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      @Override public boolean visit(final EnumDeclaration ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      @Override public boolean visit(final FieldDeclaration ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      @Override public boolean visit(final Initializer ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      @Override public boolean visit(final MethodDeclaration ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      @Override public boolean visit(final TypeDeclaration ¢) {
        visitNodesWithPotentialAnnotations(¢);
        return true;
      }

      /** Iterate over outer annotations of the current declaration and dispatch
       * them to handlers. otherwise */
      void checkAnnotations(final List<Annotation> as) {
        for (final Annotation ¢ : as)
          handler(¢);
      }

      void visitNodesWithPotentialAnnotations(final BodyDeclaration $) {
        checkAnnotations(extract.annotations($));
        if (!foundTestedAnnotation)
          return;
        final LinkedHashSet<Entry<String, Information>> enviromentSet = buildEnvironmentSet($);
        if (enviromentSet == null)
          return;
        compareFlat(enviromentSet);
        // compareNested(enviromentSet);
        testSetsReset();
        foundTestedAnnotation = false;
      }
    });
  }

  protected abstract LinkedHashSet<Entry<String, Information>> buildEnvironmentSet(BodyDeclaration $);

  /** Parse the outer annotation to get the inner ones. Add to the flat Set.
   * Compare uses() and declares() output to the flat Set.
   * @param $ JD */
  protected abstract void handler(final Annotation ¢);
}