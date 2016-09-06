package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** Singleton containing all {@link Wring}s which are active, allowing selecting
 * and applying the most appropriate such object for a given {@link ASTNode}.
 * @author Yossi Gil
 * @since 2015-08-22 */
public class Toolbox {
  /** The default instance of this class */
  static Toolbox instance;

  /** Initialize this class' internal instance object */
  public static void generate() {
    instance = new Maker()//
        .add(Assignment.class, //
            new AssignmentAndAssignment(), //
            new AssignmentAndReturn(), //
            new AssignmentOpSelf(), //
            new AssignmentToPosrfixIncrement(), //
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
            new EvaluateMultiplication(), //
            new EvaluateDivision(), //
            new EvaluateRemainder(), //
            // new InfixEmptyStringAdditionToString(), //under construction
            new InfixComparisonSizeToZero(), //
            new InfixSubtractionZero(), //
            new InfixAdditionSubtractionExpand(), //
            new InfixFactorNegatives(), //
            new EvaluateAddition(), //
            // new ConcatStrings(), //removed for now so it won't break tests,
            // see issue #120
            new EvaluateSubstraction(), //
            new EvaluateShiftRight(), //
            new EvaluateShiftLeft(), //
            new InfixTermsZero(), // must be before InfixAdditionSort
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
            new MethodRenameReturnToDollar(), //
            new BodyDeclarationRemoveModifiers.OfMethod(), //
            new BodyDeclarationSortModifiers.ofMethod(), //
            null)
        .add(MethodInvocation.class, //
            new StringEqualsConstant(), //
            new BooleanConstants(), new ToStringToEmptyStringAddition(), //
            null)
        .add(SingleVariableDeclaration.class, //
            new SingleVariableDeclarationAbbreviation(), //
            new SingelVariableDeclarationUnderscoreDoubled(), //
            new VariableRenameUnderscoreToDoubleUnderscore<>(), //
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
            new VariableRenameUnderscoreToDoubleUnderscore<>(), null) //
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
            new CleverStringTernarization(), null) //
        .add(TypeDeclaration.class, //
            new ModifierCleanInterface(), //
            new BodyDeclarationSortModifiers.ofType(), //
            null) //
        .add(EnumDeclaration.class, //
            new ModifierCleanEnum(), //
            new BodyDeclarationSortModifiers.ofEnum(), //
            null) //
        .add(SuperConstructorInvocation.class, new SuperConstructorInvocationRemover(), null) //
        .add(ReturnStatement.class, new ReturnLastInMethod()) //
        .add(FieldDeclaration.class, //
            new BodyDeclarationRemoveModifiers.OfField(), //
            new BodyDeclarationSortModifiers.ofField(), //
            null) //
        .add(CastExpression.class, //
            new CastToDouble2Multiply1(), //
            new CastToLong2Multiply1L(), //
            null) //
        .add(EnumConstantDeclaration.class, //
            new BodyDeclarationRemoveModifiers.OfEnumConstant(), //
            new BodyDeclarationSortModifiers.ofEnumConstant(), //
            null) //
        .add(NormalAnnotation.class, //
            new AnnotationDiscardValueName(), //
            new AnnotationRemoveEmptyParentheses(), //
            null) //
        .add(AnnotationTypeMemberDeclaration.class, new BodyDeclarationSortModifiers.ofAnnotationTypeMember(), null) //
        .add(AnnotationTypeDeclaration.class, new BodyDeclarationSortModifiers.ofAnnotation(), null) //
        // .add(Initializer, new ModifierSort.ofInitializer(), null) //
        .seal();
  }

  public static Toolbox instance() {
    return instance;
  }

  private static <N extends ASTNode> Wring<N> find(final N n, final List<Wring<N>> ws) {
    for (final Wring<N> $ : ws)
      if ($.scopeIncludes(n))
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
     * @param c JD
     * @param ws JD
     * @return <code><b>this</b></code>, for easy chaining. */
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

    /** Terminate a fluent API chain.
     * @return newly created object */
    public Toolbox seal() {
      return this;
    }
  }
}
