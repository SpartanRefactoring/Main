package il.org.spartan.spartanizer.dispatch;

import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.*;
import il.org.spartan.plugin.preferences.PreferencesResources.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** Singleton containing all {@link Tipper}s which are active, allowing
 * selecting and applying the most appropriate such object for a given
 * {@link ASTNode}.
 * @author Yossi Gil
 * @since 2015-08-22 */
public class Toolbox {
  public static void main(final String[] args) {
    final Toolbox t = freshCopyOfAllTippers();
    System.out.printf("Currently, there are a total of %d tippers offered on %d classes", box.it(t.tippersCount()), box.it(t.nodesTypeCount()));
  }

  @SuppressWarnings("rawtypes") private static final Map<Class<? extends Tipper>, TipperGroup> categoryMap = new HashMap<Class<? extends Tipper>, TipperGroup>() {
    static final long serialVersionUID = 1L;
    {
      final Toolbox t = freshCopyOfAllTippers();
      Stream.of(t.implementation).filter(Objects::nonNull).forEach(ts -> ts.forEach(λ -> put(λ.getClass(), λ.tipperGroup())));
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

  @SafeVarargs public static <N extends ASTNode> Tipper<N> findTipper(final N n, final Tipper<N>... ts) {
    return Stream.of(ts).filter(λ -> λ.canTip(n)).findFirst().orElse(null);
  }

  public static Toolbox freshCopyOfAllTippers() {
    return new Toolbox()//
//        .add(EnhancedForStatement.class, //
//            new Aggregate(), //
//            new Collect(), //
//            new CountIf(), //
//            new FindFirst(), //
//            new FlatMap(), //
//            new ForEach(), //
//            new ForEachSuchThat(), //
//            new HoldsForAll(), //
//            new HoldsForAny(), //
//            null) //
//        .add(InfixExpression.class, //
//            new Empty(), //
//            new LastIndex(), //
//            new Max(), //
//            new Min(), //
//            new Infix.SafeReference(), //
//            new Singleton(), //
//            null)//
        .add(SingleMemberAnnotation.class, new AnnotationRemoveSingletonArrray()) //
        .add(Initializer.class, new InitializerEmptyRemove()) //
        .add(ArrayAccess.class, new ArrayAccessAndIncrement()) //
        .add(ParenthesizedExpression.class, new ParenthesizedRemoveExtraParenthesis()).add(CatchClause.class, new CatchClauseRenameParameterToCent())
        .add(Javadoc.class, new JavadocEmptyRemove()).add(VariableDeclarationStatement.class, new TwoDeclarationsIntoOne())
        .add(ThrowStatement.class, new SequencerNotLastInBlock<>()) //
        .add(BreakStatement.class, new SequencerNotLastInBlock<>()) //
        .add(ContinueStatement.class, new SequencerNotLastInBlock<>()) //
        .add(TypeParameter.class, new TypeParameterExtendsObject()) //
        .add(WildcardType.class, new WildcardTypeExtendsObjectTrim()) //
        .add(VariableDeclarationExpression.class, new ForRenameInitializerToCent()) //
        .add(ClassInstanceCreation.class, new ClassInstanceCreationValueTypes()) //
        .add(SuperConstructorInvocation.class, new SuperConstructorInvocationRemover()) //
        .add(ExpressionStatement.class, new ExpressionStatementAssertTrueFalse(), new ExpressionStatementThatIsBooleanLiteral(), null) //
        .add(ReturnStatement.class, new ReturnLastInMethod(), //
            new SequencerNotLastInBlock<>()) //
        .add(EnhancedForStatement.class, //
            new EnhancedForRedundantContinue(), //
            new EnhancedForEliminateConditionalContinue(), //
            new EnhancedForParameterRenameToCent(), //
            null)//
        .add(LambdaExpression.class, //
            new LambdaRemoveRedundantCurlyBraces(), //
            new LambdaRemoveParenthesis(), //
            new LambdaRenameSingleParameterToLambda(), //
            null) //
        .add(Modifier.class, //
            new ModifierRedundant(), //
            new ModifierFinalAbstractMethodRedundant(), //
            new ModifierFinalTryResourceRedundant(), //
            null)//
        .add(SingleVariableDeclaration.class, //
            new SingleVariableDeclarationAbbreviation(), //
            new SingelVariableDeclarationUnderscoreDoubled(), //
            new FragmentRenameUnderscoreToDoubleUnderscore<>(), //
            new SingleVariableDeclarationEnhancedForRenameParameterToCent(), null)//
        .add(ForStatement.class, //
            new ForDeadRemove(), //
            new EliminateConditionalContinueInFor(), //
            new BlockBreakToReturnInfiniteFor(), //
            new ReturnToBreakFiniteFor(), //
            new ForToForUpdaters(), //
            new ForTrueConditionRemove(), //
            new ForAndReturnToFor(), //
            new ForRedundantContinue(), //
            null)//
        .add(WhileStatement.class, //
            new EliminateConditionalContinueInWhile(), //
            new BlockBreakToReturnInfiniteWhile(), //
            new ReturnToBreakFiniteWhile(), //
            new WhileDeadRemove(), //
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
        .add(SwitchCase.class, //
            new RemoveRedundantSwitchCases(), //
            new SwitchCaseLocalSort(), //
            null)
        .add(Assignment.class, //
            new AssignmentAndAssignment(), //
            new AssignmentAndReturn(), //
            new AssignmentToFromInfixIncludingTo(), //
            new AssignmentToPrefixIncrement(), //
            null) //
        .add(Block.class, //
            new BlockSimplify(), //
            new BlockSingleton(), //
            // new CachingPattern(), // v 2.7
            // new BlockInlineStatementIntoNext(), //
            // new FindFirst(),
            null) //
        .add(PostfixExpression.class, new PostfixToPrefix()) //
        .add(InfixExpression.class, //
            new InfixPlusToMinus(), //
            new InfixLessEqualsToLess(), //
            new InfixLessToLessEquals(), //
            new InfixMultiplicationEvaluate(), //
            new InfixDivisionEvaluate(), //
            new InfixRemainderEvaluate(), //
            new InfixComparisonSizeToZero(), //
            new InfixSubtractionZero(), //
            new InfixAdditionSubtractionExpand(), //
            new InfixPlusEmptyString(), //
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
            new InfixIndexOfToStringContains(), // v 2.7
            new InfixSimplifyComparisionOfAdditions(), //
            new InfixSimplifyComparisionOfSubtractions(), //
            null)
        .add(MethodDeclaration.class, //
            new AnnotationSort<>(), //
            new MethodDeclarationRenameReturnToDollar(), //
            new BodyDeclarationModifiersSort<>(), //
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
            null)//
        .add(TryStatement.class, //
            new TryBodyEmptyLeaveFinallyIfExists(), //
            new TryBodyEmptyNoCatchesNoFinallyEliminate(), //
            new TryBodyNotEmptyNoCatchesNoFinallyRemove(), //
            new TryFinallyEmptyRemove(), //
            new MergeCatches(), //
            null)//
        .add(IfStatement.class, //
            new IfTrueOrFalse(), //
            new IfDeadRemove(), //
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
        .add(EnumConstantDeclaration.class, new BodyDeclarationModifiersSort<>()) //
        .add(TypeDeclaration.class, //
            new BodyDeclarationModifiersSort<>(), //
            new AnnotationSort<>(), //
            new TypeDeclarationClassExtendsObject(), null) //
        .add(EnumDeclaration.class, //
            new BodyDeclarationModifiersSort<>(), //
            new AnnotationSort<>(), //
            null) //
        .add(FieldDeclaration.class, //
            new BodyDeclarationModifiersSort<>(), //
            new AnnotationSort<>(), //
            null) //
        .add(CastExpression.class, //
            new CastToDouble2Multiply1(), //
            new CastToLong2Multiply1L(), //
            null) //
        .add(EnumConstantDeclaration.class, //
            new BodyDeclarationModifiersSort<>(), //
            new AnnotationSort<>(), //
            null) //
        .add(NormalAnnotation.class, //
            new AnnotationDiscardValueName(), //
            new AnnotationRemoveEmptyParentheses(), //
            null) //
        .add(AnnotationTypeDeclaration.class, //
            new BodyDeclarationModifiersSort<>(), //
            new AnnotationSort<>(), //
            null)
        .add(AnnotationTypeMemberDeclaration.class, new BodyDeclarationModifiersSort<>(), //
            new AnnotationSort<>(), //
            null)
        .add(VariableDeclarationFragment.class, //
            new FragmentInitializerDead(), //
            new FragmentNoInitializerAssignment(), //
            new FragmentInitialiazerUpdateAssignment(), //
            new FragmentInitializerIfAssignment(), //
            new FragmentInitializerIfUpdateAssignment(), //
            new FragmentInitializerReturnVariable(), //
            new FragmentInitializerReturnExpression(), //
            new FragmentInitializerReturnAssignment(), //
            new FragmentInitializerReturn(), //
            new FragmentInitializerStatementTerminatingScope(), //
            new FragmentInitialiazerAssignment(), //
            new FragmentInitializerInlineIntoNext(), //
            new FragmentInitializerWhile(), //
            new FragmentInitializerToForInitializers(), //
            new FragmentRenameUnderscoreToDoubleUnderscore<>(), //
            new FragmentNoInitializerRemoveUnused(), //
            null) //
    ;
  }

  /** Make a for a specific kind of tippers
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
    removing:
    // noinspection ForLoopReplaceableByWhile
    for (;;) {
      for (int ¢ = 0; ¢ < ts.size(); ++¢)
        if (c.isAssignableFrom(ts.get(¢).getClass())) {
          ts.remove(¢);
          continue removing;
        }
      break;
    }
  }

  @SuppressWarnings("unchecked") private static <N extends ASTNode> Tipper<N> firstTipper(final N n, final Collection<Tipper<?>> ts) {
    return ts.stream().filter(λ -> ((Tipper<N>) λ).canTip(n)).map(λ -> (Tipper<N>) λ).findFirst().orElse(null);
  }

  /** Implementation */
  @SuppressWarnings("unchecked") public final List<Tipper<? extends ASTNode>>[] implementation = //
      (List<Tipper<? extends ASTNode>>[]) new List<?>[2 * ASTNode.TYPE_METHOD_REFERENCE];

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
              Toolbox.name(¢), //
              ¢.description()));//
      if (¢.tipperGroup().isEnabled())
        get(nodeType.intValue()).add(¢);
    }
    return this;
  }

  @SafeVarargs public final <N extends ASTNode> Toolbox remove(final Class<N> c, final Tipper<N>... ts) {
    final Integer nodeType = wizard.classToNodeType.get(c);
    for (final Tipper<N> ¢ : ts)
      get(nodeType.intValue()).remove(¢);
    return this;
  }

  public Collection<Tipper<? extends ASTNode>> getAllTippers() {
    final Collection<Tipper<? extends ASTNode>> $ = new ArrayList<>();
    for (int ¢ = 0; ¢ < implementation.length; ++¢)
      $.addAll(get(¢));
    return $;
  }

  public void disable(final Class<? extends TipperCategory> c) {
    Stream.of(implementation).filter(Objects::nonNull).forEach(λ -> disable(c, λ));
  }

  /** Find the first {@link Tipper} appropriate for an {@link ASTNode}
   * @param pattern JD
   * @return first {@link Tipper} for which the parameter is within scope, or
   *         <code><b>null</b></code> if no such {@link Tipper} is found. @ */
  public <N extends ASTNode> Tipper<N> firstTipper(final N ¢) {
    return firstTipper(¢, get(¢));
  }

  public Collection<Tipper<? extends ASTNode>> get(final int ¢) {
    return implementation[¢] = implementation[¢] == null ? new ArrayList<>() : implementation[¢];
  }

  public static long hooksCount() {
    return defaultTipperLists().count();
  }

  public static Stream<List<Tipper<? extends ASTNode>>> defaultTipperLists() {
    return Stream.of(Toolbox.defaultInstance().implementation).filter(λ -> λ != null && !λ.isEmpty());
  }

  public int tippersCount() {
    return Arrays.stream(implementation).filter(Objects::nonNull).mapToInt(List::size).sum();
  }

  public int nodesTypeCount() {
    return (int) Arrays.stream(implementation).filter(Objects::nonNull).count();
  }

  <N extends ASTNode> Collection<Tipper<? extends ASTNode>> get(final N ¢) {
    return get(¢.getNodeType());
  }

  public static String intToClassName(final int $) {
    try {
      return ASTNode.nodeClassForType($).getSimpleName();
    } catch (@SuppressWarnings("unused") final IllegalArgumentException __) {
      return "???";
    }
  }

  public static <T extends Tipper<? extends ASTNode>> String name(final T ¢) {
    return ¢.getClass().getSimpleName();
  }

  public static String name(final Class<? extends Tipper<?>> ¢) {
    return ¢.getSimpleName();
  }

  public static List<String> get(final TipperGroup ¢) {
    final List<String> $ = new ArrayList<>();
    if (¢ == null)
      return $;
    final Toolbox t = freshCopyOfAllTippers();
    assert t.implementation != null;
    Stream.of(t.implementation).filter(Objects::nonNull)
        .forEach(element -> $.addAll(element.stream().filter(λ -> ¢.equals(λ.tipperGroup())).map(Tipper::myName).collect(toList())));
    return $;
  }

  public static TipperGroup groupFor(@SuppressWarnings("rawtypes") final Class<? extends Tipper> tipperClass) {
    return categoryMap == null || !categoryMap.containsKey(tipperClass) ? null : categoryMap.get(tipperClass);
  }
}