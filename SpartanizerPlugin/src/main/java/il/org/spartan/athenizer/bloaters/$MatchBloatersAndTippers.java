package il.org.spartan.athenizer.bloaters;

import java.util.*;
import java.util.stream.*;

import il.org.spartan.athenizer.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;

/** Bloaters and their matching tippers
 * @author Yuval Simon
 * @since 2017-05-14 */
public class $MatchBloatersAndTippers {
  @SuppressWarnings("rawtypes")
  static final Map<Class<? extends Tipper>, Class<? extends Tipper>> matching = new HashMap<>();
  
  /** Complete match */
  static {
    matching.put(AssignmentAndAssignmentBloater.class, AssignmentAndAssignmentOfSameValue.class);
    matching.put(AssignmentOperatorBloater.class, AssignmentToFromInfixIncludingTo.class);
    matching.put(ForBlockBloater.class, BlockSingletonEliminate.class);
    matching.put(ForEachBlockBloater.class, BlockSingletonEliminate.class);
    matching.put(IfElseBlockBloater.class, BlockSingletonEliminate.class);
    matching.put(MultiplicationToCast.class, CastToLong2Multiply1L.class);
    matching.put(OutlineArrayAccess.class, ArrayAccessAndIncrement.class);
    matching.put(ReturnTernaryExpander.class, IfReturnFooElseReturnBar.class);
    matching.put(TernaryPushupStrings.class, TernaryPushdownStrings.class);
    matching.put(WhileBlockBloater.class, BlockSingletonEliminate.class);
    matching.put(ThrowTernaryBloater.class, IfThrowFooElseThrowBar.class);
    matching.put(MultiTypeCatchClause.class, TryMergeCatchers.class);
    matching.put(VariableDeclarationStatementSplit.class, TwoDeclarationsIntoOne.class);
    matching.put(MethodInvocationTernaryBloater.class, IfExpressionStatementElseSimilarExpressionStatement.class);
    matching.put(ExtractExpressionFromReturn.class, LocalInitializedReturnExpression.class);
    matching.put(DeclarationWithInitializerBloater.class, LocalInitializedUpdateAssignment.class);
    matching.put(CasesSplit.class, MergeSwitchBranches.class);
    matching.put(ToStringExpander.class, MethodInvocationToStringToEmptyStringAddition.class);
  }
  
  /** Close match */
  static {
    matching.put(ParenthesesBloater.class, ParenthesizedRemoveExtraParenthesis.class);
    matching.put(AssignmentTernaryBloater.class, IfAssignToFooElseAssignToFoo.class);
    matching.put(ClassInstanceIntoVariable.class, LocalInitializedUnusedRemove.class);
    matching.put(TernaryPushup.class, TernaryPushdown.class);
    matching.put(OutlineTernaryMethodInvocation.class, TernaryPushdown.class); 
    matching.put(StatementExtractParameters.class, LocalInitializedStatementTerminatingScope.class);
  }
  
  /** Expanders that should be checked */
  static {
    matching.put(PostFixToInfixExpander.class, PlusAssignToPostfix.class);
    matching.put(PrefixToInfix.class, PlusAssignToPostfix.class);
  }
  
  /** Non match */
  static {
    matching.put(BooleanExpressionBloater.class, null);
    matching.put(IfElseToSwitch.class, null);
    matching.put(LongIfBloater.class, null);
    matching.put(MethodDeclarationNameExpander.class, null);
    matching.put(SwitchMissingDefaultAdd.class, null);
    matching.put(VariableDeclarationStatementExpand.class, null);
  }
  
  /** Prints non matched tippers (only of tippers in Configurations(toolbox)) */
  public static void printNonMatchedTippers() {
    List< Class<?> > $ = new ArrayList<>();
    $.addAll(Configurations.all.get().getAllTippers().stream().map(Object::getClass).collect(Collectors.toList()));
    $.removeAll(matching.values());
    $.forEach(x->System.out.println(x.getName()));
  }
  
  /** Prints bloaters that are not in "matching" map (only of bloaters in InflaterProvider) */
  public static void printBloatersNotInMatching() {
    List< Class<?> > $ = new ArrayList<>();
    $.addAll(InflaterProvider.freshCopyOfAllExpanders().getAllTippers().stream().map(Object::getClass).collect(Collectors.toList()));
    $.removeAll(matching.keySet());
    $.forEach(x->System.out.println(x.getName()));
  }
}
