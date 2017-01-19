package il.org.spartan.bloater;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.bloater.SingleFlater.*;
import il.org.spartan.bloater.bloaters.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** holds the new toolbox for the expanders and returns them
 * @author Raviv Rachmiel
 * @since 20-12-16 */
public class InflaterProvider extends OperationsProvider {
  final Toolbox toolbox;

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
            new ExtractExpressionFromReturn()) //
        // new StatementExtractParameters<>()) //
        .add(ExpressionStatement.class, //
            new AssignmentAndAssignment(), //
            new AssignmentTernaryExpander(), //
            // new StatementExtractParameters<>(),
            new ClassInstanceIntoVariable())//
        .add(ArrayAccess.class, //
            new OutlineArrayAccess()) //
        .add(InfixExpression.class, //
            new ToStringExpander(), //
            new TernaryPushupStrings(), //
            new MultiplicationToCast()//
        // new ExpandBooleanExpression() //
        )//
        .add(PrefixExpression.class, //
            new PrefixToPostfix()) //
        .add(PostfixExpression.class, //
            new PostFixToInfixExpander())//
        .add(SwitchStatement.class, //
            new CasesSplit())//
        .add(Assignment.class, //
            new AssignmentOperatorExpansion())//
        .add(TryStatement.class, //
            new MultiTypeCatchClause())//
        .add(VariableDeclarationStatement.class, //
            new VariableDeclarationStatementSplit()) //
        .add(VariableDeclarationStatement.class, //
            new DeclarationWithInitExpander()) //
        // .add(MethodInvocation.class, //
        // new OutlineTernaryMethodInvocation()) //
        .add(ExpressionStatement.class, //
            new MethodInvocationTernaryExpander()) //
         .add(MethodDeclaration.class, //
         new RenameShortNamesMethodDec()) //
        // .add(VariableDeclarationStatement.class, //
        // new RenameShortNamesVarDec()) //
        .add(ThrowStatement.class, //
            new ThrowTernaryExpander())//
        .add(EnhancedForStatement.class, //
            new ForEachBlockExpander()) //
        .add(ForStatement.class, //
            new ForBlockExpander()) //
        .add(WhileStatement.class, //
            new WhileBlockExpander()) //
        .add(IfStatement.class, //
            new IfElseBlockExpander()) ///
        // new LongIfExpander()) //
        .add(InfixExpression.class, //
            new ParenthesesExpander(), new TernaryPushup()) //
    ;//
  }

  @Override public <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return toolbox.firstTipper(¢);
  }

  @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
    return (list) -> Collections.singletonList(list.get(0));
  }
}
