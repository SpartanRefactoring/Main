package il.org.spartan.spartanizer.dispatch;

import static java.util.stream.Collectors.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.*;
import il.org.spartan.plugin.preferences.revision.PreferencesResources.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Singleton containing all {@link Tipper}s which are active. This class does
 * minimal dispatching at the node level, selecting and applying the most
 * appropriate such object for a given {@link ASTNode}. Dispatching at the tree
 * level is done in class {@link Trimmer}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM} {@code Yossi.Gil@GMail.COM}
 * @since 2015-08-22 */
public class Toolbox {
  @SuppressWarnings("unchecked")
  public static class Tables {
    public static final Map<String, Class<? extends Tipper<?>>> TipperIDClassTranslationTable = new HashMap<>();
    public static final Map<String, String> TipperIDNameTranslationTable = new HashMap<>();
    public static final Map<Class<? extends Tipper<?>>, String> TipperDescriptionCache = new HashMap<>();
    public static final Map<Class<? extends Tipper<?>>, Example[]> TipperExamplesCache = new HashMap<>();
    public static final Map<Class<? extends Tipper<?>>, Tipper<?>> TipperObjectByClassCache = new HashMap<>();
    static {
      for (final Tipper<? extends ASTNode> t : freshCopyOfAllTippers().getAllTippers()) {
        final String id = ObjectStreamClass.lookup(t.getClass()).getSerialVersionUID() + "";
        TipperIDClassTranslationTable.put(id, (Class<? extends Tipper<?>>) t.getClass());
        TipperIDNameTranslationTable.put(id, t.className());
        TipperDescriptionCache.put((Class<? extends Tipper<?>>) t.getClass(), t.description());
        TipperExamplesCache.put((Class<? extends Tipper<?>>) t.getClass(), t.examples());
        TipperObjectByClassCache.put((Class<? extends Tipper<?>>) t.getClass(), t);
      }
    }
  }

  public static void main(final String[] args) {
    final Toolbox t = freshCopyOfAllTippers();
    System.out.printf("Currently, there are a total of %d tippers offered on %d classes", box.it(t.tippersCount()), box.it(t.nodesTypeCount()));
  }

  @SuppressWarnings("rawtypes") private static final Map<Class<? extends Tipper>, TipperGroup> categoryMap = new HashMap<Class<? extends Tipper>, TipperGroup>() {
    static final long serialVersionUID = -1755342003544953338L;
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
    final Bool done = new Bool();
    final ASTRewrite $ = ASTRewrite.create(root.getAST());
    root.accept(new ASTVisitor(true) {
      @Override public boolean preVisit2(final ASTNode n) {
        if (done.get())
          return false;
        if (disabling.on(n))
          return true;
        final Tipper<?> t = firstTipper(n);
        if (t == null)
          return true;
        done.set();
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
    return Stream.of(ts).filter(λ -> λ.check(n)).findFirst().orElse(null);
  }

  public static Toolbox freshCopyOfAllTippers() {
    return new Toolbox()//
        .add(SingleMemberAnnotation.class, new AnnotationRemoveSingletonArrray()) //
        .add(Initializer.class, new InitializerEmptyRemove()) //
        .add(ArrayAccess.class, new ArrayAccessAndIncrement()) //
        .add(ParenthesizedExpression.class, new ParenthesizedRemoveExtraParenthesis()) //
        .add(CatchClause.class, new CatchClauseRenameParameterToIt()) //
        .add(Javadoc.class, new JavadocEmptyRemove()) //
        .add(VariableDeclarationStatement.class, new TwoDeclarationsIntoOne()).add(ThrowStatement.class, new SequencerNotLastInBlock<>()) //
        .add(BreakStatement.class, new SequencerNotLastInBlock<>()) //
        .add(ContinueStatement.class, new SequencerNotLastInBlock<>()) //
        .add(TypeParameter.class, new TypeParameterExtendsObject()) //
        .add(WildcardType.class, new WildcardTypeExtendsObjectTrim()) //
        .add(VariableDeclarationExpression.class, new ForRenameInitializerToIt()) //
        .add(ClassInstanceCreation.class, new ClassInstanceCreationBoxedValueTypes()) //
        .add(SuperConstructorInvocation.class, new SuperConstructorInvocationRemover()) //
        .add(ExpressionStatement.class, new ExpressionStatementAssertTrueFalse(), new ExpressionStatementThatIsBooleanLiteral(), null) //
        .add(ReturnStatement.class, new ReturnLastInMethod(), //
            new SequencerNotLastInBlock<>()) //
        .add(EnhancedForStatement.class, //
            new EnhancedForRedundantContinue(), //
            new EnhancedForEliminateConditionalContinue(), //
            new EnhancedForParameterRenameToIt(), //
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
            new SingleVariableDeclarationEnhancedForRenameParameterToIt(), null)//
        .add(ForStatement.class, //
            new ForNoUpdatersNoInitializerToWhile(), //
            new ForDeadRemove(), //
            new ContinueCoditinalInForEliminate(), //
            new BlockBreakToReturnInfiniteFor(), //
            new ReturnToBreakFiniteFor(), //
            new ForToForUpdaters(), //
            new ForTrueConditionRemove(), //
            new ForAndReturnToFor(), //
            new ForRedundantContinue(), //
            new ForEmptyBlockToEmptyStatement(), //
            new ForWithEndingBreakToDoWhile(), //
            null)//
        .add(WhileStatement.class, //
            new EliminateConditionalContinueInWhile(), //
            new BlockBreakToReturnInfiniteWhile(), //
            new ReturnToBreakFiniteWhile(), //
            new WhileDeadRemove(), //
            new WhileToForUpdaters(), //
            new WhileEmptyBlockToEmptyStatement(), //
            null) //
        .add(DoStatement.class, //
            new DoWhileEmptyBlockToEmptyStatement(), //
            null)
        .add(SwitchStatement.class, //
            new SwitchEmpty(), //
            new MergeSwitchBranches(), //
            null)
        .add(SwitchCase.class, //
            new RemoveRedundantSwitchCases(), //
            null)
        .add(Assignment.class, //
            new AssignmentAndAssignmentOfSameValue(), //
            new AssignmentAndAssignmentToSame(), //
            new AssignmentAndReturn(), //
            new AssignmentToFromInfixIncludingTo(), //
            new AssignmentToPrefixIncrement(), //
            new AssignmentUpdateAndSameUpdate(), //
            new AssignmentAndAssignmentOfSameVariable(), //
            new PlusAssignToPostfix(), null) //
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
            // new InfixIndexOfToStringContains(), // v 2.7
            new InfixSimplifyComparisionOfAdditions(), //
            new InfixSimplifyComparisionOfSubtractions(), //
            new InfixStringLiteralsConcatenate(), //
            null)
        .add(MethodDeclaration.class, //
            new AnnotationSort<>(), //
            new MethodDeclarationRenameReturnToDollar(), //
            new BodyDeclarationModifiersSort<>(), //
            new MethodDeclarationRenameSingleParameterToIt(), //
            new MethodDeclarationConstructorMoveToInitializers(), //
            new RenameConstructorParameters(), //
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
            new IfFooBarElseFooBaz(), //
            new IfFooBarElseBazBar(), //
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
            new IfFooElseIfBarElseFoo(), //
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
            new FieldSerialVersionUIDToHexadecimal(), //
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
            new FieldFragmentInitializerDefaultValue(), //
            new FragmentNoInitializerAssignment(), //
            new LocalVariableIntializedUpdateAssignment(), //
            new FragmentInitializerIfAssignment(), //
            new LocalInitializedUpdateAssignment(), //
            new FragmentInitializerReturnVariable(), //
            new LocalInitializedReturnExpression(), //
            new FragmentInitializerReturnAssignment(), //
            new FragmentInitializerReturn(), //
            new FragmentInitializerStatementTerminatingScope(), //
            new LocalVariableIntializedAssignment(), //
            new LocalVariableUninitializedDead(), //
            new FragmentInitializerInlineIntoNext(), //
            new FragmentInitializerWhile(), //
            new FragmentInitializerToForInitializers(), //
            new FragmentRenameUnderscoreToDoubleUnderscore<>(), //
            new LocalVariableInitializedUnusedRemove(), //
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
    return ts.stream().filter(λ -> ((Tipper<N>) λ).check(n)).map(λ -> (Tipper<N>) λ).findFirst().orElse(null);
  }

  /** Implementation */
  @SuppressWarnings("unchecked") public final List<Tipper<? extends ASTNode>>[] implementation = //
      (List<Tipper<? extends ASTNode>>[]) new List<?>[2 * ASTNode.TYPE_METHOD_REFERENCE];

  /** Associate a bunch of{@link Tipper} with a given sub-class of
   * {@link ASTNode}.
   * @param c JD
   * @param ts JD
   * @return {@code this}, for easy chaining. */
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
          String.format("Did you forget to use create an enum instance in %s \nfor the %s of tipper %s \n (description= %s)?", //
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
   *         {@code null if no such {@link Tipper} is found. @ */
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
    return Stream.of(implementation).filter(Objects::nonNull).mapToInt(List::size).sum();
  }

  public int nodesTypeCount() {
    return (int) Stream.of(implementation).filter(Objects::nonNull).count();
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
        .forEach(element -> $.addAll(element.stream().filter(λ -> ¢.equals(λ.tipperGroup())).map(Tipper::technicalName).collect(toList())));
    return $;
  }

  public static TipperGroup groupFor(@SuppressWarnings("rawtypes") final Class<? extends Tipper> tipperClass) {
    return categoryMap == null || !categoryMap.containsKey(tipperClass) ? null : categoryMap.get(tipperClass);
  }
}