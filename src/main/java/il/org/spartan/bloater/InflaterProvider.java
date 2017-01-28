package il.org.spartan.bloater;

import java.util.*;
import java.util.function.*;

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
        .add(ExpressionStatement.class, //
            new AssignmentAndAssignmentBloater(), //
            new AssignmentTernaryBloater(), //
            new ClassInstanceIntoVariable(), //
            new StatementExtractParameters<>())//
        .add(ArrayAccess.class, //
            new OutlineArrayAccess()) //
        .add(InfixExpression.class, //
            new ToStringExpander(), //
            new TernaryPushupStrings(), //
            new MultiplicationToCast()//
        // new BooleanExpressionBloater() //
        )//
        .add(PrefixExpression.class, //
            new PrefixToInfix()) //
        // .add(PostfixExpression.class, //
        // new PostFixToInfixExpander())//
        .add(SwitchStatement.class, //
            new CasesSplit())//
        .add(Assignment.class, //
            new AssignmentOperatorBloater())//
        .add(TryStatement.class, //
            new MultiTypeCatchClause())//
        .add(VariableDeclarationStatement.class, //
            new VariableDeclarationStatementSplit()) //
        .add(VariableDeclarationStatement.class, //
            new DeclarationWithInitializerBloater()) //
        .add(MethodInvocation.class, //
            new OutlineTernaryMethodInvocation()) //
        .add(ExpressionStatement.class, //
            new MethodInvocationTernaryBloater()) //
        // .add(MethodDeclaration.class, //
        // new RenameShortNamesMethodDec()) //
        // .add(VariableDeclarationStatement.class, //
        // new RenameShortNamesVarDec()) //
        .add(ThrowStatement.class, //
            new ThrowTernaryBloater())//
        .add(EnhancedForStatement.class, //
            new ForEachBlockBloater()) //
        .add(ForStatement.class, //
            new ForBlockBloater()) //
        .add(WhileStatement.class, //
            new WhileBlockBloater()) //
        .add(IfStatement.class, //
            new IfElseBlockBloater()) ///
        // new LongIfBloater()) //
        .add(InfixExpression.class, //
            new ParenthesesBloater(), new TernaryPushup()) //
    ;//
  }

  @Override public <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return toolbox.firstTipper(¢);
  }

  @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
    return λ -> Collections.singletonList(λ.get(0));
  }
}
