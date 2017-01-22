package il.org.spartan.spartanizer.dispatch;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.*;
import il.org.spartan.plugin.preferences.PreferencesResources.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.tables.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.*;
import il.org.spartan.spartanizer.research.nanos.Collect;
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
          ts.forEach(¢ -> put(¢.getClass(), ¢.tipperGroup()));
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

  public static <N extends ASTNode> Tipper<N> findTipper(final N n, @SuppressWarnings("unchecked") final Tipper<N>... ts) {
    for (final Tipper<N> $ : ts)
      if ($.canTip(n))
        return $;
    return null;
  }

  public static Toolbox freshCopyOfAllTippers() {
    return new Toolbox()//
        .add(ReturnStatement.class, new ReturnLastInMethod(), new SequencerNotLastInBlock<ReturnStatement>()) //
        .add(ThrowStatement.class, new SequencerNotLastInBlock<ThrowStatement>()) //
        .add(BreakStatement.class, new SequencerNotLastInBlock<BreakStatement>()) //
        .add(ContinueStatement.class, new SequencerNotLastInBlock<ContinueStatement>()) //
        .add(TypeParameter.class, new TypeParameterExtendsObject()) //
        .add(WildcardType.class, new WildcardTypeExtendsObjectTrim()) //
        .add(EnhancedForStatement.class, //
            new EliminateConditionalContinueInEnhancedFor(), //
            new EnhancedForParameterRenameToCent(), //
            new EnhancedForRedundantContinue(), //
            null)//
        .add(Initializer.class, new InitializerEmptyRemove()) //
        .add(LambdaExpression.class, new LambdaExpressionRemoveRedundantCurlyBraces()) //
        .add(ExpressionStatement.class, new ExpressionStatementAssertTrueFalse()) //
        .add(Modifier.class, //
            new ModifierRedundant(), //
            new ModifierFinalAbstractMethodRedundant(), //
            new ModifierFinalTryResourceRedundant(), //
            null)//
        .add(VariableDeclarationExpression.class, new ForRenameInitializerToCent()) //
        .add(ClassInstanceCreation.class, new ClassInstanceCreationValueTypes()) //
        .add(SuperConstructorInvocation.class, new SuperConstructorInvocationRemover()) //
        .add(SingleVariableDeclaration.class, //
            new SingleVariableDeclarationAbbreviation(), //
            new SingelVariableDeclarationUnderscoreDoubled(), //
            new VariableDeclarationRenameUnderscoreToDoubleUnderscore<>(), //
            new SingleVariableDeclarationEnhancedForRenameParameterToCent(), //
            null)//
        // .add(EnhancedForStatement.class, //
        // new Aggregate(), //
        // new Collect(), //
        // new FindFirst(), //
        // new ForEach(), //
        // new ForEachSuchThat(), //
        // new HoldsForAll(), //
        // new HoldsForAny(), //
        // null) //
        .add(ForStatement.class, //
            new EliminateConditionalContinueInFor(), //
            new BlockBreakToReturnInfiniteFor(), //
            new ReturnToBreakFiniteFor(), //
            new RemoveRedundantFor(), //
            new ForToForUpdaters(), //
            new ForTrueConditionRemove(), //
            new ForAndReturnToFor(), //
            new ForRedundantContinue(), //
            null)//
        .add(WhileStatement.class, //
            new EliminateConditionalContinueInWhile(), //
            new BlockBreakToReturnInfiniteWhile(), //
            new ReturnToBreakFiniteWhile(), //
            new RemoveRedundantWhile(), //
            new WhileToForUpdaters(), //
            null) //
        .add(SwitchStatement.class, //
            new SwitchEmpty(), //
            new MergeSwitchBranches(), //
            new RemoveRedundantSwitchReturn(), //
            new RemoveRedundantSwitchContinue(), //
            new SwitchWithOneCaseToIf(), //
            new SwitchBranchSort(), //
            null)
        .add(SwitchCase.class, new RemoveRedundantSwitchCases(), //
            new SwitchCaseLocalSort(), //
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
            // new FindFirst(),
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
            new MethodDeclarationConstructorMoveToInitializers(), //
            // new MatchCtorParamNamesToFieldsIfAssigned(),
            // v 2.7 // This is a new // tipper // #20
            null)
        .add(MethodInvocation.class, //
            new MethodInvocationEqualsWithLiteralString(), //
            new MethodInvocationValueOfBooleanConstant(), //
            new MethodInvocationToStringToEmptyStringAddition(), //
            new StringFromStringBuilder(), //
            // new LispFirstElement(), //
            // new LispLastElement(), //
            // new StatementsThroughStep(), //
            null)//
        .add(ParenthesizedExpression.class, //
            new ParenthesizedRemoveExtraParenthesis(), //
            null) //
        .add(TryStatement.class, //
            new TryBodyEmptyLeaveFinallyIfExists(), //
            new TryBodyEmptyNoCatchesNoFinallyEliminate(), //
            new TryBodyNotEmptyNoCatchesNoFinallyRemove(), //
            new TryFinallyEmptyRemove(), //
            new MergeCatches(), //
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
            new TypeDeclarationClassExtendsObject(), null) //
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
        .add(AnnotationTypeDeclaration.class, new $BodyDeclarationModifiersSort.ofAnnotation(), //
            new AnnotationSort.ofAnnotation(), //
            null)
        .add(AnnotationTypeMemberDeclaration.class, new $BodyDeclarationModifiersSort.ofAnnotationTypeMember(), //
            new AnnotationSort.ofAnnotationTypeMember(), //
            null)
        .add(VariableDeclarationFragment.class, //
            new DeclarationRedundantInitializer(), //
            new DeclarationNoInitializerAssignment(), //
            new DeclarationInitialiazerUpdateAssignment(), //
            new DeclarationInitializerIfAssignment(), //
            new DeclarationInitializerIfUpdateAssignment(), //
            new DeclarationInitializerReturnVariable(), //
            new DeclarationInitializerReturnExpression(), //
            new DeclarationInitializerReturnAssignment(), //
            new DeclarationInitializerReturn(), //
            new DeclarationInitializerStatementTerminatingScope(), //
            new DeclarationInitialiazerAssignment(), //
            new DeclarationInlineIntoNext(), //
            new VariableDeclarationRenameUnderscoreToDoubleUnderscore<>(), //
            new ForToForInitializers(), //
            new WhileToForInitializers(), //
            null) //
        .add(VariableDeclarationStatement.class, //
            new TwoDeclarationsIntoOne(), //
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
  @SafeVarargs public static <N extends ASTNode> Toolbox make(final Class<N> clazz, final Tipper<N>... ts) {
    return emptyToolboox().add(clazz, ts);
  }

  public static void refresh() {
    defaultInstance = freshCopyOfAllTippers();
  }

  public static void refresh(final Trimmer ¢) {
    ¢.toolbox = freshCopyOfAllTippers();
  }

  private static void disable(final Class<? extends TipperCategory> c, final List<Tipper<? extends ASTNode>> ts) {
    removing: for (;;) {
      for (int ¢ = 0; ¢ < ts.size(); ++¢)
        if (c.isAssignableFrom(ts.get(¢).getClass())) {
          ts.remove(¢);
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
   * @param c JD
   * @param ts JD
   * @return <code><b>this</b></code>, for easy chaining. */
  @SafeVarargs public final <N extends ASTNode> Toolbox add(final Class<N> c, final Tipper<N>... ts) {
    final Integer $ = wizard.classToNodeType.get(c);
    assert $ != null : fault.dump() + //
        "\n c = " + c + //
        "\n c.getSimpleName() = " + c.getSimpleName() + //
        "\n classForNodeType.keySet() = " + wizard.classToNodeType.keySet() + //
        "\n classForNodeType = " + wizard.classToNodeType + //
        fault.done();
    return add($, ts);
  }

  @SafeVarargs public final <N extends ASTNode> Toolbox add(final Integer nodeType, final Tipper<N>... ts) {
    for (final Tipper<N> ¢ : ts) {
      if (¢ == null)
        break;
      assert ¢.tipperGroup() != null : fault.specifically(//
          String.format("Did you forget to use create an enum instance in %s \n" + "for the %s of tipper %s \n (description= %s)?", //
              TipperGroup.class.getSimpleName(), //
              TipperCategory.class.getSimpleName(), //
              ¢.getClass().getSimpleName(), //
              ¢.description()));//
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

  public static String intToClassName(final int $) {
    try {
      return Table_Tippers.name(ASTNode.nodeClassForType($));
    } catch (@SuppressWarnings("unused") final IllegalArgumentException __) {
      return "???";
    }
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
        $.addAll(element.stream().filter(p -> ¢.equals(p.tipperGroup())).map(Tipper::myName).collect(Collectors.toList()));
    return $;
  }

  public static TipperGroup groupFor(@SuppressWarnings("rawtypes") final Class<? extends Tipper> tipperClass) {
    return categoryMap == null || !categoryMap.containsKey(tipperClass) ? null : categoryMap.get(tipperClass);
  }
}