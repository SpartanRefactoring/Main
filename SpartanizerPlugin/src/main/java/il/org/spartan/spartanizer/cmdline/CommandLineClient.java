package il.org.spartan.spartanizer.cmdline;

import il.org.spartan.spartanizer.cmdline.report.*;

/** Simplified version of command line client that uses spartanizer applicator
 * @author Matteo Orru' */
public class CommandLineClient {
  // TODO Matteo: try to fix compilation errors - matteo
  static String inputDir = ".";
  private static String outputDir = "/tmp";
  @SuppressWarnings("unused") private static MetricsReport metricsReport = new MetricsReport();

  public static void main(final String[] args) {
    if (args.length == 0)
      processCommandLine(args);
  }

  @SuppressWarnings({ "unused" }) private static void processCommandLine(final String[] args) {
    new CommandLineClient();
    // final List<String> remaining = extract(args, r);
    //
    MetricsReport.getSettings().setInputFolder(inputDir);
    MetricsReport.getSettings().setOutputFolder(outputDir);
    MetricsReport.initialize();
    // ReportGenerator.setOutputFolder(outputDir);
    // ReportGenerator.setInputFolder(inputDir);
    //
    new CommandLineSpartanizer(inputDir).apply();
    // r.printExternals();
    MetricsReport.write();
  }

  static void printPrompt() {
    System.out.println("Help");
    System.out.println("");
    System.out.println("Options:");
    System.out.println("  -d       default directory: use the current directory for the analysis");
    System.out.println("  -o       output directory: here go the results of the analysis");
    System.out.println("  -i       input directory: place here the projects that you want to analyze.");
    System.out.println("");
    System.out.println("===================");
    System.out.println("List of Node Types:");
    System.out.println("===================");
    System.out.println("EnhancedForStatement, Modifier, VariableDeclarationExpression, ThrowStatement\n"
        + "CastExpression, ClassInstanceCreation, SuperConstructorInvocation, SingleVariableDeclaration\n"
        + "ForStatement, WhileStatement, Assignment, Block, PostfixExpression, InfixExpression\n"
        + "InstanceofExpression, MethodDeclaration, MethodInvocation, IfStatement, PrefixExpression\n"
        + "ConditionalExpression, TypeDeclaration, EnumDeclaration, FieldDeclaration, CastExpression\n"
        + "EnumConstantDeclaration, NormalAnnotation, Initializer, VariableDeclarationFragment\n");
    System.out.println("================");
    System.out.println("List of Tippers:");
    System.out.println("================");
    System.out.println("EnhancedForParameterRenameToCent, RedundantModifier, ForRenameInitializerToCent\n"
        + "ThrowNotLastInBlock, Coercion, ClassInstanceCreationValueTypes, SuperConstructorInvocationRemover\n"
        + "SingleVariableDeclarationAbbreviation, SingelVariableDeclarationUnderscoreDoubled\n"
        + "SingleVariableDeclarationEnhancedForRenameParameterToCent, BlockBreakToReturnInfiniteFor\n"
        + "ReturnToBreakFiniteFor, RemoveRedundentFor, ForToForUpdaters, ForTrueConditionRemove\n"
        + "BlockBreakToReturnInfiniteWhile, ReturnToBreakFiniteWhile, RemoveRedundantWhile, WhileToForUpdaters\n"
        + "AssignmentAndAssignment, AssignmentAndReturn, AssignmentToFromInfixIncludingTo, AssignmentToPostfixIncrement\n"
        + "BlockSimplify, BlockSingleton, CachingPattern, BlockInlineStatementIntoNext, PostfixToPrefix\n"
        + "InfixMultiplicationEvaluate, InfixDivisionEvaluate, InfixRemainderEvaluate, InfixComparisonSizeToZero\n"
        + "InfixSubtractionZero, InfixAdditionSubtractionExpand, InfixEmptyStringAdditionToString\n"
        + "InfixConcatenationEmptyStringLeft, InfixFactorNegatives, InfixAdditionEvaluate, InfixSubtractionEvaluate\n"
        + "InfixTermsZero, InfixPlusRemoveParenthesis, InfixAdditionSort, InfixComparisonBooleanLiteral\n"
        + "InfixConditionalAndTrue, InfixConditionalOrFalse, InfixComparisonSpecific, InfixMultiplicationByOne\n"
        + "InfixMultiplicationByZero, InfixMultiplicationSort, InfixPseudoAdditionSort, InfixSubtractionSort\n"
        + "InfixDivisonSortRest, InfixConditionalCommon, InfixIndexOfToStringContains, InstanceOf\n"
        + "MethodDeclarationRenameReturnToDollar, MethodDeclarationRenameSingleParameterToCent\n"
        + "SetterGoFluent, MethodInvocationEqualsWithLiteralString, MethodInvocationValueOfBooleanConstant\n"
        + "MethodInvocationToStringToEmptyStringAddition, IfTrueOrFalse, RemoveRedundantIf\n"
        + "IfLastInMethodThenEndingWithEmptyReturn, IfLastInMethodElseEndingWithEmptyReturn\n"
        + "IfLastInMethod, IfReturnFooElseReturnBar, IfReturnNoElseReturn, IfAssignToFooElseAssignToFoo\n"
        + "IfThenFooBarElseFooBaz, IfBarFooElseBazFoo, IfThrowFooElseThrowBar, IfThrowNoElseThrow\n"
        + "IfExpressionStatementElseSimilarExpressionStatement, IfThenOrElseIsCommandsFollowedBySequencer\n"
        + "IfFooSequencerIfFooSameSequencer, IfCommandsSequencerNoElseSingletonSequencer\n"
        + "IfPenultimateInMethodFollowedBySingleStatement, IfThenIfThenNoElseNoElse, IfEmptyThenEmptyElse\n"
        + "IfDegenerateElse, IfEmptyThen, IfShortestFirst, PutIfAbsent, PrefixIncrementDecrementReturn\n"
        + "PrefixNotPushdown, PrefixPlusRemove, TernaryBooleanLiteral, TernaryCollapse, TernaryEliminate\n"
        + "TernaryShortestFirst, TernaryPushdown, TernaryPushdownStrings, CastToDouble2Multiply1\n"
        + "CastToLong2Multiply1L, AnnotationDiscardValueName, AnnotationRemoveEmptyParentheses\n"
        + "DeclarationRedundantInitializer, DeclarationAssignment, DeclarationInitialiazelUpdateAssignment\n"
        + "DeclarationInitializerIfAssignment, DeclarationInitializerIfUpdateAssignment\n"
        + "DeclarationInitializerReturnVariable, DeclarationInitializerReturnExpression\n"
        + "DeclarationInitializerReturnAssignment, DeclarationInitializerReturnUpdateAssignment\n"
        + "DeclarationInitializerStatementTerminatingScope, DeclarationInitialiazerAssignment\n"
        + "ForToForInitializers, WhileToForInitializers"
        );
    System.out.println("======================");
    System.out.println("List of Nano Patterns:");
    System.out.println("======================");
    System.out.println(" to be completed ... ");
  }
}
