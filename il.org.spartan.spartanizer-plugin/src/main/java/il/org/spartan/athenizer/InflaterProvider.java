package il.org.spartan.athenizer;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.athenizer.SingleFlater.*;
import il.org.spartan.athenizer.zoomers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;

/** holds the new configuration for the expanders and returns them
 * @author Raviv Rachmiel
 * @since 20-12-16 */
public class InflaterProvider extends OperationsProvider {
  final Toolbox configuration;
  Function<List<Operation<?>>, List<Operation<?>>> function = λ -> Collections.singletonList(the.firstOf(λ));

  public InflaterProvider() {
    configuration = InflaterProvider.freshCopyOfAllExpanders();
  }
  public InflaterProvider(final Toolbox tb) {
    configuration = tb;
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
    return configuration.firstTipper(¢);
  }
  public InflaterProvider provideAll() {
    function = λ -> λ;
    return this;
  }
  @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
    return function;
  }
}
