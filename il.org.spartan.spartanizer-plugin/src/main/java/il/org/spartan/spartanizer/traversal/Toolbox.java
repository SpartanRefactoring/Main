package il.org.spartan.spartanizer.traversal;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** Singleton containing all {@link Tipper}s which are active. This class does
 * minimal dispatching at the node level, selecting and applying the most
 * appropriate such object for a given {@link ASTNode}. Dispatching at the tree
 * level is done in class {@link TraversalImplementation}
 * @author Yossi Gil
 * @since 2015-08-22 */
public class Toolbox {
  @Override public Toolbox clone() {
    final Toolbox $ = new Toolbox();
    int i = 0;
    for (final List<Tipper<? extends ASTNode>> ¢ : implementation)
      $.implementation[i++] = ¢ == null ? null : new ArrayList<>(¢);
    return $;
  }
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
        Tippers.extractTip(t, n).go($, null);
        return false;
      }
    });
    return $;
  }
  private static void disable(final Class<? extends Category> c, final List<Tipper<? extends ASTNode>> ts) {
    ts.removeIf(λ -> c.isAssignableFrom(λ.getClass()));
  }
  @SuppressWarnings("unchecked") private static <N extends ASTNode> Tipper<N> firstTipper(final N n, final List<Tipper<?>> ts) {
    return ts.stream()//
        .map(λ -> (Tipper<N>) λ)//
        .filter(λ -> λ.check(n))//
        .findFirst()//
        .orElse(null);
  }

  /** Implementation */
  @SuppressWarnings("unchecked") public final List<Tipper<? extends ASTNode>>[] implementation = //
      (List<Tipper<? extends ASTNode>>[]) new List<?>[2 * ASTNode.TYPE_METHOD_REFERENCE];
  /** The default instance of this class; tippers not found here, do not exist!
   * if you need to disable tippers, or update this, make a copy using
   * {@link Toolboxes#allClone()} */
  public static final lazy<Toolbox> full = lazy.get(() -> new Toolbox()//
      .add(SingleMemberAnnotation.class, new AnnotationRemoveSingletonArrray()) //
      //.add(EmptyStatement.class, new EmptyStatementRemove()) //
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
      .add(ClassInstanceCreation.class, //
          new ClassInstanceCreationBoxedValueTypes(), //
          new StringFromStringBuilder(), //
          null) //
      .add(SuperConstructorInvocation.class, new SuperConstructorInvocationRemover()) //
      .add(ExpressionStatement.class, new ExpressionStatementAssertTrueFalse(), new ExpressionStatementThatIsBooleanLiteral(), null) //
      .add(ReturnStatement.class, //
          new ReturnLastInMethod(), //
          new ReturnDeadAssignment(), //
          new SequencerNotLastInBlock<>(), null) //
      .add(EnhancedForStatement.class, //
          new EnhancedForRedundantContinue(), //
          new EnhancedForEliminateConditionalContinue(), //
          new EnhancedForEmptyBlock(), //
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
          // new ParameterAbbreviate(), //
          new ParameterAnonymize(), //
          new ParameterRenameUnderscoreToDoubleUnderscore<>(), //
          new ForParameterRenameToIt(), null)//
      .add(ForStatement.class, //
          new ForNoUpdatersNoInitializerToWhile(), //
          new ForDeadRemove(), //
          new ContinueCoditinalInForEliminate(), //
          new InfiniteForBreakToReturn(), //
          // new ForFiniteConvertReturnToBreak(), // TODO Dor: your bug
          new ForToForUpdaters(), //
          new ForTrueConditionRemove(), //
          new ForAndReturnToFor(), //
          new ForRedundantContinue(), //
          new ForEmptyBlockToEmptyStatement(), //
          null)//
      .add(WhileStatement.class, //
          new EliminateConditionalContinueInWhile(), //
          new WhileInfiniteBreakToReturn(), //
          new WhileFiniteReturnToBreak(), //
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
          new SwitchSingleCaseToIf(), //
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
          new AssignmentAndAssignmentToSameKill(), //
          new PlusAssignToPostfix(), null) //
      .add(Block.class, //
          new BlockSimplify(), //
          new BlockSingletonEliminate(), //
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
          new InfixSimplifyComparisionOfAdditions(), //
          new InfixSimplifyComparisionOfSubtractions(), //
          new InfixStringLiteralsConcatenate(), //
          null)
      .add(MethodDeclaration.class, //
          new AnnotationSort<>(), //
          new MethodDeclarationRenameReturnToDollar(), //
          new BodyDeclarationModifiersSort<>(), //
          new MethodDeclarationRenameSingleParameter(), //
          // new ConstructorRenameParameters(), //
          new ConstructorEmptyRemove(), //
          new MethodDeclarationConstructorMoveToInitializers(), //
          new MethodDeclarationOverrideDegenerateRemove(), //
          // new AnonymizeAllParameters(), //
          null)
      .add(MethodInvocation.class, //
          new MethodInvocationEqualsWithLiteralString(), //
          new MethodInvocationValueOfBooleanConstant(), //
          new MethodInvocationToStringToEmptyStringAddition(), //
          null)//
      .add(TryStatement.class, //
          new TryBodyEmptyLeaveFinallyIfExists(), //
          new TryBodyEmptyNoCatchesNoFinallyEliminate(), //
          new TryBodyNotEmptyNoCatchesNoFinallyRemove(), //
          new TryFinallyEmptyRemove(), //
          new TryMergeCatchers(), //
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
          new IfStatementBlockSequencerBlockSameSequencer(), //
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
          new TernarySameValueEliminate(), //
          new TernaryBranchesAreOppositeBooleans(), //
          null) //
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
          new FieldInitializedSerialVersionUIDToHexadecimal(), //
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
      .add(AnnotationTypeMemberDeclaration.class, //
          new BodyDeclarationModifiersSort<>(), //
          new AnnotationSort<>(), //
          null)
      .add(VariableDeclarationFragment.class, //
          new FieldInitializedDefaultValue(), //
          new ParameterRenameUnderscoreToDoubleUnderscore<>(), //
          new LocalUninitializedAssignment(), //
          new LocalUninitializedDead(), //
          new LocalInitializedAssignment(), //
          new LocalInitializedUpdateAssignment(), //
          new LocalInitializedReturnExpression(), //
          new LocalInitializedIfAssignment(), //
          new LocalInitializedIfAssignmentUpdating(), //
          new LocalInitializedReturn(), //
          new LocalInitializedStatementReturnVariable(), //
          new LocalInitializedStatementReturnAssignment(), //
          new LocalInitializedStatementTerminatingScope(), //
          new LocalInitializedInlineIntoNext(), //
          new LocalInitializedStatementWhile(), //
          new LocalInitializedStatementToForInitializers(), //
          new LocalInitializedUnusedRemove(), //
          new LocalInitializedIncrementDecrementInline(), //
          new LocalInitializedNewAddAll(), //
          new LocalInitializedArithmeticsInline(), //
          null) //
  );

  /** Associate a bunch of{@link Tipper} with a given sub-class of
   * {@link ASTNode}.
   * @param c JD
   * @param ts JD
   * @return {@code this}, for easy chaining. */
  @SafeVarargs public final <N extends ASTNode> Toolbox add(final Class<N> c, final Tipper<N>... ts) {
    return add(wizard.nodeType(c), ts);
  }
  @SafeVarargs public final <N extends ASTNode> Toolbox add(final int nodeType, final Tipper<N>... ts) {
    for (final Tipper<N> ¢ : ts) {
      if (¢ == null)
        break;
      get(nodeType).add(¢);
    }
    return this;
  }
  @SafeVarargs public final <N extends ASTNode> Toolbox remove(final Class<N> c, final Tipper<N>... ts) {
    final int nodeType = wizard.nodeType(c);
    for (final Tipper<N> ¢ : ts)
      get(nodeType).remove(¢);
    return this;
  }
  public Collection<Tipper<? extends ASTNode>> getAllTippers() {
    final Collection<Tipper<? extends ASTNode>> $ = an.empty.list();
    for (int ¢ = 0; ¢ < implementation.length; ++¢)
      $.addAll(get(¢));
    return $;
  }
  public void disable(final Class<? extends Category> c) {
    Stream.of(implementation).filter(Objects::nonNull).forEach(λ -> disable(c, λ));
  }
  /** Find the first {@link Tipper} appropriate for an {@link ASTNode}
   * @param pattern JD
   * @return first {@link Tipper} for which the parameter is within scope, or
   *         {@code null if no such {@link Tipper} is found. @ */
  public <N extends ASTNode> Tipper<N> firstTipper(final N ¢) {
    return firstTipper(¢, get(¢));
  }
  public List<Tipper<? extends ASTNode>> get(final int ¢) {
    return implementation[¢] = implementation[¢] == null ? an.empty.list() : implementation[¢];
  }
  public int tippersCount() {
    return Stream.of(implementation).filter(Objects::nonNull).mapToInt(List::size).sum();
  }
  public long nodesTypeCount() {
    return Stream.of(implementation).filter(Objects::nonNull).count();
  }
  List<Tipper<? extends ASTNode>> get(final ASTNode ¢) {
    return get(¢.getNodeType());
  }
  /** Clears the toolbox except of the given tippers */
  @SafeVarargs public final Toolbox restrictTo(final Tipper<?>... ts) {
    Stream.of(implementation).filter(Objects::nonNull).forEach(x -> x.removeIf(λ -> !is.in(λ, ts)));
    return this;
  }
  @SafeVarargs public final <N extends ASTNode> Toolbox setTo(final Class<N> c, final Tipper<N>... ts) {
    clear().get(wizard.nodeType(c)).addAll(as.list(ts));
    return this;
  }
  public Toolbox clear() {
    Stream.of(implementation).filter(Objects::nonNull).forEach(List::clear);
    return this;
  }
  public static void main(final String[] args) {
    final Toolbox t = full();
    System.out.printf("Currently, there are a total of %d tippers offered on %d classes", box.it(t.tippersCount()), box.it(t.nodesTypeCount()));
  }
  public static Toolbox full() {
    return full.get();
  }
  public static Stream<Tipper<? extends ASTNode>> fullStream() {
    return Stream.of(full().implementation)//
        .filter(λ -> λ != null && !λ.isEmpty())//
        .flatMap(Collection::stream) //
    ;
  }
}