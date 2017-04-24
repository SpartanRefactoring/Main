package il.org.spartan.athenizer;

import static il.org.spartan.lisp.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.athenizer.SingleFlater.*;
import il.org.spartan.athenizer.bloaters.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;
import nano.ly.*;

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
        .add(SwitchStatement.class, //
            new CasesSplit(), new SwitchAddDefault())//
        .add(Assignment.class, //
            new AssignmentOperatorBloater()//
        // new PlusAssignToPostfix())//
        ).add(TryStatement.class, //
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
            new ForBlockBloater() //
        ) //
        .add(WhileStatement.class, //
            new WhileBlockBloater()) //
        .add(IfStatement.class, //
            new IfElseBlockBloater() ///
            , new LongIfBloater()) //
        .add(InfixExpression.class, //
            new ParenthesesBloater(), new TernaryPushup()) //
    ;//
  }

  @Override public <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return configuration.firstTipper(¢);
  }

  @Override public Function<List<Operation<?>>, List<Operation<?>>> getFunction() {
    return λ -> Collections.singletonList(the.headOf(λ));
  }
}
