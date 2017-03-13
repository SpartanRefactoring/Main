# Spartanizer? Huh?
The Spartanizer is:
- An eclipse plugin
- Offers in the problems view tips for simplifying your code 
- Make your code laconic: say much in few words.

The Spartanizer help you make a sequence small, nano-refactorings of your code, to make it shorter, and more conforming to a collection of nano-patterns. The resulting code is not just shorter, it is more regular. The spartanization process tries to remove as many distracting details and variations from the code, stripping it to the bare bone.

This includes removal of piles of syntactic baggage, which is code that does not nothing, except for being there:  curly brackets around one statement, initializations which reiterate the default, modifiers which do not change the semantics, implicit call to `super()` which every constructor has, fancy, but uselessly long variable names, variables which never vary and contain temporaries and  many more. Overall, the Spartanizer has over 100 tippers.

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
|1 | Inlining | InliningPrefix | 2 | ArrayAccess | ArrayAccess | ArrayAccess |
|2 | Canonicalization | AssignmentAndAssignment | 7 | Assignment | Assignment | Assignment |
|3 | Canonicalization | AssignmentAndReturn | 7 | Assignment | Assignment | Assignment |
|4 | SyntacticBaggage | AssignmentToFromInfixIncludingTo | 7 | Assignment | Assignment | Assignment |
|5 | SyntacticBaggage | AssignmentToPrefixIncrement | 7 | Assignment | Assignment | Assignment |
|6 | InVain | BlockSimplify | 8 | Block | Block | Block |
|7 | SyntacticBaggage | BlockSingleton | 8 | Block | Block | Block |
|8 | Canonicalization | BlockInlineStatementIntoNext | 8 | Block | Block | Block |
|9 | InVain | CastToDouble2Multiply1 | 11 | CastExpression | CastExpression | CastExpression |
|10 | InVain | CastToLong2Multiply1L | 11 | CastExpression | CastExpression | CastExpression |
|11 | Centification | CatchClauseRenameParameterToCent | 12 | CatchClause | CatchClause | CatchClause |
|12 | SyntacticBaggage | ClassInstanceCreationValueTypes | 14 | ClassInstanceCreation | ClassInstanceCreation | ClassInstanceCreation |
|13 | InVain | TernaryBooleanLiteral | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|14 | Canonicalization | TernaryCollapse | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|15 | InVain | TernaryEliminate | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|16 | Idiomatic | TernaryShortestFirst | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|17 | Canonicalization | TernaryPushdown | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|18 | Ternarization | TernaryPushdownStrings | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|19 | Canonicalization | SameEvaluationConditional | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|20 | Canonicalization | TernaryBranchesAreOppositeBooleans | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|21 | Canonicalization | SameEvaluationConditional | 16 | ConditionalExpression | ConditionalExpression | ConditionalExpression |
|22 | Idiomatic | ExpressionStatementAssertTrueFalse | 21 | ExpressionStatement | ExpressionStatement | ExpressionStatement |
|23 | Idiomatic | ofField | 23 | FieldDeclaration | ??? | ASTNode |
|24 | Idiomatic | ofField | 23 | FieldDeclaration | ??? | ASTNode |
|25 | Canonicalization | BlockBreakToReturnInfiniteFor | 24 | ForStatement | ForStatement | ForStatement |
|26 | Canonicalization | ReturnToBreakFiniteFor | 24 | ForStatement | ForStatement | ForStatement |
|27 | Canonicalization | RemoveRedundentFor | 24 | ForStatement | ForStatement | ForStatement |
|28 | Canonicalization | ForToForUpdaters | 24 | ForStatement | ForStatement | ForStatement |
|29 | Canonicalization | ForTrueConditionRemove | 24 | ForStatement | ForStatement | ForStatement |
|30 | Canonicalization | ForAndReturnToFor | 24 | ForStatement | ForStatement | ForStatement |
|31 | SyntacticBaggage | ForRedundantContinue | 24 | ForStatement | ForStatement | ForStatement |
|32 | InVain | IfTrueOrFalse | 25 | IfStatement | IfStatement | IfStatement |
|33 | Canonicalization | RemoveRedundantIf | 25 | IfStatement | IfStatement | IfStatement |
|34 | EarlyReturn | IfLastInMethodThenEndingWithEmptyReturn | 25 | IfStatement | IfStatement | IfStatement |
|35 | EarlyReturn | IfLastInMethodElseEndingWithEmptyReturn | 25 | IfStatement | IfStatement | IfStatement |
|36 | EarlyReturn | IfLastInMethod | 25 | IfStatement | IfStatement | IfStatement |
|37 | Ternarization | IfReturnFooElseReturnBar | 25 | IfStatement | IfStatement | IfStatement |
|38 | Ternarization | IfReturnNoElseReturn | 25 | IfStatement | IfStatement | IfStatement |
|39 | Ternarization | IfAssignToFooElseAssignToFoo | 25 | IfStatement | IfStatement | IfStatement |
|40 | Canonicalization | IfThenFooBarElseFooBaz | 25 | IfStatement | IfStatement | IfStatement |
|41 | Ternarization | IfBarFooElseBazFoo | 25 | IfStatement | IfStatement | IfStatement |
|42 | Ternarization | IfThrowFooElseThrowBar | 25 | IfStatement | IfStatement | IfStatement |
|43 | Ternarization | IfThrowNoElseThrow | 25 | IfStatement | IfStatement | IfStatement |
|44 | Ternarization | IfExpressionStatementElseSimilarExpressionStatement | 25 | IfStatement | IfStatement | IfStatement |
|45 | Canonicalization | IfThenOrElseIsCommandsFollowedBySequencer | 25 | IfStatement | IfStatement | IfStatement |
|46 | Ternarization | IfFooSequencerIfFooSameSequencer | 25 | IfStatement | IfStatement | IfStatement |
|47 | EarlyReturn | IfCommandsSequencerNoElseSingletonSequencer | 25 | IfStatement | IfStatement | IfStatement |
|48 | EarlyReturn | IfPenultimateInMethodFollowedBySingleStatement | 25 | IfStatement | IfStatement | IfStatement |
|49 | Canonicalization | IfThenIfThenNoElseNoElse | 25 | IfStatement | IfStatement | IfStatement |
|50 | InVain | IfEmptyThenEmptyElse | 25 | IfStatement | IfStatement | IfStatement |
|51 | InVain | IfDegenerateElse | 25 | IfStatement | IfStatement | IfStatement |
|52 | Canonicalization | IfEmptyThen | 25 | IfStatement | IfStatement | IfStatement |
|53 | Idiomatic | IfShortestFirst | 25 | IfStatement | IfStatement | IfStatement |
|54 | SyntacticBaggage | InfixPlusToMinus | 27 | InfixExpression | InfixExpression | InfixExpression |
|55 | Canonicalization | LessEqualsToLess | 27 | InfixExpression | InfixExpression | InfixExpression |
|56 | Canonicalization | LessToLessEquals | 27 | InfixExpression | InfixExpression | InfixExpression |
|57 | InVain | InfixMultiplicationEvaluate | 27 | InfixExpression | ??? | ASTNode |
|58 | InVain | InfixDivisionEvaluate | 27 | InfixExpression | ??? | ASTNode |
|59 | InVain | InfixRemainderEvaluate | 27 | InfixExpression | ??? | ASTNode |
|60 | Idiomatic | InfixComparisonSizeToZero | 27 | InfixExpression | InfixExpression | InfixExpression |
|61 | InVain | InfixSubtractionZero | 27 | InfixExpression | InfixExpression | InfixExpression |
|62 | Idiomatic | InfixAdditionSubtractionExpand | 27 | InfixExpression | InfixExpression | InfixExpression |
|63 | InVain | InfixEmptyStringAdditionToString | 27 | InfixExpression | InfixExpression | InfixExpression |
|64 | Canonicalization | InfixConcatenationEmptyStringLeft | 27 | InfixExpression | InfixExpression | InfixExpression |
|65 | Idiomatic | InfixFactorNegatives | 27 | InfixExpression | InfixExpression | InfixExpression |
|66 | InVain | InfixAdditionEvaluate | 27 | InfixExpression | ??? | ASTNode |
|67 | InVain | InfixSubtractionEvaluate | 27 | InfixExpression | ??? | ASTNode |
|68 | InVain | InfixTermsZero | 27 | InfixExpression | InfixExpression | InfixExpression |
|69 | SyntacticBaggage | InfixPlusRemoveParenthesis | 27 | InfixExpression | InfixExpression | InfixExpression |
|70 | Idiomatic | InfixAdditionSort | 27 | InfixExpression | ??? | ASTNode |
|71 | Canonicalization | InfixComparisonBooleanLiteral | 27 | InfixExpression | InfixExpression | InfixExpression |
|72 | InVain | InfixConditionalAndTrue | 27 | InfixExpression | InfixExpression | InfixExpression |
|73 | InVain | InfixConditionalOrFalse | 27 | InfixExpression | InfixExpression | InfixExpression |
|74 | Idiomatic | InfixComparisonSpecific | 27 | InfixExpression | InfixExpression | InfixExpression |
|75 | InVain | InfixMultiplicationByOne | 27 | InfixExpression | InfixExpression | InfixExpression |
|76 | InVain | InfixMultiplicationByZero | 27 | InfixExpression | InfixExpression | InfixExpression |
|77 | Idiomatic | InfixMultiplicationSort | 27 | InfixExpression | ??? | ASTNode |
|78 | Idiomatic | InfixPseudoAdditionSort | 27 | InfixExpression | ??? | ASTNode |
|79 | Idiomatic | InfixSubtractionSort | 27 | InfixExpression | ??? | ASTNode |
|80 | Idiomatic | InfixDivisonSortRest | 27 | InfixExpression | ??? | ASTNode |
|81 | Canonicalization | InfixConditionalCommon | 27 | InfixExpression | InfixExpression | InfixExpression |
|82 | Canonicalization | SimplifyComparisionOfAdditions | 27 | InfixExpression | InfixExpression | InfixExpression |
|83 | Canonicalization | SimplifyComparisionOfSubtractions | 27 | InfixExpression | InfixExpression | InfixExpression |
|84 | Idiomatic | ofInitializer | 28 | Initializer | ??? | ASTNode |
|85 | Idiomatic | ofInitializer | 28 | Initializer | ??? | ASTNode |
|86 | SyntacticBaggage | JavadocEmpty | 29 | Javadoc | Javadoc | Javadoc |
|87 | Idiomatic | ofMethod | 31 | MethodDeclaration | ??? | ASTNode |
|88 | Dollarization | MethodDeclarationRenameReturnToDollar | 31 | MethodDeclaration | MethodDeclaration | MethodDeclaration |
|89 | Idiomatic | ofMethod | 31 | MethodDeclaration | ??? | ASTNode |
|90 | Centification | MethodDeclarationRenameSingleParameterToCent | 31 | MethodDeclaration | MethodDeclaration | MethodDeclaration |
|91 | Canonicalization | RedundentReturnStatementInVoidTypeMethod | 31 | MethodDeclaration | MethodDeclaration | MethodDeclaration |
|92 | Idiomatic | MethodInvocationEqualsWithLiteralString | 32 | MethodInvocation | MethodInvocation | MethodInvocation |
|93 | Canonicalization | MethodInvocationValueOfBooleanConstant | 32 | MethodInvocation | MethodInvocation | MethodInvocation |
|94 | Canonicalization | MethodInvocationToStringToEmptyStringAddition | 32 | MethodInvocation | MethodInvocation | MethodInvocation |
|95 | Idiomatic | PostfixToPrefix | 37 | PostfixExpression | PostfixExpression | PostfixExpression |
|96 | Canonicalization | PrefixIncrementDecrementReturn | 38 | PrefixExpression | PrefixExpression | PrefixExpression |
|97 | Idiomatic | PrefixNotPushdown | 38 | PrefixExpression | PrefixExpression | PrefixExpression |
|98 | InVain | PrefixPlusRemove | 38 | PrefixExpression | PrefixExpression | PrefixExpression |
|99 | Abbreviation | SingleVariableDeclarationAbbreviation | 44 | SingleVariableDeclaration | SingleVariableDeclaration | SingleVariableDeclaration |
|100 | Annonimaization | SingelVariableDeclarationUnderscoreDoubled | 44 | SingleVariableDeclaration | SingleVariableDeclaration | SingleVariableDeclaration |
|101 | Annonimaization | VariableDeclarationRenameUnderscoreToDoubleUnderscore | 44 | SingleVariableDeclaration | ??? | VariableDeclaration |
|102 | Centification | SingleVariableDeclarationEnhancedForRenameParameterToCent | 44 | SingleVariableDeclaration | SingleVariableDeclaration | SingleVariableDeclaration |
|103 | SyntacticBaggage | SuperConstructorInvocationRemover | 46 | SuperConstructorInvocation | SuperConstructorInvocation | SuperConstructorInvocation |
|104 | Canonicalization | SwitchEmpty | 50 | SwitchStatement | SwitchStatement | SwitchStatement |
|105 | Canonicalization | RemoveRedundantSwitchCases | 50 | SwitchStatement | SwitchStatement | SwitchStatement |
|106 | Canonicalization | RemoveRedundantSwitchBranch | 50 | SwitchStatement | SwitchStatement | SwitchStatement |
|107 | InVain | ThrowNotLastInBlock | 53 | ThrowStatement | ThrowStatement | ThrowStatement |
|108 | Canonicalization | EliminateEmptyFinally | 54 | TryStatement | TryStatement | TryStatement |
|109 | Canonicalization | MergeCatches | 54 | TryStatement | TryStatement | TryStatement |
|110 | Canonicalization | EliminateEmptyTryBlock | 54 | TryStatement | TryStatement | TryStatement |
|111 | Idiomatic | ofType | 55 | TypeDeclaration | ??? | ASTNode |
|112 | Idiomatic | ofType | 55 | TypeDeclaration | ??? | ASTNode |
|113 | Centification | ForRenameInitializerToCent | 58 | VariableDeclarationExpression | VariableDeclarationExpression | VariableDeclarationExpression |
|114 | SyntacticBaggage | DeclarationRedundantInitializer | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|115 | Canonicalization | DeclarationAssignment | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|116 | Canonicalization | DeclarationInitialiazelUpdateAssignment | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|117 | Canonicalization | DeclarationInitializerIfAssignment | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|118 | Canonicalization | DeclarationInitializerIfUpdateAssignment | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|119 | Inlining | DeclarationInitializerReturnVariable | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|120 | Inlining | DeclarationInitializerReturnExpression | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|121 | Canonicalization | DeclarationInitializerReturnAssignment | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|122 | Inlining | DeclarationInitializerReturn | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|123 | Inlining | DeclarationInitializerStatementTerminatingScope | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|124 | Canonicalization | DeclarationInitialiazerAssignment | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|125 | Canonicalization | DeclarationInlineIntoNext | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|126 | Annonimaization | VariableDeclarationRenameUnderscoreToDoubleUnderscore | 59 | VariableDeclarationFragment | ??? | VariableDeclaration |
|127 | Canonicalization | ForToForInitializers | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|128 | Canonicalization | WhileToForInitializers | 59 | VariableDeclarationFragment | VariableDeclarationFragment | VariableDeclarationFragment |
|129 | Canonicalization | BlockBreakToReturnInfiniteWhile | 61 | WhileStatement | WhileStatement | WhileStatement |
|130 | Canonicalization | ReturnToBreakFiniteWhile | 61 | WhileStatement | WhileStatement | WhileStatement |
|131 | Canonicalization | RemoveRedundantWhile | 61 | WhileStatement | WhileStatement | WhileStatement |
|132 | Canonicalization | WhileToForUpdaters | 61 | WhileStatement | WhileStatement | WhileStatement |
|133 | Centification | EnhancedForParameterRenameToCent | 70 | EnhancedForStatement | EnhancedForStatement | EnhancedForStatement |
|134 | SyntacticBaggage | EnhancedForRedundantConinue | 70 | EnhancedForStatement | EnhancedForStatement | EnhancedForStatement |
|135 | Idiomatic | ofEnum | 71 | EnumDeclaration | ??? | ASTNode |
|136 | Idiomatic | ofEnum | 71 | EnumDeclaration | ??? | ASTNode |
|137 | Idiomatic | ofEnumConstant | 72 | EnumConstantDeclaration | ??? | ASTNode |
|138 | Idiomatic | ofEnumConstant | 72 | EnumConstantDeclaration | ??? | ASTNode |
|139 | Idiomatic | ofEnumConstant | 72 | EnumConstantDeclaration | ??? | ASTNode |
|140 | SyntacticBaggage | AnnotationDiscardValueName | 77 | NormalAnnotation | NormalAnnotation | NormalAnnotation |
|141 | SyntacticBaggage | AnnotationRemoveEmptyParentheses | 77 | NormalAnnotation | NormalAnnotation | NormalAnnotation |
|142 | Idiomatic | ofAnnotation | 81 | AnnotationTypeDeclaration | ??? | ASTNode |
|143 | Idiomatic | ofAnnotation | 81 | AnnotationTypeDeclaration | ??? | ASTNode |
|144 | Idiomatic | ofAnnotationTypeMember | 82 | AnnotationTypeMemberDeclaration | ??? | ASTNode |
|145 | Idiomatic | ofAnnotationTypeMember | 82 | AnnotationTypeMemberDeclaration | ??? | ASTNode |
|146 | SyntacticBaggage | RedundantModifier | 83 | Modifier | Modifier | Modifier |
|147 | ScopeReduction | LambdaExpressionRemoveRedundantCurlyBraces | 86 | LambdaExpression | LambdaExpression | LambdaExpression |
|N |  |  | 147.0 |  |  |  |
|N/A |  |  | 0.0 |  |  |  |
|mean |  |  | 36.45578231292517 |  |  |  |
|σ |  |  | 19.970465026322703 |  |  |  |
|median |  |  | 27.0 |  |  |  |
|M.A.D |  |  | 10.0 |  |  |  |
|min |  |  | 2.0 |  |  |  |
|max |  |  | 86.0 |  |  |  |
|range |  |  | 84.0 |  |  |  |



