package il.org.spartan.athenizer;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import fluent.ly.the;
import il.org.spartan.athenizer.SingleFlater.Operation;
import il.org.spartan.athenizer.zoomers.AssignmentAndAssignmentBloater;
import il.org.spartan.athenizer.zoomers.AssignmentOperatorBloater;
import il.org.spartan.athenizer.zoomers.AssignmentTernaryBloater;
import il.org.spartan.athenizer.zoomers.CasesSplit;
import il.org.spartan.athenizer.zoomers.ClassInstanceIntoVariable;
import il.org.spartan.athenizer.zoomers.DeclarationWithInitializerBloater;
import il.org.spartan.athenizer.zoomers.ExtractExpressionFromReturn;
import il.org.spartan.athenizer.zoomers.ForBlockBloater;
import il.org.spartan.athenizer.zoomers.ForEachBlockBloater;
import il.org.spartan.athenizer.zoomers.IfElseBlockBloater;
import il.org.spartan.athenizer.zoomers.LocalInitializedCollection;
import il.org.spartan.athenizer.zoomers.LongIfBloater;
import il.org.spartan.athenizer.zoomers.MethodInvocationTernaryBloater;
import il.org.spartan.athenizer.zoomers.MultiTypeCatchClause;
import il.org.spartan.athenizer.zoomers.MultiplicationToCast;
import il.org.spartan.athenizer.zoomers.OutlineArrayAccess;
import il.org.spartan.athenizer.zoomers.OutlineTernaryMethodInvocation;
import il.org.spartan.athenizer.zoomers.ParenthesesBloater;
import il.org.spartan.athenizer.zoomers.PrefixToInfix;
import il.org.spartan.athenizer.zoomers.ReturnTernaryExpander;
import il.org.spartan.athenizer.zoomers.StatementExtractParameters;
import il.org.spartan.athenizer.zoomers.SwitchMissingDefaultAdd;
import il.org.spartan.athenizer.zoomers.TernaryPushup;
import il.org.spartan.athenizer.zoomers.TernaryPushupStrings;
import il.org.spartan.athenizer.zoomers.ThrowTernaryBloater;
import il.org.spartan.athenizer.zoomers.ToStringExpander;
import il.org.spartan.athenizer.zoomers.VariableDeclarationStatementSplit;
import il.org.spartan.athenizer.zoomers.WhileBlockBloater;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.spartanizer.traversal.Toolbox;

/** holds the new toolbox for the expanders and returns them
 * @author Raviv Rachmiel
 * @since 20-12-16 */
public class InflaterProvider extends OperationsProvider {
  final Toolbox toolbox;
  Function<List<Operation<?>>, List<Operation<?>>> function = λ -> Collections.singletonList(the.firstOf(λ));

  public InflaterProvider() {
    toolbox = InflaterProvider.freshCopyOfAllExpanders();
  }
  public InflaterProvider(final Toolbox tb) {
    toolbox = tb;
  }
  public static Toolbox freshCopyOfAllExpanders() {
    return new Toolbox()//
        .add(ReturnStatement.class, //
            new ReturnTernaryExpander(), //
            new ExtractExpressionFromReturn(), //
            new StatementExtractParameters<>(), //
            null) //
        .add(ExpressionStatement.class, //
            new AssignmentAndAssignmentBloater(), //
            new AssignmentTernaryBloater(), //
            new ClassInstanceIntoVariable(), //
            new StatementExtractParameters<>(), //
            null)//
        .add(ArrayAccess.class, //
            new OutlineArrayAccess(), //
            null) //
        .add(InfixExpression.class, //
            new ToStringExpander(), //
            new TernaryPushupStrings(), //
            new MultiplicationToCast(), //
            // new BooleanExpressionBloater(), //
            null) //
        .add(PrefixExpression.class, //
            new PrefixToInfix(), //
            null) //
        .add(SwitchStatement.class, //
            new CasesSplit(), //
            new SwitchMissingDefaultAdd(), //
            null)//
        .add(Assignment.class, //
            new AssignmentOperatorBloater(), //
            null)
        .add(TryStatement.class, //
            new MultiTypeCatchClause(), //
            null)//
        .add(VariableDeclarationStatement.class, //
            new VariableDeclarationStatementSplit(), //
            null) //
        .add(VariableDeclarationStatement.class, //
            new DeclarationWithInitializerBloater(), //
            null) //
        .add(MethodInvocation.class, //
            new OutlineTernaryMethodInvocation(), //
            null) //
        // .add(MethodDeclaration.class, //
        // // new MethodDeclarationNameExpander(),
        // // new AddModifiersToMethodDeclaration(), //
        // null) //
        // .add(EnumDeclaration.class, //
        // new AddModifiersToEnums(), //
        // null) //
        .add(ExpressionStatement.class, //
            new MethodInvocationTernaryBloater(), //
            null) //
        .add(ThrowStatement.class, //
            new ThrowTernaryBloater(), //
            null)//
        .add(EnhancedForStatement.class, //
            new ForEachBlockBloater(), //
            null) //
        .add(ForStatement.class, //
            new ForBlockBloater(), //
            null) //
        .add(WhileStatement.class, //
            new WhileBlockBloater(), //
            null) //
        .add(IfStatement.class, //
            new IfElseBlockBloater(), //
            new LongIfBloater(), //
            null) //
        .add(InfixExpression.class, //
            new ParenthesesBloater(), //
            new TernaryPushup(), //
            null) //
        .add(VariableDeclarationFragment.class, //
            new LocalInitializedCollection(), //
            null)//
    ;//
  }
  @Override public <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return toolbox.firstTipper(¢);
  }
  public InflaterProvider provideAll() {
    function = λ -> λ;
    return this;
  }
  @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
    return function;
  }
}
