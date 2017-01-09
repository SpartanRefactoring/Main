package il.org.spartan.zoomer.zoomin;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.inflate.zoomers.*;
import il.org.spartan.zoomer.zoomin.SingleFlater.*;

/** holds the new toolbox for the expanders and returns them
 * @author Raviv Rachmiel
 * @since 20-12-16 */
public class InflaterProvider extends OperationsProvider {
  Toolbox toolbox;

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
        .add(ExpressionStatement.class, //
            new AssignmentAndAssignment(), //
            new AssignmentTernaryExpander())//
        .add(ArrayAccess.class, //
            new OutlineArrayAccess()) //
        .add(InfixExpression.class, //
            new toStringExpander(), new TernaryPushupStrings())//
        .add(PrefixExpression.class, //
            new PrefixToPostfix()) //
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
        .add(ExpressionStatement.class, //
            new MethodInvocationTernaryExpander()) //
        .add(ThrowStatement.class, //
            new ThrowTernaryExpander())//
        .add(ForStatement.class, //
            new ForBlockExpander()) //
        .add(WhileStatement.class, //
            new WhileBlockExpander()) //
        .add(IfStatement.class, //
            new IfElseBlockExpander()) //
    ;//
  }

  @Override public <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return toolbox.firstTipper(¢);
  }

  @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
    return (list) -> Collections.singletonList(list.get(0));
  }
}
