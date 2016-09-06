package il.org.spartan.refactoring.engine;

import static il.org.spartan.azzert.*;

import java.io.*;
import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.java.*;
import il.org.spartan.refactoring.java.Environment.*;
import il.org.spartan.refactoring.utils.*;

public abstract class ENVTestEngineAbstract {
  enum AnnotationType {
    FLAT, NESTED, BEGINEND
  }

  protected static Set<Entry<String, Environment.Information>> generateSet() {
    return Collections.unmodifiableSet(new HashSet<>());
  }

  /** @param from - file path
   * @return CompilationUnit of the code written in the file specified. */
  protected static ASTNode getCompilationUnit(final String from) {
    final String ROOT = "./src/test/resources/";
    final File f = new File(ROOT + from);
    azzert.notNull(ROOT);
    azzert.notNull(from);
    azzert.notNull(f);
    azzert.aye(f.exists());
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(f);
    azzert.notNull($);
    azzert.that($, instanceOf(CompilationUnit.class));
    return $;
  }

  public static boolean isNameId(final Name n1) {
    assert(!"Id".equals("" + n1)); //debug
    return "@Id".equals("" + n1);
  }

  protected ASTNode n = null;
  Set<Entry<String, Environment.Information>> testSet;

  // TODO: Information should be instantiated with PrudentType
  public void addValueToSet(final List<MemberValuePair> ps) {
    testSet.add(new MapEntry<>(wizard.asString(ps.get(0).getValue()), new Information()));
  }

  /** Compares the set from the annotation with the set that the checked function generates.
   * @param $ */
  // Go over both sets in serial manner, and make sure every two members are
  // equal.
  // Also, check size, to avoid the case Set A is contained in B.
  // azzert.fail Otherwise.
  protected void compareInOrder(final Set<Entry<String, Information>> $) {
  }

  /** Compares the set from the annotation with the set that the checked function generates.
   * @param $ */
  // Check that each member of $ is contained in FlatENV, and that the size is
  // equal.
  // azzert.fail Otherwise.
  protected void compareOutOfOrder(final Set<Entry<String, Information>> $) {
  }

  /** Cast
   * @param ¢ JD */
  void handler(final Annotation ¢) {
    handler(az.singleMemberAnnotation(¢));
  }

  /**
   * Parse the outer annotation to get the inner ones. Add to the flat Set. Compare uses() and declares() output to the flat Set.
   * @param $  JD 
   */
  protected abstract void handler(final SingleMemberAnnotation $);

  /* define: outer annotation = OutOfOrderNestedENV, InOrderFlatENV, Begin, End.
   * define: inner annotation = Id. ASTVisitor that goes over the ASTNodes in
   * which annotations can be defined, and checks if the annotations are of the
   * kind that interests us. An array of inner annotations is defined inside of
   * each outer annotation of interest. I think it will be less error prone and
   * more scalable to implement another, internal, ASTVisitor that will go over
   * each inner annotation node, and send everything to an outside function to
   * add to the Sets as required. That means that each inner annotation will be
   * visited twice from the same outer annotation, but that should not cause
   * worry, since the outside visitor will do nothing.
   *
   * TODO: internal node parsing. Think about Nested parsing. */
  public void runTest() {
    n.accept(new ASTVisitor() {
      /** Iterate over annotations of the current declaration and dispatch them
       * to handlers. otherwise */
      void testAnnotations(final List<Annotation> as) {
        for (final Annotation ¢ : as)
          handler(¢);
      }

      /** TODO: only MothodDeclaration is implemented. Should implement all
       * nodes which can have annotations. */
      @Override public boolean visit(final MethodDeclaration d) {
        testAnnotations(extract.annotations(d));
        testSet.clear();
        return true;
      }
    });
  }
}