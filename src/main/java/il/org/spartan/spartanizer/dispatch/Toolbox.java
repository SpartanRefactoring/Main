package il.org.spartan.spartanizer.dispatch;

import java.util.*;
import java.util.concurrent.atomic.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.*;
import il.org.spartan.plugin.PreferencesResources.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Singleton containing all {@link Tipper}s which are active, allowing
 * selecting and applying the most appropriate such object for a given
 * {@link ASTNode}.
 * @author Yossi Gil
 * @since 2015-08-22 */
public class Toolbox {
  @SuppressWarnings("rawtypes") private static final Map<Class<? extends Tipper>, TipperGroup> categoryMap = new HashMap<Class<? extends Tipper>, TipperGroup>() {
    static final long serialVersionUID = 1L;
    {
      final Toolbox t = freshCopyOfAllTippers();
      assert t.implementation != null;
      for (final List<Tipper<? extends ASTNode>> ts : t.implementation)
        if (ts != null)
          for (final Tipper<? extends ASTNode> ¢ : ts)
            put(¢.getClass(), ¢.tipperGroup());
    }
  };
  /** The default instance of this class */
  static Toolbox defaultInstance;

  /** Generate an {@link ASTRewrite} that contains the changes proposed by the
   * first tipper that applies to a node in the usual scan.
   * @param root JD
   * @return */
  public ASTRewrite pickFirstTip(final ASTNode root) {
    disabling.scan(root);
    final AtomicBoolean done = new AtomicBoolean(false);
    final ASTRewrite $ = ASTRewrite.create(root.getAST());
    root.accept(new ASTVisitor() {
      @Override public boolean preVisit2(final ASTNode n) {
        if (done.get())
          return false;
        if (disabling.on(n))
          return true;
        final Tipper<?> t = firstTipper(n);
        if (t == null)
          return true;
        done.set(true);
        extractTip(t, n).go($, null);
        return false;
      }
    });
    return $;
  }

  public static Tip extractTip(final Tipper<? extends ASTNode> t, final ASTNode n) {
    @SuppressWarnings("unchecked") final Tipper<ASTNode> $ = (Tipper<ASTNode>) t;
    return extractTip(n, $);
  }

  public static Tip extractTip(final ASTNode n, final Tipper<ASTNode> t) {
    return t.tip(n);
  }

  public static Toolbox defaultInstance() {
    return defaultInstance = defaultInstance != null ? defaultInstance : freshCopyOfAllTippers();
  }

  public static Toolbox mutableDefaultInstance() {
    return freshCopyOfAllTippers();
  }

  private static Toolbox emptyToolboox() {
    return new Toolbox();
  }

  public static <N extends ASTNode> Tipper<N> findTipper(final N n, @SuppressWarnings("unchecked") final Tipper<N>... ns) {
    for (final Tipper<N> $ : ns)
      if ($.canTip(n))
        return $;
    return null;
  }

  public static Toolbox freshCopyOfAllTippers() {
    return new Toolbox()//
        .add(EnhancedForStatement.class, //
            new EnhancedForParameterRenameToCent(), //
            new EnhancedForRedundantConinue(), //
            null)//
        .add(LambdaExpression.class, new LambdaExpressionRemoveRedundantCurlyBraces()) //
        .add(ExpressionStatement.class, new ExpressionStatementAssertTrueFalse()) //
        .add(Modifier.class, new RedundantModifier())//
        .add(VariableDeclarationExpression.class, new ForRenameInitializerToCent()) //
        .add(ThrowStatement.class, new ThrowNotLastInBlock()) //
        .add(ClassInstanceCreation.class, new ClassInstanceCreationValueTypes()) //
        .add(SuperConstructorInvocation.class, new SuperConstructorInvocationRemover()) //
        .add(SingleVariableDeclaration.class, //
            new SingleVariableDeclarationAbbreviation(), //
            new SingelVariableDeclarationUnderscoreDoubled(), //
            new VariableDeclarationRenameUnderscoreToDoubleUnderscore<SingleVariableDeclaration>(), //
            new SingleVariableDeclarationEnhancedForRenameParameterToCent(), //
            null)//
        .add(ForStatement.class, //
            new BlockBreakToReturnInfiniteFor(), //
            new ReturnToBreakFiniteFor(), //
            new RemoveRedundentFor(), //
            new ForToForUpdaters(), //
            new ForTrueConditionRemove(), //
            new ForAndReturnToFor(), //
            new ForRedundantContinue(), //
            null)//
        .add(WhileStatement.class, //
            new BlockBreakToReturnInfiniteWhile(), //
            new ReturnToBreakFiniteWhile(), //
            new RemoveRedundantWhile(), //
            new WhileToForUpdaters(), //
            null) //
        .add(SwitchStatement.class, //
            new SwitchEmpty(), //
            new RemoveRedundantSwitchCases(), //
            new RemoveRedundantSwitchBranch(), //
            // new SwitchWithOneCaseToIf(), //
            null)
        .add(Assignment.class, //
            new AssignmentAndAssignment(), //
            new AssignmentAndReturn(), //
            new AssignmentToFromInfixIncludingTo(), //
            new AssignmentToPrefixIncrement(), //
            null) //
        .add(Block.class, //
            // new BlockRemoveDeadVariables(), //
            new BlockSimplify(), //
            new BlockSingleton(), //
            // new CachingPattern(), // v 2.7
            new BlockInlineStatementIntoNext(), //
            // new BlockRemoveDeadVariables(), // v 2.7
            null) //
        .add(PostfixExpression.class, //
            new PostfixToPrefix(), //
            null) //
        .add(ArrayAccess.class, //
            new InliningPrefix(), //
            null) //
        .add(Javadoc.class, new JavadocEmpty())
        .add(InfixExpression.class, //
            new InfixPlusToMinus(), //
            new LessEqualsToLess(), //
            new LessToLessEquals(), //
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
            new InfixSubtractionEvaluate(), //
            new InfixTermsZero(), //
            new InfixPlusRemoveParenthesis(), //
            new InfixAdditionSort(), //
            new InfixComparisonBooleanLiteral(), //
            new InfixConditionalAndTrue(), //
            new InfixConditionalOrFalse(), //
            new InfixComparisonSpecific(), //
            new InfixMultiplicationByOne(), //
            new InfixMultiplicationByZero(), //
            new InfixMultiplicationSort(), //
            new InfixPseudoAdditionSort(), //
            new InfixSubtractionSort(), //
            new InfixDivisonSortRest(), //
            new InfixConditionalCommon(), //
            // new InfixIndexOfToStringContains(), // v 2.7
            new SimplifyComparisionOfAdditions(), new SimplifyComparisionOfSubtractions(), //
            null)
        .add(MethodDeclaration.class, //
            new AnnotationSort.ofMethod(), //
            new MethodDeclarationRenameReturnToDollar(), //
            new $BodyDeclarationModifiersSort.ofMethod(), //
            new MethodDeclarationRenameSingleParameterToCent(), //
            new RedundentReturnStatementInVoidTypeMethod(), //
            // new MatchCtorParamNamesToFieldsIfAssigned(), // v 2.7
            // This is a new
            // tipper
            // #20
            null)
        .add(MethodInvocation.class, //
            new MethodInvocationEqualsWithLiteralString(), //
            new MethodInvocationValueOfBooleanConstant(), //
            new MethodInvocationToStringToEmptyStringAddition(), //
            // new LispFirstElement(), //
            // new LispLastElement(), //
            // new StatementsThroughStep(), //
            null)//
        .add(TryStatement.class, //
            new EliminateEmptyFinally(), //
            new MergeCatches(), //
            new EliminateEmptyTryBlock(), //
            null)//
        .add(CatchClause.class, //
            new CatchClauseRenameParameterToCent(), //
            null)//
        .add(IfStatement.class, //
            new IfTrueOrFalse(), //
            new RemoveRedundantIf(), //
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
            new IfPenultimateInMethodFollowedBySingleStatement(), //
            new IfThenIfThenNoElseNoElse(), //
            new IfEmptyThenEmptyElse(), //
            new IfDegenerateElse(), //
            new IfEmptyThen(), //
            new IfShortestFirst(), //
            // new PutIfAbsent(), //
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
            new TernaryPushdownStrings(), //
            new SameEvaluationConditional(), //
            new TernaryBranchesAreOppositeBooleans(), //
            new SameEvaluationConditional(), null) //
        .add(EnumConstantDeclaration.class, new $BodyDeclarationModifiersSort.ofEnumConstant()) //
        .add(TypeDeclaration.class, //
            new $BodyDeclarationModifiersSort.ofType(), //
            new AnnotationSort.ofType(), //
            null) //
        .add(EnumDeclaration.class, //
            new $BodyDeclarationModifiersSort.ofEnum(), //
            new AnnotationSort.ofEnum(), //
            null) //
        .add(FieldDeclaration.class, //
            new $BodyDeclarationModifiersSort.ofField(), //
            new AnnotationSort.ofField(), //
            null) //
        .add(CastExpression.class, //
            new CastToDouble2Multiply1(), //
            new CastToLong2Multiply1L(), //
            null) //
        .add(EnumConstantDeclaration.class, //
            new $BodyDeclarationModifiersSort.ofEnumConstant(), //
            new AnnotationSort.ofEnumConstant(), //
            null) //
        .add(NormalAnnotation.class, //
            new AnnotationDiscardValueName(), //
            new AnnotationRemoveEmptyParentheses(), //
            null) //
        .add(Initializer.class, new $BodyDeclarationModifiersSort.ofInitializer(), //
            new AnnotationSort.ofInitializer(), //
            null) //
        .add(AnnotationTypeDeclaration.class, new $BodyDeclarationModifiersSort.ofAnnotation(), //
            new AnnotationSort.ofAnnotation(), //
            null)
        .add(AnnotationTypeMemberDeclaration.class, new $BodyDeclarationModifiersSort.ofAnnotationTypeMember(), //
            new AnnotationSort.ofAnnotationTypeMember(), //
            null)
        .add(VariableDeclarationFragment.class, //
            new DeclarationRedundantInitializer(), //
            new DeclarationAssignment(), //
            new DeclarationInitialiazelUpdateAssignment(), //
            new DeclarationInitializerIfAssignment(), //
            new DeclarationInitializerIfUpdateAssignment(), //
            new DeclarationInitializerReturnVariable(), //
            new DeclarationInitializerReturnExpression(), //
            new DeclarationInitializerReturnAssignment(), //
            new DeclarationInitializerReturn(), //
            new DeclarationInitializerStatementTerminatingScope(), //
            new DeclarationInitialiazerAssignment(), //
            new DeclarationInlineIntoNext(), //
            new VariableDeclarationRenameUnderscoreToDoubleUnderscore<VariableDeclarationFragment>(), //
            new ForToForInitializers(), //
            new WhileToForInitializers(), //
            null) //
    //
    //
    ;
  }

  /** Make a {@link Toolbox} for a specific kind of tippers
   * @param clazz JD
   * @param w JS
   * @return a new defaultInstance containing only the tippers passed as
   *         parameter */
  @SafeVarargs public static <N extends ASTNode> Toolbox make(final Class<N> clazz, final Tipper<N>... ns) {
    return emptyToolboox().add(clazz, ns);
  }

  public static void refresh() {
    defaultInstance = freshCopyOfAllTippers();
  }

  public static void refresh(final Trimmer ¢) {
    ¢.toolbox = freshCopyOfAllTippers();
  }

  private static void disable(final Class<? extends TipperCategory> c, final List<Tipper<? extends ASTNode>> ns) {
    removing: for (;;) {
      for (int ¢ = 0; ¢ < ns.size(); ++¢)
        if (c.isAssignableFrom(ns.get(¢).getClass())) {
          ns.remove(¢);
          continue removing;
        }
      break;
    }
  }

  @SuppressWarnings("unchecked") private static <N extends ASTNode> Tipper<N> firstTipper(final N n, final List<Tipper<?>> ts) {
    for (final Tipper<?> $ : ts)
      if (((Tipper<N>) $).canTip(n))
        return (Tipper<N>) $;
    return null;
  }

  /** Implementation */
  @SuppressWarnings("unchecked") public final List<Tipper<? extends ASTNode>>[] implementation = //
      (List<Tipper<? extends ASTNode>>[]) new List<?>[2 * ASTNode.TYPE_METHOD_REFERENCE];

  public Toolbox() {
    // Nothing to do
  }

  /** Associate a bunch of{@link Tipper} with a given sub-class of
   * {@link ASTNode}.
   * @param n JD
   * @param ns JD
   * @return <code><b>this</b></code>, for easy chaining. */
  @SafeVarargs public final <N extends ASTNode> Toolbox add(final Class<N> n, final Tipper<N>... ns) {
    final Integer nodeType = wizard.classToNodeType.get(n);
    assert nodeType != null : fault.dump() + //
        "\n c = " + n + //
        "\n c.getSimpleName() = " + n.getSimpleName() + //
        "\n classForNodeType.keySet() = " + wizard.classToNodeType.keySet() + //
        "\n classForNodeType = " + wizard.classToNodeType + //
        fault.done();
    for (final Tipper<N> ¢ : ns) {
      if (¢ == null)
        break;
      assert ¢.tipperGroup() != null : "Did you forget to use a specific kind for " + ¢.getClass().getSimpleName();
      if (¢.tipperGroup().isEnabled())
        get(nodeType.intValue()).add(¢);
    }
    return this;
  }

  public List<Tipper<? extends ASTNode>> getAllTippers() {
    final List<Tipper<? extends ASTNode>> $ = new ArrayList<>();
    for (int ¢ = 0; ¢ < implementation.length; ++¢)
      $.addAll(get(¢));
    return $;
  }

  public void disable(final Class<? extends TipperCategory> c) {
    for (final List<Tipper<? extends ASTNode>> ¢ : implementation)
      if (¢ != null)
        disable(c, ¢);
  }

  /** Find the first {@link Tipper} appropriate for an {@link ASTNode}
   * @param pattern JD
   * @return first {@link Tipper} for which the parameter is within scope, or
   *         <code><b>null</b></code> if no such {@link Tipper} is found. @ */
  public <N extends ASTNode> Tipper<N> firstTipper(final N ¢) {
    return firstTipper(¢, get(¢));
  }

  public List<Tipper<? extends ASTNode>> get(final int ¢) {
    return implementation[¢] = implementation[¢] == null ? new ArrayList<>() : implementation[¢];
  }

  public int hooksCount() {
    int $ = 0;
    for (final List<Tipper<? extends ASTNode>> ¢ : implementation)
      $ += as.bit(¢ != null && !¢.isEmpty());
    return $;
  }

  public int tippersCount() {
    int $ = 0;
    for (final List<?> ¢ : implementation)
      if (¢ != null)
        $ += ¢.size();
    return $;
  }

  <N extends ASTNode> List<Tipper<? extends ASTNode>> get(final N ¢) {
    return get(¢.getNodeType());
  }

  /** TODO: Apparently there is no check that ¢ is not occupied already... */
  public static List<String> get(final TipperGroup ¢) {
    final List<String> $ = new LinkedList<>();
    if (¢ == null)
      return $;
    final Toolbox t = freshCopyOfAllTippers();
    assert t.implementation != null;
    for (final List<Tipper<? extends ASTNode>> element : t.implementation)
      if (element != null)
        for (final Tipper<?> p : element)
          if (¢.equals(p.tipperGroup()))
            $.add(p.myName());
    return $;
  }

  public static TipperGroup groupFor(@SuppressWarnings("rawtypes") final Class<? extends Tipper> tipperClass) {
    return categoryMap == null || !categoryMap.containsKey(tipperClass) ? null : categoryMap.get(tipperClass);
  }
}