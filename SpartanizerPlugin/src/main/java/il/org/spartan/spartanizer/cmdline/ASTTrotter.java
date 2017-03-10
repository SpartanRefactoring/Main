package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.spartanizer.ast.navigate.trivia.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** An almost fully compatible, but more sane version of {@link ASTVisitor}
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-10 */
public class ASTTrotter extends ASTVisitor {
  private boolean folding;
  private List<Rule<? extends ASTNode, ?>>[] dispatch;

  @SuppressWarnings("unchecked") private void init() {
    if (dispatch == null)
      dispatch = (List<Rule<?, ?>>[]) new List<?>[nodeTypesCount()];
  }

  public <N extends ASTNode, T> void hook(Class<N> c, Rule<N, T> r) {
    init();
    Integer nodeType = wizard.classToNodeType.get(c);
    assert nodeType != null : fault.specifically("Unrecongized class", c);
    get(nodeType.intValue());
    dispatch[nodeType.intValue()].add(r);
  }

  public boolean isFolding() {
    return folding;
  }

  @SuppressWarnings({ "cast", "unchecked" }) private <N extends ASTNode, T> Collection<Rule<N, T>> get(N ¢) {
    @SuppressWarnings("rawtypes") Collection $ = get(¢.getNodeType());
    return (Collection<Rule<N, T>>) $;
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

  @Override public final boolean preVisit2(ASTNode ¢) {
    return !isFolding() && go(¢) != null;
  }

  @SuppressWarnings("unchecked") private <N extends ASTNode, T> T go(N n) {
    return dispatch == null ? null : (T) get(n).stream().filter(λ -> λ.interesting(n)).map(λ -> λ.tip(n)).findFirst().orElse(null);
  }

  @SuppressWarnings("static-method") boolean interesting(@SuppressWarnings("unused") ASTNode __) {
    return true;
  }

  @SuppressWarnings("static-method") boolean interesting(@SuppressWarnings("unused") ExpressionStatement ¢) {
    return false;
  }

  @Override public boolean visit(final ExpressionStatement ¢) {
    ++total;
    if (interesting(¢)) {
      ++interesting;
      record(squeezeSpaces(theSpartanizer.fixedPoint(removeComments(normalize.code(¢ + "")))) + "\n");
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
      record(squeezeSpaces(theSpartanizer.fixedPoint(removeComments(normalize.code(¢ + "")))) + "\n");
    }
    return true;
  }

  @Override public boolean visit(final MethodDeclaration ¢) {
    ++total;
    if (interesting(¢)) {
      ++interesting;
      record(squeezeSpaces(theSpartanizer.fixedPoint(removeComments(normalize.code(¢ + "")))) + "\n");
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