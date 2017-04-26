# Spartanizer? Huh?
The Spartanizer is:
- An eclipse plugin
- Offers in the problems view tips for simplifying your code 
- Make your code laconic: say much in few words.

The Spartanizer help you make a sequence small, nano-refactorings of your code, to make it shorter, and more conforming to a language of nano-patterns. The resulting code is not just shorter, it is more regular. The spartanization process tries to remove as many distracting details and variations from the code, stripping it to its bare bone.

This includes removal of piles of syntactic baggage, which is code that does not nothing, except for being there:  curly brackets around one statement, initializations which reiterate the default, modifiers which do not change the semantics, implicit call to `super()` which every constructor has, fancy, but uselessly long variable names, variables which never vary and contain temporaries and  many more. Overall, the Spartanizer has over 100 tippers.

# Contents

[List of all the tippers](https://github.com/SpartanRefactoring/Spartanizer/wiki/List-of-Tippers "List of the Tippers")

![spartanization](https://cloud.githubusercontent.com/assets/15183108/19212649/59d65e3e-8d5e-11e6-9940-ac7a070be7d6.gif)

# Video Demo

Click on the picture below to watch a video demonstration on YouTube.

[![IMAGE ALT TEXT](https://img.youtube.com/vi/33npJI-MZ1I/0.jpg)](https://www.youtube.com/watch_popup?v=49M55azHHM0 "Spartanization Demo")

# Installation

- Installation button (drag to your eclipse workspace)
<a href="http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=2617709" class="drag" title="Drag to your running Eclipse workspace to install Spartan Refactoring"><img class="img-responsive" src="https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.png" alt="Drag to your running Eclipse workspace to install Spartan Refactoring" /></a>
- This plugin's <a href="https://www.google.co.il/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&ved=0ahUKEwj7p7iPwL7PAhUrI8AKHW87AVsQFggaMAA&url=https%3A%2F%2Fmarketplace.eclipse.org%2Fcontent%2Fspartan-refactoring-0&usg=AFQjCNFaOBCLW8-CKYYnfLFCjakdWM1qjA&sig2=Z1zbbkq96-iECkhmMf5Qcw&bvm=bv.134495766,d.ZGg">page on market place</href>

# Development Status

 [![Build Status](https://travis-ci.org/SpartanRefactoring/Spartanizer.svg?branch=master)](https://travis-ci.org/SpartanRefactoring/Spartanizer)

[![codecov](https://codecov.io/gh/TechnionYP5777/SmartCity-ParkingManagement/branch/master/graph/badge.svg)](https://codecov.io/gh/SpartanRefactoring/Spartanizer)


![spartan resized](https://cloud.githubusercontent.com/assets/15859817/23854098/7f02ba4e-07f8-11e7-8bd9-8ebe2ccbe9e8.png)


# What is this?
The Spartanizer is an  Eclipse plugin that automatically applies the principles
of *[Spartan Programming]* to your Java code. It applies many different tippers,
   which are little rules that provide suggestions on how to shorten and
   simplify your code, e.g, by using fewer variables, factoring out common
   structures, more efficient use of control flow, etc. 

# Background
This project was conceived as an academic project in the [Technion - Israel
Institute of Technology], and was later developed for several years by
different students and members of the Computer Science faculty.

The refactorings made by this plug-in are based on the concept of Spartan
Programming, which suggests guidelines for writing short, clean code. There's a
lot of reading material on the subject in the [project wiki].

## Stable version
### Installing from a jar file
1. Download the jar file from the latest [Release].
2. Close Eclipse.
3. Put the jar file in eclipse/dropin folder.
4. Start Eclipse. 

## Compiling from source
#### Using Maven and Git in command line(recommended)
By assuming that the current directory is at relative path "?", Clone the repository by:

```
git clone https://github.com/SpartanRefactoring/Spartanizer.git
```
After cloning the repository, go into the ?/Spartanizer/SpartanRefactoring
directory and execute:

```
mvn package
```
The packaged plug-in (.jar file) will be created in the
`?/Spartanizer/SpartanRefactoring/target` directory.
Copy the .jar file into your eclipse directory, Eclipse/dropins or plugins, and
run Eclipse.

#### From inside Eclipse

1. Open Eclipse and go to "*Install New Software...*". From the list of install
   sites, pick *The Eclipse Project Updates* and make sure you've installed all
   the items from the categories **Eclipse Platform**, **Eclipse Platform SDK**
   and **Eclipse Plugin Development Tools**. Failure to install one of these
   will result in import errors.

2. Create a *run configuration* by running the project (Ctrl+F11), then elect
to run it as an Eclipse Application.
    * If a new instance of Eclipse doesn't launch, open the run configurations
    for the project and make sure that in the *Program to Run* box, "Run a
    product" is selected and the box next to it says
    "org.eclipse.platform.ide".

3. Go to *File -> Export...*. Under the *Plug-in Development category*, choose
   the *Deployable plug-ins and fragments*, and continue until the plug-in has
   been built successfully.

A *tipper*, in the context of this project, is a small object responsible for
converting one form of code into another, under two major assumptions:

1. The latter is shorter and/or more comprehensible than the former.

2. Both forms are semantically equivalent, meaning that both versions do
   exactly the same thing (yet the latter may perform more efficiently).

Consider this basic programmers' mistake:
```java
if(myString.length() < 5) {
    return true;
} else {
    return false;
}
```
Which can be shortened to:
```java
if(myString.length() < 5)
    return true;
return false;
```
Or even:
```java
return myString.length() < 5;
```
Spartan Refactoring first detects this problem by:
1. Finding a matching tipper,
2. Notifying the user that a spartanization can be made
3. Finally while preserving the programmer's intention, revises the `if` block
   all the way to the last example.

#### Method of operation
The plugin works by analyzing the [abstract syntax tree] (AST) generated by
Eclipse for each of the compilation units in the current Java project. After
the AST has been generated, and the user had asked to apply a set of tipper to
the code, it is traversed using an [ASTVisitor], and the tipper are applied one
by one to the tree.

After the traversal is complete, the transformations made to the AST are
written back to the source code.

## License
The project is available under the **[MIT License]**

[Release]: https://github.com/SpartanRefactoring/Spartanizer/releases/tag/2.6.3
[Spartan Programming]: http://blog.codinghorror.com/spartan-programming/
[project wiki]: https://github.com/SpartanRefactoring/spartan-refactoring/wiki/Spartan-Programming
[Technion - Israel Institute of Technology]: http://www.technion.ac.il/en/
[abstract syntax tree]: https://en.wikipedia.org/wiki/Abstract_syntax_tree
[ASTVisitor]: http://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fjdt%2Fcore%2Fdom%2FASTVisitor.html
[MIT License]: https://opensource.org/licenses/MIT

# List of all tippers

|Nº | Category | Tipper | Node Type Number | Node Class | Actual class | Abstract class |
| --- |--- |--- |--- |--- |--- |--- |
|1 | Inlining | ArrayAccessAndIncrement | 2 | ArrayAccess | ArrayAccess | ArrayAccess |
|2 | Canonicalization | AssignmentAndAssignmentOfSameValue | 7 | Assignment | Assignment | Assignment |
|3 | Canonicalization | AssignmentAndAssignmentToSame | 7 | Assignment | Assignment | Assignment |
|4 | Canonicalization | AssignmentAndReturn | 7 | Assignment | Assignment | Assignment |
|5 | SyntacticBaggage | AssignmentToFromInfixIncludingTo | 7 | Assignment | Assignment | Assignment |
|6 | SyntacticBaggage | AssignmentToPrefixIncrement | 7 | Assignment | Assignment | Assignment |
|7 | Canonicalization | AssignmentUpdateAndSameUpdate | 7 | Assignment | Assignment | Assignment |
|8 | Canonicalization | AssignmentAndAssignmentOfSameVariable | 7 | Assignment | Assignment | Assignment |
|9 | SyntacticBaggage | BlockSimplify | 8 | Block | Block | Block |
|10 | SyntacticBaggage | BlockSingleton | 8 | Block | Block | Block |
|11 | Deadcode | SequencerNotLastInBlock | 10 | BreakStatement | ??? | Statement |
|12 | Arithmetic | CastToDouble2Multiply1 | 11 | CastExpression | CastExpression | CastExpression |
|13 | NOOP | CastToLong2Multiply1L | 11 | CastExpression | CastExpression | CastExpression |
|14 | Centification | CatchClauseRenameParameterToCent | 12 | CatchClause | CatchClause | CatchClause |
|15 | Idiomatic | ClassInstanceCreationValueTypes | 14 | ClassInstanceCreation | ClassInstanceCreation | ClassInstanceCreation |
|16 | NOOP | TernaryBooleanLiteral | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|17 | Canonicalization | TernaryCollapse | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|18 | Canonicalization | TernaryEliminate | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|19 | Idiomatic | TernaryShortestFirst | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|20 | Canonicalization | TernaryPushdown | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|21 | Canonicalization | TernaryPushdownStrings | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|22 | Thrashing | SameEvaluationConditional | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|23 | Canonicalization | TernaryBranchesAreOppositeBooleans | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|24 | Thrashing | SameEvaluationConditional | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|25 | Deadcode | SequencerNotLastInBlock | 18 | ContinueStatement | ??? | Statement |
|26 | SyntacticBaggage | DoWhileEmptyBlockToEmptyStatement | 19 | DoStatement | DoStatement | DoStatement |
|27 | Idiomatic | ExpressionStatementAssertTrueFalse | 21 | ExpressionStatement | ExpressionStatement | ExpressionStatement |
|28 | Idiomatic | ExpressionStatementThatIsBooleanLiteral | 21 | ExpressionStatement | ExpressionStatement | ExpressionStatement |
|29 | Idiomatic | BodyDeclarationModifiersSort | 23 | FieldDeclaration | ??? | BodyDeclaration |
|30 | Idiomatic | AnnotationSort | 23 | FieldDeclaration | ??? | BodyDeclaration |
|31 | Thrashing | ForDeadRemove | 24 | ForStatement | ForStatement | ForStatement |
|32 | Shortcut | ContinueCoditinalInForEliminate | 24 | ForStatement | ForStatement | ForStatement |
|33 | Shortcut | BlockBreakToReturnInfiniteFor | 24 | ForStatement | ForStatement | ForStatement |
|34 | Canonicalization | ReturnToBreakFiniteFor | 24 | ForStatement | ForStatement | ForStatement |
|35 | Canonicalization | ForToForUpdaters | 24 | ForStatement | ForStatement | ForStatement |
|36 | SyntacticBaggage | ForTrueConditionRemove | 24 | ForStatement | ForStatement | ForStatement |
|37 | Canonicalization | ForAndReturnToFor | 24 | ForStatement | ForStatement | ForStatement |
|38 | Shortcut | ForRedundantContinue | 24 | ForStatement | ForStatement | ForStatement |
|39 | SyntacticBaggage | ForEmptyBlockToEmptyStatement | 24 | ForStatement | ForStatement | ForStatement |
|40 | Deadcode | IfTrueOrFalse | 25 | IfStatement | IfStatement | IfStatement |
|41 | Thrashing | IfDeadRemove | 25 | IfStatement | IfStatement | IfStatement |
|42 | EarlyReturn | IfLastInMethodThenEndingWithEmptyReturn | 25 | IfStatement | IfStatement | IfStatement |
|43 | EarlyReturn | IfLastInMethodElseEndingWithEmptyReturn | 25 | IfStatement | IfStatement | IfStatement |
|44 | EarlyReturn | IfLastInMethod | 25 | IfStatement | IfStatement | IfStatement |
|45 | Canonicalization | IfReturnFooElseReturnBar | 25 | IfStatement | IfStatement | IfStatement |
|46 | Canonicalization | IfReturnNoElseReturn | 25 | IfStatement | IfStatement | IfStatement |
|47 | Canonicalization | IfAssignToFooElseAssignToFoo | 25 | IfStatement | IfStatement | IfStatement |
|48 | Canonicalization | IfThenFooBarElseFooBaz | 25 | IfStatement | IfStatement | IfStatement |
|49 | Canonicalization | IfBarFooElseBazFoo | 25 | IfStatement | IfStatement | IfStatement |
|50 | Canonicalization | IfThrowFooElseThrowBar | 25 | IfStatement | IfStatement | IfStatement |
|51 | Canonicalization | IfThrowNoElseThrow | 25 | IfStatement | IfStatement | IfStatement |
|52 | Canonicalization | IfExpressionStatementElseSimilarExpressionStatement | 25 | IfStatement | IfStatement | IfStatement |
|53 | Canonicalization | IfThenOrElseIsCommandsFollowedBySequencer | 25 | IfStatement | IfStatement | IfStatement |
|54 | Canonicalization | IfFooSequencerIfFooSameSequencer | 25 | IfStatement | IfStatement | IfStatement |
|55 | EarlyReturn | IfCommandsSequencerNoElseSingletonSequencer | 25 | IfStatement | IfStatement | IfStatement |
|56 | EarlyReturn | IfPenultimateInMethodFollowedBySingleStatement | 25 | IfStatement | IfStatement | IfStatement |
|57 | Canonicalization | IfThenIfThenNoElseNoElse | 25 | IfStatement | IfStatement | IfStatement |
|58 | NOOP | IfEmptyThenEmptyElse | 25 | IfStatement | IfStatement | IfStatement |
|59 | SyntacticBaggage | IfDegenerateElse | 25 | IfStatement | IfStatement | IfStatement |
|60 | SyntacticBaggage | IfEmptyThen | 25 | IfStatement | IfStatement | IfStatement |
|61 | Idiomatic | IfShortestFirst | 25 | IfStatement | IfStatement | IfStatement |
|62 | SyntacticBaggage | InfixPlusToMinus | 27 | InfixExpression | InfixExpression | InfixExpression |
|63 | Canonicalization | InfixLessEqualsToLess | 27 | InfixExpression | InfixExpression | InfixExpression |
|64 | Canonicalization | InfixLessToLessEquals | 27 | InfixExpression | InfixExpression | InfixExpression |
|65 | Arithmetic | InfixMultiplicationEvaluate | 27 | InfixExpression | ??? | ASTNode |
|66 | Arithmetic | InfixDivisionEvaluate | 27 | InfixExpression | ??? | ASTNode |
|67 | Arithmetic | InfixRemainderEvaluate | 27 | InfixExpression | ??? | ASTNode |
|68 | Idiomatic | InfixComparisonSizeToZero | 27 | InfixExpression | InfixExpression | InfixExpression |
|69 | NOOP | InfixSubtractionZero | 27 | InfixExpression | InfixExpression | InfixExpression |
|70 | Arithmetic | InfixAdditionSubtractionExpand | 27 | InfixExpression | InfixExpression | InfixExpression |
|71 | NOOP | InfixPlusEmptyString | 27 | InfixExpression | InfixExpression | InfixExpression |
|72 | Idiomatic | InfixConcatenationEmptyStringLeft | 27 | InfixExpression | InfixExpression | InfixExpression |
|73 | Idiomatic | InfixFactorNegatives | 27 | InfixExpression | InfixExpression | InfixExpression |
|74 | Arithmetic | InfixAdditionEvaluate | 27 | InfixExpression | ??? | ASTNode |
|75 | Arithmetic | InfixSubtractionEvaluate | 27 | InfixExpression | ??? | ASTNode |
|76 | NOOP | InfixTermsZero | 27 | InfixExpression | InfixExpression | InfixExpression |
|77 | SyntacticBaggage | InfixPlusRemoveParenthesis | 27 | InfixExpression | InfixExpression | InfixExpression |
|78 | Idiomatic | InfixAdditionSort | 27 | InfixExpression | ??? | ASTNode |
|79 | NOOP | InfixComparisonBooleanLiteral | 27 | InfixExpression | InfixExpression | InfixExpression |
|80 | NOOP | InfixConditionalAndTrue | 27 | InfixExpression | InfixExpression | InfixExpression |
|81 | NOOP | InfixConditionalOrFalse | 27 | InfixExpression | InfixExpression | InfixExpression |
|82 | Idiomatic | InfixComparisonSpecific | 27 | InfixExpression | InfixExpression | InfixExpression |
|83 | NOOP | InfixMultiplicationByOne | 27 | InfixExpression | InfixExpression | InfixExpression |
|84 | NOOP | InfixMultiplicationByZero | 27 | InfixExpression | InfixExpression | InfixExpression |
|85 | Idiomatic | InfixMultiplicationSort | 27 | InfixExpression | ??? | ASTNode |
|86 | Idiomatic | InfixPseudoAdditionSort | 27 | InfixExpression | ??? | ASTNode |
|87 | Idiomatic | InfixSubtractionSort | 27 | InfixExpression | ??? | ASTNode |
|88 | Idiomatic | InfixDivisonSortRest | 27 | InfixExpression | ??? | ASTNode |
|89 | Canonicalization | InfixConditionalCommon | 27 | InfixExpression | InfixExpression | InfixExpression |
|90 | Idiomatic | InfixIndexOfToStringContains | 27 | InfixExpression | InfixExpression | InfixExpression |
|91 | Idiomatic | InfixSimplifyComparisionOfAdditions | 27 | InfixExpression | InfixExpression | InfixExpression |
|92 | Idiomatic | InfixSimplifyComparisionOfSubtractions | 27 | InfixExpression | InfixExpression | InfixExpression |
|93 | NOOP | InfixStringLiteralsConcatenate | 27 | InfixExpression | InfixExpression | InfixExpression |
|94 | SyntacticBaggage | InitializerEmptyRemove | 28 | Initializer | Initializer | Initializer |
|95 | SyntacticBaggage | JavadocEmptyRemove | 29 | Javadoc | Javadoc | Javadoc |
|96 | Idiomatic | AnnotationSort | 31 | MethodDeclaration | ??? | BodyDeclaration |
|97 | Dollarization | MethodDeclarationRenameReturnToDollar | 31 | MethodDeclaration | MethodDeclaration | MethodDeclaration |
|98 | Idiomatic | BodyDeclarationModifiersSort | 31 | MethodDeclaration | ??? | BodyDeclaration |
|99 | Centification | MethodDeclarationRenameSingleParameterToCent | 31 | MethodDeclaration | MethodDeclaration | MethodDeclaration |
|100 | Idiomatic | MethodDeclarationConstructorMoveToInitializers | 31 | MethodDeclaration | MethodDeclaration | MethodDeclaration |
|101 | Idiomatic | MethodInvocationEqualsWithLiteralString | 32 | MethodInvocation | MethodInvocation | MethodInvocation |
|102 | Idiomatic | MethodInvocationValueOfBooleanConstant | 32 | MethodInvocation | MethodInvocation | MethodInvocation |
|103 | Idiomatic | MethodInvocationToStringToEmptyStringAddition | 32 | MethodInvocation | MethodInvocation | MethodInvocation |
|104 | Idiomatic | StringFromStringBuilder | 32 | MethodInvocation | MethodInvocation | MethodInvocation |
|105 | SyntacticBaggage | ParenthesizedRemoveExtraParenthesis | 36 | ParenthesizedExpression | ParenthesizedExpression | ParenthesizedExpression |
|106 | Idiomatic | PostfixToPrefix | 37 | PostfixExpression | PostfixExpression | PostfixExpression |
|107 | Canonicalization | PrefixIncrementDecrementReturn | 38 | PrefixExpression | PrefixExpression | PrefixExpression |
|108 | Idiomatic | PrefixNotPushdown | 38 | PrefixExpression | PrefixExpression | PrefixExpression |
|109 | NOOP | PrefixPlusRemove | 38 | PrefixExpression | PrefixExpression | PrefixExpression |
|110 | SyntacticBaggage | ReturnLastInMethod | 41 | ReturnStatement | ReturnStatement | ReturnStatement |
|111 | Deadcode | SequencerNotLastInBlock | 41 | ReturnStatement | ??? | Statement |
|112 | Abbreviation | SingleVariableDeclarationAbbreviation | 44 | SingleVariableDeclaration | SingleVariableDeclaration | SingleVariableDeclaration |
|113 | Annonimaization | SingelVariableDeclarationUnderscoreDoubled | 44 | SingleVariableDeclaration | SingleVariableDeclaration | SingleVariableDeclaration |
|114 | Annonimaization | FragmentRenameUnderscoreToDoubleUnderscore | 44 | SingleVariableDeclaration | ??? | VariableDeclaration |
|115 | Centification | SingleVariableDeclarationEnhancedForRenameParameterToCent | 44 | SingleVariableDeclaration | SingleVariableDeclaration | SingleVariableDeclaration |
|116 | SyntacticBaggage | SuperConstructorInvocationRemover | 46 | SuperConstructorInvocation | SuperConstructorInvocation | SuperConstructorInvocation |
|117 | SyntacticBaggage | RemoveRedundantSwitchCases | 49 | SwitchCase | SwitchCase | SwitchCase |
|118 | Idiomatic | SwitchCaseLocalSort | 49 | SwitchCase | SwitchCase | SwitchCase |
|119 | SyntacticBaggage | SwitchEmpty | 50 | SwitchStatement | SwitchStatement | SwitchStatement |
|120 | Canonicalization | MergeSwitchBranches | 50 | SwitchStatement | SwitchStatement | SwitchStatement |
|121 | Shortcut | RemoveRedundantSwitchReturn | 50 | SwitchStatement | SwitchStatement | SwitchStatement |
|122 | Shortcut | RemoveRedundantSwitchContinue | 50 | SwitchStatement | SwitchStatement | SwitchStatement |
|123 | Canonicalization | SwitchWithOneCaseToIf | 50 | SwitchStatement | SwitchStatement | SwitchStatement |
|124 | Idiomatic | SwitchBranchSort | 50 | SwitchStatement | SwitchStatement | SwitchStatement |
|125 | Deadcode | SequencerNotLastInBlock | 53 | ThrowStatement | ??? | Statement |
|126 | SyntacticBaggage | TryBodyEmptyLeaveFinallyIfExists | 54 | TryStatement | TryStatement | TryStatement |
|127 | SyntacticBaggage | TryBodyEmptyNoCatchesNoFinallyEliminate | 54 | TryStatement | TryStatement | TryStatement |
|128 | SyntacticBaggage | TryBodyNotEmptyNoCatchesNoFinallyRemove | 54 | TryStatement | TryStatement | TryStatement |
|129 | SyntacticBaggage | TryFinallyEmptyRemove | 54 | TryStatement | TryStatement | TryStatement |
|130 | Canonicalization | MergeCatches | 54 | TryStatement | TryStatement | TryStatement |
|131 | Idiomatic | BodyDeclarationModifiersSort | 55 | TypeDeclaration | ??? | BodyDeclaration |
|132 | Idiomatic | AnnotationSort | 55 | TypeDeclaration | ??? | BodyDeclaration |
|133 | SyntacticBaggage | TypeDeclarationClassExtendsObject | 55 | TypeDeclaration | TypeDeclaration | TypeDeclaration |
|134 | Centification | ForRenameInitializerToCent | 58 | VariableDeclarationExpression | VariableDeclarationExpression | VariableDeclarationExpression |
|135 | SyntacticBaggage | FragmentInitializerDead | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|136 | Canonicalization | FragmentNoInitializerAssignment | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|137 | Canonicalization | FragmentInitialiazerUpdateAssignment | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|138 | Inlining | FragmentInitializerIfAssignment | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|139 | Inlining | FragmentInitializerIfUpdateAssignment | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|140 | Inlining | FragmentInitializerReturnVariable | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|141 | Inlining | FragmentInitializerReturnExpression | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|142 | Inlining | FragmentInitializerReturnAssignment | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|143 | Shortcut | FragmentInitializerReturn | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|144 | Inlining | FragmentInitializerStatementTerminatingScope | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|145 | Inlining | FragmentInitialiazerAssignment | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|146 | Inlining | FragmentInitializerInlineIntoNext | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|147 | Canonicalization | FragmentInitializerWhile | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|148 | Canonicalization | FragmentInitializerToForInitializers | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|149 | Annonimaization | FragmentRenameUnderscoreToDoubleUnderscore | 59 | VariableDeclarationFragment | ??? | VariableDeclaration |
|150 | Deadcode | FragmentNoInitializerRemoveUnused | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|151 | Canonicalization | TwoDeclarationsIntoOne | 60 | VariableDeclarationStatement | VariableDeclarationStatement | VariableDeclarationStatement |
|152 | Shortcut | EliminateConditionalContinueInWhile | 61 | WhileStatement | WhileStatement | WhileStatement |
|153 | Shortcut | BlockBreakToReturnInfiniteWhile | 61 | WhileStatement | WhileStatement | WhileStatement |
|154 | Canonicalization | ReturnToBreakFiniteWhile | 61 | WhileStatement | WhileStatement | WhileStatement |
|155 | Thrashing | WhileDeadRemove | 61 | WhileStatement | WhileStatement | WhileStatement |
|156 | Canonicalization | WhileToForUpdaters | 61 | WhileStatement | WhileStatement | WhileStatement |
|157 | SyntacticBaggage | WhileEmptyBlockToEmptyStatement | 61 | WhileStatement | WhileStatement | WhileStatement |
|158 | Shortcut | EnhancedForRedundantContinue | 70 | EnhancedForStatement | EnhancedForStatement | EnhancedForStatement |
|159 | SyntacticBaggage | EnhancedForEliminateConditionalContinue | 70 | EnhancedForStatement | EnhancedForStatement | EnhancedForStatement |
|160 | Centification | EnhancedForParameterRenameToCent | 70 | EnhancedForStatement | EnhancedForStatement | EnhancedForStatement |
|161 | Idiomatic | BodyDeclarationModifiersSort | 71 | EnumDeclaration | ??? | BodyDeclaration |
|162 | Idiomatic | AnnotationSort | 71 | EnumDeclaration | ??? | BodyDeclaration |
|163 | Idiomatic | BodyDeclarationModifiersSort | 72 | EnumConstantDeclaration | ??? | BodyDeclaration |
|164 | Idiomatic | BodyDeclarationModifiersSort | 72 | EnumConstantDeclaration | ??? | BodyDeclaration |
|165 | Idiomatic | AnnotationSort | 72 | EnumConstantDeclaration | ??? | BodyDeclaration |
|166 | SyntacticBaggage | TypeParameterExtendsObject | 73 | TypeParameter | TypeParameter | TypeParameter |
|167 | SyntacticBaggage | WildcardTypeExtendsObjectTrim | 76 | WildcardType | WildcardType | WildcardType |
|168 | SyntacticBaggage | AnnotationDiscardValueName | 77 | NormalAnnotation | NormalAnnotation | NormalAnnotation |
|169 | SyntacticBaggage | AnnotationRemoveEmptyParentheses | 77 | NormalAnnotation | NormalAnnotation | NormalAnnotation |
|170 | SyntacticBaggage | AnnotationRemoveSingletonArrray | 79 | SingleMemberAnnotation | SingleMemberAnnotation | SingleMemberAnnotation |
|171 | Idiomatic | BodyDeclarationModifiersSort | 81 | AnnotationTypeDeclaration | ??? | BodyDeclaration |
|172 | Idiomatic | AnnotationSort | 81 | AnnotationTypeDeclaration | ??? | BodyDeclaration |
|173 | Idiomatic | BodyDeclarationModifiersSort | 82 | AnnotationTypeMemberDeclaration | ??? | BodyDeclaration |
|174 | Idiomatic | AnnotationSort | 82 | AnnotationTypeMemberDeclaration | ??? | BodyDeclaration |
|175 | SyntacticBaggage | ModifierRedundant | 83 | Modifier | Modifier | Modifier |
|176 | SyntacticBaggage | ModifierFinalAbstractMethodRedundant | 83 | Modifier | Modifier | Modifier |
|177 | SyntacticBaggage | ModifierFinalTryResourceRedundant | 83 | Modifier | Modifier | Modifier |
|178 | SyntacticBaggage | LambdaRemoveRedundantCurlyBraces | 86 | LambdaExpression | LambdaExpression | LambdaExpression |
|179 | Inlining | LambdaRemoveParenthesis | 86 | LambdaExpression | LambdaExpression | LambdaExpression |
|180 | Centification | LambdaRenameSingleParameterToLambda | 86 | LambdaExpression | LambdaExpression | LambdaExpression |
