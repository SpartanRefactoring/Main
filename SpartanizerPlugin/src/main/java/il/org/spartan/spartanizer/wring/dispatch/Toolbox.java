package il.org.spartan.spartanizer.wring.dispatch;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.wring.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** Singleton containing all {@link Wring}s which are active, allowing selecting
 * and applying the most appropriate such object for a given {@link ASTNode}.
 * @author Yossi Gil
 * @since 2015-08-22 */
public class Toolbox {
  /** The default instance of this class */
  static Toolbox instance;

  public static Toolbox defaultInstance() {
    return instance;
  }

  /** Make a {@link Toolbox} for a specific kind of wrings
   * @param clazz JD
   * @param w JS
   * @return a new instance containing only the wrings passed as parameter */
  @SafeVarargs public static <N extends ASTNode> Toolbox make(final Class<N> clazz, final Wring<N>... ns) {
    return new Maker().add(clazz, ns);
  }

  /** Initialize this class' internal instance object */
  public static void refresh() {
    instance = new Maker()//
        .add(SuperConstructorInvocation.class, new SuperConstructorInvocationRemover()) //
        .add(EnhancedForStatement.class, new EnhancedForRenameParameterToCent()) //
        .add(ReturnStatement.class, new ReturnLastInMethod()) //
        .add(AnnotationTypeMemberDeclaration.class, new AbstractBodyDeclarationSortModifiers.ofAnnotationTypeMember()) //
        .add(AnnotationTypeDeclaration.class, new AbstractBodyDeclarationSortModifiers.ofAnnotation()) //
        .add(Assignment.class, //
            new AssignmentAndAssignment(), //
            new AssignmentAndReturn(), //
            new AssignmentToFromInfixIncludingTo(), //
            new AssignmentToPostfixIncrement(), //
            null) //
        .add(Block.class, //
            new BlockSimplify(), //
            new BlockSingleton(), //
            null) //
        .add(PostfixExpression.class, //
            new PostfixToPrefix(), //
            null) //
        .add(InfixExpression.class, //
            /* The following line was intentionally commented: Matteo, I believe
             * this generates many bugs --yg Bug Fixed, but not integrated, as
             * per request. Waiting for the enhancement (Term, Factor, etc.) --
             * mo */
            // new InfixMultiplicationDistributive(), //
            new InfixMultiplicationEvaluate(), //
            new InfixDivisionEvaluate(), //
            new InfixRemainderEvaluate(), //
            new InfixComparisonSizeToZero(), //
            new InfixSubtractionZero(), //
            new InfixAdditionSubtractionExpand(), //
            new InfixEmptyStringAdditionToString(), //
            new InfixConcatenationEmptyStringLeft(), //
            new InfixFactorNegatives(), //
            new InfixAdditionEvaluate(), //
            // new ConcatStrings(), //removed for now so it won't break tests,
            // see issue #120
            new InfixSubractionEvaluate(), //
            //TODO: Yossi, InfixTermsZero and InfixAdditionNeutralElement seem to
            //do the same thing. except the later is bugged and removes 0 from
            //String concating as well. For now I left the second, Niv.
            new InfixTermsZero(), //
            //new InfixAdditionNeutralElement(), //
            new InfixPlusRemoveParenthesis(), //
            new InfixAdditionSort(), //
            new InfixComparisonBooleanLiteral(), //
            new InfixConditionalAndTrue(), //
            new InfixConditionalOrFalse(), //
            new InfixComparisonSpecific(), //
            new InfixMultiplicationByOne(), //
            new InfixMultiplicationSort(), //
            new InfixPseudoAdditionSort(), //
            new InfixSubtractionSort(), //
            new InfixDivisonSort(), //
            new InfixConditionalCommon(), //
            null)
        .add(MethodDeclaration.class, //
            new MethodDeclarationRenameReturnToDollar(), //
            new MethodDeclarationRenameSingleParameterToCent(), //
            new MethodDeclarationRedundantModifiers(), //
            new AbstractBodyDeclarationSortModifiers.ofMethod(), //
            null)
        .add(MethodInvocation.class, //
            new MethodInvocationEqualsWithLiteralString(), //
            new MethodInvocationValueOfBooleanConstant(), //
            new MethodInvocationToStringToEmptyStringAddition(), //
            null)
        .add(SingleVariableDeclaration.class, //
            new SingleVariableDeclarationAbbreviation(), //
            new SingelVariableDeclarationUnderscoreDoubled(), //
            new VariableDeclarationRenameUnderscoreToDoubleUnderscore<>(), //
            null)//
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
            new VariableDeclarationRenameUnderscoreToDoubleUnderscore<>(), null) //
        .add(Block.class, //
            new BlockBreakToReturnInfiniteFor(), //
            new BlockBreakToReturnInfiniteWhile(), //
            new ReturnToBreakFiniteFor(), //
            new ReturnToBreakFiniteWhile(), //
            null) //
        .add(IfStatement.class, //
            new IfTrueOrFalse(), //
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
            new PrefixNotPushdown(), //
            new PrefixPlusRemove(), //
            null) //
        .add(ConditionalExpression.class, //
            new TernaryBooleanLiteral(), //
            new TernaryCollapse(), //
            new TernaryEliminate(), //
            new TernaryShortestFirst(), //
            new TernaryPushdown(), //
            new TernaryPusdownStrings(), null) //
        .add(TypeDeclaration.class, //
            new TypeModifierCleanInterface(), //
            new AbstractBodyDeclarationSortModifiers.ofType(), //
            null) //
        .add(EnumDeclaration.class, //
            new EnumDeclarationModifierCleanEnum(), //
            new AbstractBodyDeclarationSortModifiers.ofEnum(), //
            null) //
        .add(FieldDeclaration.class, //
            new FieldRedundantModifiers(), //
            new AbstractBodyDeclarationSortModifiers.ofField(), //
            null) //
        .add(CastExpression.class, //
            new CastToDouble2Multiply1(), //
            new CastToLong2Multiply1L(), //
            null) //
        .add(EnumConstantDeclaration.class, //
            new EnumConstantRedundantModifiers(), //
            new AbstractBodyDeclarationSortModifiers.ofEnumConstant(), //
            null) //
        .add(NormalAnnotation.class, //
            new AnnotationDiscardValueName(), //
            new AnnotationRemoveEmptyParentheses(), //
            null) //
        // TODO: Alex, shouldn't we remove this line?
        // .add(Initializer, new ModifierSort.ofInitializer(), null) //
        .seal();
  }

  private static <N extends ASTNode> Wring<N> find(final N n, final List<Wring<N>> ns) {
    for (final Wring<N> $ : ns)
      if ($.claims(n))
        return $;
    return null;
  }

  private final Map<Class<? extends ASTNode>, List<Object>> inner = new HashMap<>();

  /** Find the first {@link Wring} appropriate for an {@link ASTNode}
   * @param n JD
   * @return first {@link Wring} for which the parameter is within scope, or
   *         <code><b>null</b></code> if no such {@link Wring} is found. @ */
  public <N extends ASTNode> Wring<N> find(final N n) {
    return find(n, get(n));
  }
  
  public <N extends ASTNode> Wring<N> findWring(final N n, final Wring<N>... ws) {
    for (Wring<N> $ : get(n))
      for (Wring<?> w : ws)
        if (w.getClass().equals($.getClass())) {
          if ($.claims(n))
            return $;
          else
            break;
        }
    return null;
  }

  @SuppressWarnings("unchecked") <N extends ASTNode> List<Wring<N>> get(final Class<? extends ASTNode> n) {
    if (!inner.containsKey(n))
      inner.put(n, new ArrayList<>());
    return (List<Wring<N>>) (List<?>) inner.get(n);
  }

  <N extends ASTNode> List<Wring<N>> get(final N n) {
    return get(n.getClass());
  }

  /** A builder for the enclosing class.
   * @author Yossi Gil
   * @since 2015-08-22 */
  public static class Maker extends Toolbox {
    /** Associate a bunch of{@link Wring} with a given sub-class of
     * {@link ASTNode}.
     * @param n JD
     * @param ns JD
     * @return <code><b>this</b></code>, for easy chaining. */
    @SafeVarargs public final <N extends ASTNode> Maker add(final Class<N> n, final Wring<N>... ns) {
      final List<Wring<N>> l = get(n);
      for (final Wring<N> w : ns) {
        if (w == null)
          break;
        assert w.wringGroup() != null : "Did you forget to use a specific kind for " + w.getClass().getSimpleName();
        if (!w.wringGroup().isEnabled())
          continue;
        l.add(w);
      }
      return this;
    }

    /** Terminate a fluent API chain.
     * @return newly created object */
    public Toolbox seal() {
      return this;
    }
  }
}
