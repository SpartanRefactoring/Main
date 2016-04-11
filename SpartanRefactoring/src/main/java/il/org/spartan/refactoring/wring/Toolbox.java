package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/**
 * A class that contains a bunch of {@link Wring} objects, allowing selecting
 * and applying the most appropriate such object for a given {@link ASTNode}.
 *
 * @author Yossi Gil
 * @since 2015-08-22
 */
public class Toolbox {
  private static <N extends ASTNode> Wring<N> find(final N n, final List<Wring<N>> ws) {
    for (final Wring<N> $ : ws)
      if ($.scopeIncludes(n))
        return $;
    return null;
  }
  private final Map<Class<? extends ASTNode>, List<Object>> inner = new HashMap<>();
  /**
   * Find the first {@link Wring} appropriate for an {@link ASTNode}
   *
   * @param n JD
   * @return the first {@link Wring} for which the parameter is within scope, or
   *         <code><b>null</b></code>i if no such {@link Wring} is found.
   */
  public <N extends ASTNode> Wring<N> find(final N n) {
    return find(n, get(n));
  }
  @SuppressWarnings("unchecked") <N extends ASTNode> List<Wring<N>> get(final Class<? extends ASTNode> n) {
    if (!inner.containsKey(n))
      inner.put(n, new ArrayList<>());
    return (List<Wring<N>>) (List<?>) inner.get(n);
  }
  <N extends ASTNode> List<Wring<N>> get(final N n) {
    return get(n.getClass());
  }
  /**
   * Initialize this class' internal instance object
   */
  public static void generate() {
    instance = new Maker()//
        .add(Assignment.class, //
            new AssignmentAndAssignment(), //
            new AssignmentAndReturn(), //
            null) //
        .add(Block.class, //
            new BlockSimplify(), //
            new BlockSingleton(), //
            null) //
        .add(PostfixExpression.class, new PostfixToPrefix()) //
        .add(InfixExpression.class, //
            new InfixDivisionMultiplicationNegatives(), //
            new InfixSortAddition(), //
            new InfixComparisonBooleanLiteral(), //
            new InfixConditionalAndTrue(), //
            new InfixConditionalOrFalse(), //
            new InfixComparisonSpecific(), //
            new InfixSortMultiplication(), //
            new InfixSortPseudoAddition(), //
            new InfixSortSubstraction(), //
            new InfixSortDivision(), //
            new InfixConditionalCommon(), //
            null)
        .add(MethodDeclaration.class, //
            new MethodRenameReturnToDollar(), //
            new MethodRemoveDegenerateOverride(), //
            null)
        .add(MethodInvocation.class, //
            new BooleanConstants(), //
            new StringFromStringBuilder(), //
            null) //
        .add(SingleVariableDeclaration.class, //
            new SingleVariableDeclarationAbbreviation(), //
            null)
        .add(VariableDeclarationFragment.class, //
            new DeclarationAssignment(), //
            new DeclarationInitialiazerAssignment(), //
            new DeclarationInitialiazelUpdateAssignment(), //
            new DeclarationInitializerIfAssignment(), //
            new DeclarationInitializerIfUpdateAssignment(), //
            new DeclarationInitializerReturnVariable(), //
            new DeclarationInitializerReturnExpression(), //
            new DeclarationInitializerReturnAssignment(), //
            new DeclarationInitializerReturnUpdateAssignment(), //
            new DeclarationInitializerStatementTerminatingScope(), //
            null) //
        .add(IfStatement.class, //
            new IfLastInMethodThenEndingWithEmptyReturn(), //
            new IfLastInMethodElseEndingWithEmptyReturn(), //
            new IfLastInMethod(), //
            new IfReturnFooElseReturnBar(), //
            new IfReturnNoElseReturn(), //
            new IfAssignToFooElseAssignToFoo(), //
            new IfThenFooBarElseFooBaz(), //
            new IfBarFooElseBazFoo(), //
            new IfThrowFooElseThrowBar(), //
            new IfThrowNoElseThrow(), //
            new IfExpressionStatementElseSimilarExpressionStatement(), //
            new IfThenOrElseIsCommandsFollowedBySequencer(), //
            new IfFooSequencerIfFooSameSequencer(), //
            new IfCommandsSequencerNoElseSingletonSequencer(), //
            new IfThenIfThenNoElseNoElse(), //
            new IfEmptyThenEmptyElse(), //
            new IfDegenerateElse(), //
            new IfEmptyThen(), //
            new IfShortestFirst(), //
            null)//
        .add(PrefixExpression.class, //
            new PrefixIncrementDecrementReturn(), //
            new PrefixNotPushdown()) //
        .add(ConditionalExpression.class, //
            new TernaryBooleanLiteral(), //
            new TernaryCollapse(), //
            new TernaryEliminate(), //
            new TernaryShortestFirst(), //
            new TernaryPushdown(), //
            null) //
        .add(NormalAnnotation.class, //
            new AnnotationDiscardValueName(), //
            new AnnotationRemoveEmptyParentheses(), //
            null) //
        .add(SuperConstructorInvocation.class, new SuperConstructorInvocationRemover()) //
        .add(ReturnStatement.class, new ReturnLastInMethod()) //
        .add(ClassInstanceCreation.class, new WrapperReplaceWithFactory()) //
        .seal();
  }
  public static Toolbox instance() {
    return instance;
  }
  /** The default instance of this class */
  static Toolbox instance;

  /**
   * A builder for the enclosing class.
   *
   * @author Yossi Gil
   * @since 2015-08-22
   */
  public static class Maker extends Toolbox {
    /**
     * Associate a bunch of{@link Wring} with a given sub-class of
     * {@link ASTNode}.
     *
     * @param c JD
     * @param ws JD
     * @return <code><b>this</b></code>, for easy chaining.
     */
    @SafeVarargs public final <N extends ASTNode> Maker add(final Class<N> c, final Wring<N>... ws) {
      final List<Wring<N>> l = get(c);
      for (final Wring<N> w : ws) {
        if (w == null)
          break;
        if (!w.wringGroup().isEnabled())
          continue;
        l.add(w);
      }
      return this;
    }
    /**
     * Terminate a fluent API chain.
     *
     * @return the newly created object
     */
    public Toolbox seal() {
      return this;
    }
  }
}
