package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.external.External.Introspector.*;

import il.org.spartan.external.*;
import il.org.spartan.spartanizer.cmdline.report.*;

/** Simplified version of command line client that uses spartanizer applicator
 * @author Matteo Orru' */
public final class CommandLineClient {
  @External(alias = "i", value = "name of the input directory") static String inputDir = ".";
  @External(alias = "o", value = "name of the output directory") private static String outputDir = "/tmp";
  @External(alias = "d", value = "default values for the directories") private static boolean devault;
  @External(alias = "cs", value = "class name on which apply the tippers") private static String[] clazzes;
  @External(alias = "tg", value = "tipper group to be applied to the clazzes") private static String[] tipperGroups;
  @External(alias = "etg", value = "exclude one or more tipper groups") private static String[] excludeTipperGroups;
  @External(alias = "np", value = "Nano Patterns") private static String[] NanoPatterns;
  @External(alias = "enp", value = "Exclude Selected Nano Patterns") private static String[] excludeNanoPatterns;
  @External(alias = "xallnp", value = "Exclude All Nano Patterns") private static boolean excludeAllNanoPatterns;
 private final MetricsReport metricsReport = new MetricsReport();

  public static void main(final String[] args) {
    System.out.println(args.length);
    new CommandLineClient().go(args);
  }

  private void go(String[] args) {
    
    // TODO Yossi, the instruction
    // External.Introspector.extract(args, this).isEmpty()
    // returns true (an empty list) even if args.length() > 0
    // I changed it

    if (args.length == 0) {
      System.err.println(usage(this, args, this));
      return;
    }
    
    extract(args, this);
 
//    MetricsReport.getSettings().setInputFolder(inputDir);
//    MetricsReport.getSettings().setOutputFolder(outputDir);
//    MetricsReport.initialize();
    ReportGenerator.generate("metrics");
    ReportGenerator.setOutputFolder(outputDir);
    ReportGenerator.setInputFolder(inputDir);
    run();
//    MetricsReport.generate();
  }
  
  @SuppressWarnings("static-method")
  private void run() {
    // new CommandLineSpartanizer(inputDir).apply();
    CommandLineSpartanizer s = new CommandLineSpartanizer();
    s.inputDir(inputDir);
    s.name(system.folder2File(inputDir));
    if (clazzes != null)
      s.setClazzes(clazzes);
    if (tipperGroups != null)
      s.setTipperGroups(tipperGroups);
    if (excludeTipperGroups != null)
      s.setExcludeTipperGroups(excludeTipperGroups);
    if (NanoPatterns != null)
      s.setNanoPatterns(NanoPatterns);
    if (excludeNanoPatterns != null)
      s.setExcludeNanoPatterns(excludeNanoPatterns);
    if (excludeAllNanoPatterns)
      s.setExcludeAllNanoPatterns(excludeAllNanoPatterns);
    s.apply();
    // r.printExternals();
  }

  static void printPrompt() {
    // System.out.println("Help");
    System.err.println("===================");
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
        + "DeclarationInitializerStatementTerminatingScope, DeclarationInitialiazerAssignment\n" + "ForToForInitializers, WhileToForInitializers");
    System.out.println("======================");
    System.out.println("List of Nano Patterns:");
    System.out.println("======================");
    System.out.println(" to be completed ... ");
  }
}
