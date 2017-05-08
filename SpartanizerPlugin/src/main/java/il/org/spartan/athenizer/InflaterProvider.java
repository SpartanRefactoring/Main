package il.org.spartan.athenizer;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.athenizer.SingleFlater.*;
import il.org.spartan.athenizer.bloaters.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;

/** holds the new configuration for the expanders and returns them
 * @author Raviv Rachmiel
 * @since 20-12-16 */
public class InflaterProvider extends OperationsProvider {
  final Configuration configuration;

  public InflaterProvider() {
    configuration = InflaterProvider.freshCopyOfAllExpanders();
  }
  public InflaterProvider(final Configuration tb) {
    configuration = tb;
  }
  public static Configuration freshCopyOfAllExpanders() {
    return new Configuration()//
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
            null) //
        .add(PrefixExpression.class, //
            new PrefixToInfix(), //
            null) //
        .add(SwitchStatement.class, //
            new CasesSplit(), new SwitchMissingDefaultAdd(), //
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
            new ParenthesesBloater(), new TernaryPushup(), //
            null) //
    ;//
  }
  @Override public <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return configuration.firstTipper(¢);
  }
  @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
    return λ -> Collections.singletonList(the.headOf(λ));
  }
}
