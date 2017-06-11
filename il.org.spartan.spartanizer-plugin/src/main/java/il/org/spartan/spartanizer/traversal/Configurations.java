package il.org.spartan.spartanizer.traversal;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.plugin.preferences.revision.PreferencesResources.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-12 */
@SuppressWarnings("unused")
public interface Configurations {
  /** The default instance of this class; tippers not found here, do not exist!
   * if you need to disable tippers, or update this, make a copy using
   * {@link #allClone()} */
  lazy<Configuration> all = lazy.get(() -> new Configuration()//
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
          new ParameterAbbreviate(), //
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
          // new InfixIndexOfToStringContains(), // v 2.7
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
          new TernarySameValueEliminate(), null) //
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
      .add(AnnotationTypeMemberDeclaration.class, new BodyDeclarationModifiersSort<>(), //
          new AnnotationSort<>(), //
          null)
      .add(VariableDeclarationFragment.class, //
          new FieldInitializedDefaultValue(), //
          new ParameterRenameUnderscoreToDoubleUnderscore<>(), //
          new LocalUninitializedAssignment(), //
          new LocalUninitializedDead(), //
          new LocalInitializedAssignment(), new LocalInitializedUpdateAssignment(), //
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

  static Configuration all() {
    return all.get();
  }
  static Stream<Tipper<? extends ASTNode>> allTippers() {
    return Stream.of(all().implementation)//
        .filter(λ -> λ != null && !λ.isEmpty())//
        .flatMap(Collection::stream) //
    ;
  }
  static Configuration empty() {
    return new Configuration();
  }
  static Configuration allClone() {
    return all().clone();
  }
  static List<String> get(final Taxon ¢) {
    final List<String> $ = an.empty.list();
    if (¢ == null)
      return $;
    final Configuration t = allClone();
    assert t.implementation != null;
    Stream.of(t.implementation).filter(Objects::nonNull)
        .forEach(element -> $.addAll(element.stream().filter(λ -> ¢.equals(λ.tipperGroup())).map(Tipper::technicalName).collect(toList())));
    return $;
  }
  static Taxon groupOf(@SuppressWarnings("rawtypes") final Class<? extends Tipper> tipperClass) {
    return categoryMap == null || !categoryMap.containsKey(tipperClass) ? null : categoryMap.get(tipperClass);
  }
  static long hooksCount() {
    return allTippers().count();
  }
  static void main(final String[] args) {
    final Configuration t = all();
    System.out.printf("Currently, there are a total of %d tippers offered on %d classes", box.it(t.tippersCount()), box.it(t.nodesTypeCount()));
  }
  /** Make a for a specific kind of tippers
   * @param clazz JD
   * @param w JS
   * @return a new configuration containing only the tippers passed as
   *         parameter */
  @SafeVarargs static <N extends ASTNode> Configuration make(final Class<N> clazz, final Tipper<N>... ts) {
    return empty().add(clazz, ts);
  }
  static String name(final Class<? extends Tipper<?>> ¢) {
    return ¢.getSimpleName();
  }

  @SuppressWarnings("rawtypes") Map<Class<? extends Tipper>, Taxon> categoryMap = new HashMap<Class<? extends Tipper>, Taxon>() {
    static final long serialVersionUID = -0x185C3A40849E91FAL;
    {
      Stream.of(allClone().implementation).filter(Objects::nonNull).forEach(ts -> ts.forEach(λ -> put(λ.getClass(), λ.tipperGroup())));
    }
  };

  static Taxon groupOf(final Tip ¢) {
    return groupOf(¢.tipperClass);
  }
}
