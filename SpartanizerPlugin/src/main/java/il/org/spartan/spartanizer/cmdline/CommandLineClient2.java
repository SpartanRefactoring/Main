package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.external.External.Introspector.*;

import java.util.*;

import il.org.spartan.external.*;
import il.org.spartan.spartanizer.cmdline.report.*;

/** Simplified version of command line client that uses spartanizer applicator
 * @author Matteo Orru' */
public class CommandLineClient2 {
  // TODO Matteo: try to fix compilation errors - matteo
  @External(alias = "i", value = "name of the input directory") static String inputDir = ".";
  @External(alias = "o", value = "name of the output directory") private static String outputDir = "/tmp";
  @External(alias = "d", value = "default values for the directories") private static boolean devault;
  @External(alias = "cs", value = "class name on which apply the tippers") private static String[] clazzes;
  @External(alias = "tg", value = "tipper group to be applied to the clazzes") private static String[] tipperGroups;
  @External(alias = "etg", value = "exclude one or more tipper groups") private static String[] excludeTipperGroups;
  @External(alias = "np", value = "Nano Patterns") private static String[] NanoPatterns;
  @External(alias = "enp", value = "Exclude Selected Nano Patterns") private static String[] excludeNanoPatterns;
  @External(alias = "allnp", value = "Exclude All Nano Patterns") private static boolean excludeAllNanoPatterns;
//  private static MetricsReport metricsReport = new MetricsReport();
  
  public static void main(final String[] args) {
//    for (final String ¢ : args.length != 0 ? args : as.array("."))
//      new CommandLineSpartanizer(¢).apply();
    if(args.length == 0){
//      usage(inputDir);
      usageErrorExit("name(s)", new CommandLineClient2());
      printPrompt();
    }
    processCommandLine(args);

    MetricsReport.getSettings().setInputFolder(inputDir);
    MetricsReport.getSettings().setOutputFolder(outputDir);
    MetricsReport.initialize();

    ReportGenerator.generate("metrics");
    ReportGenerator.setOutputFolder(outputDir);
    ReportGenerator.setInputFolder(inputDir);
    
    run();
    
    MetricsReport.generate();
  }
  
  @SuppressWarnings("unused") private static void processCommandLine(final String[] args) {
    final List<String> remaining = extract(args, (new CommandLineClient2()));
  }

  private static void run() {
    //    new CommandLineSpartanizer(inputDir).apply();
        CommandLineSpartanizer s = new CommandLineSpartanizer();
        s.inputDir(inputDir);
        s.name(system.folder2File(inputDir));
        if(clazzes != null)
          s.setClazzes(clazzes);
        if(tipperGroups != null)
          s.setTipperGroups(tipperGroups);
        if(excludeTipperGroups != null)
          s.setExcludeTipperGroups(excludeTipperGroups);
        if(NanoPatterns != null)
          s.setNanoPatterns(NanoPatterns);
        if(excludeNanoPatterns != null)
          s.setExcludeNanoPatterns(excludeNanoPatterns);
        if(excludeAllNanoPatterns)
          s.setExcludeAllNanoPatterns(excludeAllNanoPatterns);
        s.apply();
    //    r.printExternals();
  }
  
  @SuppressWarnings("unused") private void printExternals() {
    System.out.println(usage(this));
    System.out.println("Externals after processing command line arguments:");
    System.out.println("==================================================");
    System.out.println("outputDir: " + outputDir());
    System.out.println("inputDir: " + inputDir());
    System.out.println();
} 

  @SuppressWarnings({ "static-method" }) 
  private String inputDir() {
    return inputDir;
  }

  @SuppressWarnings({ "static-method" }) 
  private String outputDir() {
    return outputDir;
  }

  @SuppressWarnings("unused") private static void parseCommandLineArgs(final String[] args) {
    if (args.length == 0)
      printPrompt();
    for (int ¢ = 0; ¢ < args.length;)
      if ("-o".equals(args[¢]))
        ¢ += 2;
      else if ("-i".equals(args[¢])) {
        inputDir = args[¢ + 1];
        ¢ += 2;
      } else if ("-d".equals(args[¢])) {
        inputDir = ".";
        ¢ += 1;
      } else {
        System.out.println(args[¢]);
        System.out.println("[ERROR]: Something went wrong! Parameter or switch not allowed");
        ++¢;
      }
  }

  static void printPrompt() {
//    System.out.println("Help");
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
        + "DeclarationInitializerStatementTerminatingScope, DeclarationInitialiazerAssignment\n"
        + "ForToForInitializers, WhileToForInitializers"
        );
    System.out.println("======================");
    System.out.println("List of Nano Patterns:");
    System.out.println("======================");
    System.out.println(" to be completed ... ");
  } 
  
//  .add(EnhancedForStatement.class, //
//      new EnhancedForParameterRenameToCent(), //
//      null)//
//  .add(Modifier.class, new RedundantModifier())//
//  .add(VariableDeclarationExpression.class, new ForRenameInitializerToCent()) //
//  .add(ThrowStatement.class, //
//      new ThrowNotLastInBlock(), //
//      null) //
//  // TODO: Marco add this tipper when it's ready
//  // .add(CastExpression.class, //
//  // new Coercion(), //
//  // null)
//  .add(ClassInstanceCreation.class, //
//      new ClassInstanceCreationValueTypes(), //
//      null) //
//  .add(SuperConstructorInvocation.class, new SuperConstructorInvocationRemover()) //
//  .add(SingleVariableDeclaration.class, //
//      new SingleVariableDeclarationAbbreviation(), //
//      new SingelVariableDeclarationUnderscoreDoubled(), //
//      new VariableDeclarationRenameUnderscoreToDoubleUnderscore<SingleVariableDeclaration>(), //
//      new SingleVariableDeclarationEnhancedForRenameParameterToCent(), //
//      null)//
//  .add(ForStatement.class, //
//      new BlockBreakToReturnInfiniteFor(), //
//      new ReturnToBreakFiniteFor(), //
//      new RemoveRedundentFor(), //
//      new ForToForUpdaters(), //
//      new ForTrueConditionRemove(), //
//      null)//
//  .add(WhileStatement.class, //
//      new BlockBreakToReturnInfiniteWhile(), //
//      new ReturnToBreakFiniteWhile(), //
//      new RemoveRedundantWhile(), //
//      new WhileToForUpdaters(), //
//      null) //
//  .add(Assignment.class, //
//      new AssignmentAndAssignment(), //
//      new AssignmentAndReturn(), //
//      new AssignmentToFromInfixIncludingTo(), //
//      new AssignmentToPostfixIncrement(), //
//      null) //
//  .add(Block.class, //
//      new BlockSimplify(), //
//      new BlockSingleton(), //
////      new CachingPattern(), // // TODO Ori Marco - I temporarily disabled this because it is raising an 
//      // exception in commons-lang -- matteo
//      new BlockInlineStatementIntoNext(), //
//      null) //
//  .add(PostfixExpression.class, //
//      new PostfixToPrefix(), //
//      null) //
//  .add(InfixExpression.class, //
//      new InfixMultiplicationEvaluate(), //
//      new InfixDivisionEvaluate(), //
//      new InfixRemainderEvaluate(), //
//      new InfixComparisonSizeToZero(), //
//      new InfixSubtractionZero(), //
//      new InfixAdditionSubtractionExpand(), //
//      new InfixEmptyStringAdditionToString(), //
//      new InfixConcatenationEmptyStringLeft(), //
//      new InfixFactorNegatives(), //
//      new InfixAdditionEvaluate(), //
//      new InfixSubtractionEvaluate(), //
//      new InfixTermsZero(), //
//      new InfixPlusRemoveParenthesis(), //
//      new InfixAdditionSort(), //
//      new InfixComparisonBooleanLiteral(), //
//      new InfixConditionalAndTrue(), //
//      new InfixConditionalOrFalse(), //
//      new InfixComparisonSpecific(), //
//      new InfixMultiplicationByOne(), //
//      new InfixMultiplicationByZero(), //
//      new InfixMultiplicationSort(), //
//      new InfixPseudoAdditionSort(), //
//      new InfixSubtractionSort(), //
//      new InfixDivisonSortRest(), //
//      new InfixConditionalCommon(), //
//      new InfixIndexOfToStringContains(), //
//      null)
//  // TODO: Marco add when ready
//  // .add(InstanceofExpression.class, //
//  // new InstanceOf(), //
//  // null)//
//  .add(MethodDeclaration.class, //
//      new MethodDeclarationRenameReturnToDollar(), //
//      new $BodyDeclarationModifiersSort.ofMethod(), //
//      new MethodDeclarationRenameSingleParameterToCent(), //
////      new SetterGoFluent(), // TODO Ori Marco - I temporarily disabled this because it is raising an 
//      // exception in commons-lang -- matteo
//      null)
//  .add(MethodInvocation.class, //
//      new MethodInvocationEqualsWithLiteralString(), //
//      new MethodInvocationValueOfBooleanConstant(), //
//      new MethodInvocationToStringToEmptyStringAddition(), //
//      null)//
//  .add(IfStatement.class, //
//      new IfTrueOrFalse(), //
//      new RemoveRedundantIf(), //
//      new IfLastInMethodThenEndingWithEmptyReturn(), //
//      new IfLastInMethodElseEndingWithEmptyReturn(), //
//      new IfLastInMethod(), //
//      new IfReturnFooElseReturnBar(), //
//      new IfReturnNoElseReturn(), //
//      new IfAssignToFooElseAssignToFoo(), //
//      new IfThenFooBarElseFooBaz(), //
//      new IfBarFooElseBazFoo(), //
//      new IfThrowFooElseThrowBar(), //
//      new IfThrowNoElseThrow(), //
//      new IfExpressionStatementElseSimilarExpressionStatement(), //
//      new IfThenOrElseIsCommandsFollowedBySequencer(), //
//      new IfFooSequencerIfFooSameSequencer(), //
//      new IfCommandsSequencerNoElseSingletonSequencer(), //
//      new IfPenultimateInMethodFollowedBySingleStatement(), //
//      new IfThenIfThenNoElseNoElse(), //
//      new IfEmptyThenEmptyElse(), //
//      new IfDegenerateElse(), //
//      new IfEmptyThen(), //
//      new IfShortestFirst(), //
////      new PutIfAbsent(), // TODO Ori Marco - I temporarily disabled this because it is raising an 
//      // exception in commons-lang -- matteo
//      null)//
//  .add(PrefixExpression.class, //
//      new PrefixIncrementDecrementReturn(), //
//      new PrefixNotPushdown(), //
//      new PrefixPlusRemove(), //
//      null) //
//  .add(ConditionalExpression.class, //
//      new TernaryBooleanLiteral(), //
//      new TernaryCollapse(), //
//      new TernaryEliminate(), //
//      new TernaryShortestFirst(), //
//      new TernaryPushdown(), //
//      new TernaryPushdownStrings(), //
//      null) //
//  .add(TypeDeclaration.class, //
//      new $BodyDeclarationModifiersSort.ofType(), //
//      null) //
//  .add(EnumDeclaration.class, //
//      new $BodyDeclarationModifiersSort.ofEnum(), //
//      null) //
//  .add(FieldDeclaration.class, //
//      new $BodyDeclarationModifiersSort.ofField(), //
//      null) //
//  .add(CastExpression.class, //
//      new CastToDouble2Multiply1(), //
//      new CastToLong2Multiply1L(), //
//      null) //
//  .add(EnumConstantDeclaration.class, //
//      new $BodyDeclarationModifiersSort.ofEnumConstant(), //
//      null) //
//  .add(NormalAnnotation.class, //
//      new AnnotationDiscardValueName(), //
//      new AnnotationRemoveEmptyParentheses(), //
//      null) //
//  .add(Initializer.class, new $BodyDeclarationModifiersSort.ofInitializer(), //
//      null) //
//  .add(VariableDeclarationFragment.class, //
//      new DeclarationRedundantInitializer(), //
//      new DeclarationAssignment(), //
//      new DeclarationInitialiazelUpdateAssignment(), //
//      new DeclarationInitializerIfAssignment(), //
//      new DeclarationInitializerIfUpdateAssignment(), //
//      new DeclarationInitializerReturnVariable(), //
//      new DeclarationInitializerReturnExpression(), //
//      new DeclarationInitializerReturnAssignment(), //
//      new DeclarationInitializerReturnUpdateAssignment(), //
//      new DeclarationInitializerStatementTerminatingScope(), //
//      new DeclarationInitialiazerAssignment(), //
//      new VariableDeclarationRenameUnderscoreToDoubleUnderscore<VariableDeclarationFragment>(), //
//      new ForToForInitializers(), //
//      new WhileToForInitializers(), //
//      null) //
////
;
}
