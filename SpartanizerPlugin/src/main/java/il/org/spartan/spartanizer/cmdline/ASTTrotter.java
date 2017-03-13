package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.engine.nominal.trivia.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;

/** An almost fully compatible, but more sane version of {@link ASTVisitor}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-10 */
public class ASTTrotter extends ASTVisitor {
  private boolean folding;
  private List<Rule<? extends ASTNode, ?>>[] dispatch;

  @SuppressWarnings("unchecked") private void init() {
    if (dispatch == null)
      dispatch = (List<Rule<? extends ASTNode, ?>>[]) new List<?>[nodeTypesCount()];
  }

  public <N extends ASTNode, T> void hook(final Class<N> c, final Rule<N, T> r) {
    init();
    final Integer nodeType = wizard.classToNodeType.get(c);
    assert nodeType != null : fault.specifically("Unrecongized class", c);
    get(nodeType.intValue());
    dispatch[nodeType.intValue()].add(r);
  }

  public boolean isFolding() {
    return folding;
  }

  @SuppressWarnings("unchecked") private <N extends ASTNode, T> Collection<Rule<N, T>> get(final N ¢) {
    @SuppressWarnings("rawtypes") final Collection $ = get(¢.getNodeType());
    return $;
  }

  private Collection<Rule<? extends ASTNode, ?>> get(final int ¢) {
    return dispatch[¢] = dispatch[¢] != null ? dispatch[¢] : new ArrayList<>();
  }

  protected void startFolding() {
    folding = true;
  }

  public ASTTrotter() {
    super(true);
  }

  @Override public final boolean preVisit2(final ASTNode ¢) {
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
    if (interesting(¢)) {
      ++interesting;
      record(squeeze(theSpartanizer.fixedPoint(removeComments(anonymize.code(¢ + "")))) + "\n");
    }
    return true;
  }

  @Override public boolean visit(final ReturnStatement ¢) {
    return process(¢);
  }

  private boolean process(final ASTNode ¢) {
    ++total;
    if (interesting(¢)) {
      ++interesting;
      record(squeeze(theSpartanizer.fixedPoint(removeComments(anonymize.code(¢ + "")))) + "\n");
    }
    return true;
  }

  @Override public boolean visit(final MethodDeclaration ¢) {
    ++total;
    if (interesting(¢)) {
      ++interesting;
      record(squeeze(theSpartanizer.fixedPoint(removeComments(anonymize.code(¢ + "")))) + "\n");
    }
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
}