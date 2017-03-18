package il.org.spartan.spartanizer.tipping;

import static il.org.spartan.spartanizer.engine.nominal.trivia.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** An almost fully compatible, but more sane version of {@link ASTVisitor}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-10 */
public class ASTTrotter extends ASTVisitor {
  /** Instantiates this class to visit also {@link Javadoc} elements. */
  public ASTTrotter() {
    super(true);
  }

  public <N extends ASTNode> Hookable<N> on(final Class<N> c) {
    return new Hookable<N>() {
      @Override public Hookable<N> hook(final Rule<N, Object> ¢) {
        init();
        final Integer nodeType = wizard.classToNodeType.get(c);
        assert nodeType != null : fault.specifically("Unrecongized class", c);
        get(nodeType.intValue()).add(¢);
        return this;
      }
    };
  }
  private int total;
  private int interesting;

  private Collection<Rule<? extends ASTNode, ?>> get(final int ¢) {
    return dispatch[¢] = dispatch[¢] != null ? dispatch[¢] : new ArrayList<>();
  }

  @SuppressWarnings("unchecked") private <N extends ASTNode, T> Collection<Rule<N, T>> get(final N ¢) {
    @SuppressWarnings("rawtypes") final Collection $ = get(¢.getNodeType());
    return $;
  }

  @SuppressWarnings("unchecked") private <N extends ASTNode, T> T go(final N n) {
    return dispatch == null ? null : (T) get(n).stream().filter(λ -> λ.check(n)).map(λ -> λ.apply(n)).findFirst().orElse(null);
  }

  protected <N extends ASTNode, T> ASTTrotter hookRuleOnClass(final Class<N> c, final Rule<N, T> r) {
    init();
    final Integer nodeType = wizard.classToNodeType.get(c);
    assert nodeType != null : fault.specifically("Unrecongized class", c);
    get(nodeType.intValue()).add(r);
    return this;
  }

  @SuppressWarnings("unchecked") private void init() {
    if (dispatch == null)
      dispatch = (List<Rule<? extends ASTNode, ?>>[]) new List<?>[nodeTypesCount()];
  }

  @SuppressWarnings("static-method") protected boolean interesting(@SuppressWarnings("unused") final MethodDeclaration __) {
    return false;
  }

  @Override public final boolean preVisit2(final ASTNode ¢) {
    return go(¢) != null;
  }

  private boolean process(final ASTNode ¢) {
    ++total;
    if (interesting(¢)) {
      ++interesting;
      record(squeeze(theSpartanizer.fixedPoint(removeComments(anonymize.code(¢ + "")))) + "\n");
    }
    return true;
  }

  private boolean interesting(final ASTNode ¢) {
    return false;
  }

  @SuppressWarnings("boxing") protected void record(final String summary) {
    System.out.printf("%d/%d=%5.2f%% %s", interesting, total, 100. * interesting / total, summary);
  }

  @Override public boolean visit(final ExpressionStatement ¢) {
    ++total;
    if (interesting(¢)) {
      ++interesting;
      record(squeeze(theSpartanizer.fixedPoint(removeComments(anonymize.code(¢ + "")))) + "\n");
    }
    return true;
  }

  @SuppressWarnings("static-method")
  private boolean interesting(final ExpressionStatement ¢) {
    return false;
  }

  @Override public boolean visit(final MethodDeclaration ¢) {
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

  private List<Rule<? extends ASTNode, ?>>[] dispatch;

  /** FAPI of {@link ASTTrotter#on(Class)}
   * @param <T> JD
   * @author Yossi Gil {@code yossi.gil@gmail.com}
   * @since 2017-03-18 */
  @FunctionalInterface
  public interface Hookable<T> {
    Hookable<T> hook(Rule<T, Object> o);
  }
}
