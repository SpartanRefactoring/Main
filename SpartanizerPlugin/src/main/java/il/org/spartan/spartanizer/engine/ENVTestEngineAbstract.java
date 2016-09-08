package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;

import java.io.*;
import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.java.Environment.*;
import il.org.spartan.spartanizer.utils.*;

public abstract class ENVTestEngineAbstract {
  protected static LinkedHashSet<Entry<String, Environment.Information>> generateSet() {
    return new LinkedHashSet<>();
  }

  /** @param from - file path
   * @return CompilationUnit of the code written in the file specified. */
  public static ASTNode getCompilationUnit(final String from) {
    final String ROOT = "./src/test/java/il/org/spartan/spartanizer/java/";
    final File f = new File(ROOT + from);
    assert ROOT != null;
    assert from != null;
    assert f != null;
    azzert.aye(f.exists());
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(f);
    assert $ != null;
    azzert.that($, instanceOf(CompilationUnit.class));
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

  protected boolean foundTestedAnnotation = false; // Global flag, used to
                                                   // determine when to run the
                                                   // test on a node with
                                                   // potential annotations.
  protected ASTNode n = null;
  protected LinkedHashSet<Entry<String, Environment.Information>> testSet;

  /* Add new Entry to testSet from the inner annotation. */
  public void addTestSet(final List<MemberValuePair> ps) {
    final String s = wizard.asString(ps.get(0).getValue());
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
    // PrudentType t =
    // PrudentType.typeSwitch(wizard.asString(ps.get(1).getValue()),PrudentType.NOTHING);
    // add returns true iff the element did not exist in the set already.
    if (!testSet.add(new MapEntry<>(s.substring(1, s.length() - 1),
        new Information(PrudentType.prudentGenerateFromTypeName(wizard.asString(ps.get(1).getValue()))))))
      azzert.fail("Bad test file - an entity appears twice.");
  }

  protected abstract LinkedHashSet<Entry<String, Information>> buildEnvironmentSet(BodyDeclaration $);

  /** Compares the set from the annotation with the set that the checked
   * function generates.
   * @param $ */
  // Go over both sets in serial manner, and make sure every two members are
  // equal.
  // Also, check size, to avoid the case Set A is contained in B.
  // azzert.fail Otherwise.
  //
  // TODO Implement method. Currently awaits Yossi's advice regarding
  // LinkedHashSet unmodifiable issue.
  // TODO once the method is determined to be working, change to visibility to
  // protected.
  @SuppressWarnings("null") public void compareInOrder(final LinkedHashSet<Entry<String, Information>> $) {
    azzert.aye(testSet != null);
    azzert.aye($ != null);
    final Iterator<Entry<String, Information>> i = testSet.iterator();
    final Iterator<Entry<String, Information>> j = $.iterator();
    boolean flag = true;
    while (i.hasNext()) {
      final Entry<String, Information> testEntry = i.next();
      flag = false;
      while (j.hasNext()) {
        final Entry<String, Information> comparedEntry = j.next();
        if (comparedEntry.equals(testEntry)) {
          flag = true;
          break;
        }
      }
    }
    azzert.aye(flag);
  }

  /** Compares the set from the annotation with the set that the checked
   * function generates.
   * @param $ */
  // TODO once the method is determined to be working, change to visibility to
  // protected.
  @SuppressWarnings("null") public void compareOutOfOrder(final LinkedHashSet<Entry<String, Information>> $) {
    azzert.aye(testSet != null);
    azzert.aye($ != null);
    boolean flag;
    for (final Entry<String, Information> e1 : testSet) {
      flag = false;
      for (final Entry<String, Information> e2 : $)
        if (e1.getKey().equals(e2.getKey()) && e1.getValue().equals(e2.getValue())) {
          flag = true;
          break;
        }
      if (!flag)
        azzert.fail("Some of the annotations are not contained in the result.");
    }
    // if(!$.containsAll(testSet)) thats the correct implementation of the
    // method, that requires hashCode() function.
    // azzert.fail("Some of the annotations are not contained in the result.");
  }

  /** Parse the outer annotation to get the inner ones. Add to the flat Set.
   * Compare uses() and declares() output to the flat Set.
   * @param $ JD */
  protected abstract void handler(final Annotation ¢);

  /* define: outer annotation = OutOfOrderNestedENV, InOrderFlatENV, Begin, End.
   * define: inner annotation = Id. ASTVisitor that goes over the ASTNodes in
   * which annotations can be defined, and checks if the annotations are of the
   * kind that interests us. An array of inner annotations is defined inside of
   * each outer annotation of interest. I think it will be less error prone and
   * more scalable to implement another, internal, ASTVisitor that goes over
   * each inner annotation node, and send everything to an outside function to
   * add to the Sets as required. That means that each inner annotation will be
   * visited twice from the same outer annotation, but that should not cause
   * worry, since the outside visitor will do nothing. */
  public void runTest() {
    n.accept(new ASTVisitor() {
      /** Iterate over outer annotations of the current declaration and dispatch
       * them to handlers. otherwise */
      void checkAnnotations(final List<Annotation> as) {
        for (final Annotation ¢ : as)
          handler(¢);
      }

      @Override public boolean visit(final AnnotationTypeDeclaration $) {
        visitNodesWithPotentialAnnotations($);
        return true;
      }

      @Override public boolean visit(final AnnotationTypeMemberDeclaration $) {
        visitNodesWithPotentialAnnotations($);
        return true;
      }

      @Override public boolean visit(final EnumConstantDeclaration $) {
        visitNodesWithPotentialAnnotations($);
        return true;
      }

      @Override public boolean visit(final EnumDeclaration $) {
        visitNodesWithPotentialAnnotations($);
        return true;
      }

      @Override public boolean visit(final FieldDeclaration $) {
        visitNodesWithPotentialAnnotations($);
        return true;
      }

      @Override public boolean visit(final Initializer $) {
        visitNodesWithPotentialAnnotations($);
        return true;
      }

      @Override public boolean visit(final MethodDeclaration $) {
        visitNodesWithPotentialAnnotations($);
        return true;
      }

      @Override public boolean visit(final TypeDeclaration $) {
        visitNodesWithPotentialAnnotations($);
        return true;
      }

      void visitNodesWithPotentialAnnotations(final BodyDeclaration $) {
        checkAnnotations(extract.annotations($));
        if (foundTestedAnnotation) {
          final LinkedHashSet<Entry<String, Information>> enviromentSet = buildEnvironmentSet($);
          if (enviromentSet == null)
            return;
          compareOutOfOrder(enviromentSet);
          compareInOrder(enviromentSet);
          foundTestedAnnotation = false;
        }
      }
    });
  }
}