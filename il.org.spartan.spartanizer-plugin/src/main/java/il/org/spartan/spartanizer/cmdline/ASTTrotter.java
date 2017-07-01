package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.engine.nominal.Trivia.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import an.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.good.*;
import il.org.spartan.utils.*;

/** An almost fully compatible, but more sane version of {@link ASTVisitor}
 * @author Yossi Gil
 * @since 2017-03-10 */
public class ASTTrotter extends ASTVisitor {
  private boolean folding;
  private List<Rule<? extends ASTNode, ?>>[] dispatch;

  @SuppressWarnings("unchecked") private void init() {
    if (dispatch == null)
      dispatch = (List<Rule<? extends ASTNode, ?>>[]) new List<?>[nodeTypesCount()];
  }
  protected <N extends ASTNode, T> ASTTrotter hookClassOnRule(final Class<N> c, final Rule<N, T> r) {
    init();
    get(wizard.nodeType(c)).add(r);
    return this;
  }
  public boolean isFolding() {
    return folding;
  }
  @SuppressWarnings("unchecked") private <N extends ASTNode, T> Collection<Rule<N, T>> get(final N ¢) {
    @SuppressWarnings("rawtypes") final Collection $ = get(¢.getNodeType());
    return $;
  }
  private Collection<Rule<? extends ASTNode, ?>> get(final int ¢) {
    return dispatch[¢] = dispatch[¢] != null ? dispatch[¢] : empty.list();
  }
  protected void startFolding() {
    folding = true;
  }
  public ASTTrotter() {
    super(true);
  }
  @Override public boolean preVisit2(final ASTNode ¢) {
    return !isFolding() && go(¢) != null;
  }
  @SuppressWarnings("unchecked") private <N extends ASTNode, T> T go(final N n) {
    return dispatch == null ? null : (T) get(n).stream().filter(λ -> λ.check(n)).map(λ -> λ.apply(n)).findFirst().orElse(null);
  }
  @SuppressWarnings("static-method") boolean interesting(@SuppressWarnings("unused") final ASTNode __) {
    return true;
  }
  @SuppressWarnings("static-method") boolean interesting(@SuppressWarnings("unused") final ExpressionStatement ¢) {
    return false;
  }
  @Override public boolean visit(final ExpressionStatement ¢) {
    ++total;
    if (!interesting(¢))
      return true;
    ++interesting;
    record(squeeze(theSpartanizer.repetitively(removeComments(JUnitTestMethodFacotry.code(¢ + "")))) + "\n");
    return true;
  }
  @Override public boolean visit(final ReturnStatement ¢) {
    return process(¢);
  }
  private boolean process(final ASTNode ¢) {
    ++total;
    if (!interesting(¢))
      return true;
    ++interesting;
    record(squeeze(theSpartanizer.repetitively(removeComments(JUnitTestMethodFacotry.code(¢ + "")))) + "\n");
    return true;
  }
  @Override public boolean visit(final MethodDeclaration ¢) {
    ++total;
    if (!interesting(¢))
      return true;
    ++interesting;
    record(squeeze(theSpartanizer.repetitively(removeComments(JUnitTestMethodFacotry.code(¢ + "")))) + "\n");
    return true;
  }
  @SuppressWarnings("boxing") protected void record(final String summary) {
    System.out.printf("%d/%d=%5.2f%% %s", interesting, total, 100. * interesting / total, summary);
  }
  @SuppressWarnings("static-method") boolean interesting(@SuppressWarnings("unused") final MethodDeclaration __) {
    return false;
  }

  private int interesting;
  private int total;

  public <N extends ASTNode> Hookable<N> on(final Class<N> c) {
    return new Hookable<N>() {
      @Override public Hookable<N> hook(final Rule<N, Object> ¢) {
        hookClassOnRule(c, ¢);
        return this;
      }
    };
  }
}

interface Hookable<T> {
  Hookable<T> hook(Rule<T, Object> o);
}