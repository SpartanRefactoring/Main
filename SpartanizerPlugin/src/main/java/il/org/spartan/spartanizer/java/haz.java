package il.org.spartan.spartanizer.java;
import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;
import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2016-09-12 */
public enum haz {
  ;
  public static boolean annotation(final VariableDeclarationFragment ¢) {
    return annotation((VariableDeclarationStatement) ¢.getParent());
  }

  public static boolean annotation(final VariableDeclarationStatement ¢) {
    return !extract.annotations(¢).isEmpty();
  }

  /** @param ¢ JD
   * @return */
  public static boolean anyStatements(final MethodDeclaration ¢) {
    return ¢ != null && statements(¢) != null && !statements(¢).isEmpty();
  }

  public static boolean binding(final ASTNode ¢) {
    return ¢ != null && ¢.getAST() != null && ¢.getAST().hasResolvedBindings();
  }

  /** Determines whether the method's return type is boolean.
   * @param ¢ method
   * @return */
  public static boolean booleanReturnType(final MethodDeclaration ¢) {
    return ¢ != null && returnType(¢) != null && iz.booleanType(returnType(¢));
  }

  public static boolean cent(final ASTNode ¢) {
    return !collect.usesOf(namer.current).inside(¢).isEmpty();
  }

  /** Determine whether an {@link ASTNode} contains as a children a
   * {@link ContinueStatement}
   * @param ¢ JD
   * @return {@code true } iff ¢ contains any continue statement
   * @see {@link convertWhileToFor} */
  @SuppressWarnings("boxing")
  public static boolean containsContinueStatement(final ASTNode ¢) {
    return ¢ != null
        && new Recurser<>(¢, 0).postVisit(λ -> λ.getRoot().getNodeType() != CONTINUE_STATEMENT ? λ.getCurrent() : λ.getCurrent() + 1) > 0;
  }

  /** Determine whether an {@link ASTNode} contains as a children a
   * {@link ContinueStatement}
   * @param ¢ JD
   * @return {@code true } iff ¢ contains any continue statement
   * @see {@link convertWhileToFor} */
  @SuppressWarnings("boxing") public static boolean ContinueStatement(final ASTNode ¢) {
    return ¢ != null
        && new Recurser<>(¢, 0).postVisit(λ -> λ.getRoot().getNodeType() != ASTNode.CONTINUE_STATEMENT ? λ.getCurrent() : λ.getCurrent() + 1) > 0;
  }

  public static boolean dollar(final ASTNode ¢) {
    return !collect.usesOf("$").inside(¢).isEmpty();
  }

  public static boolean dollar(final Collection<SimpleName> ns) {
    return ns.stream().anyMatch(λ -> "$".equals(identifier(λ)));
  }

  /** @param ¢ JD
   * @return */
  public static boolean expression(final MethodInvocation ¢) {
    return ¢ != null && step.expression(¢) != null;
  }

  public static boolean final¢(final Collection<IExtendedModifier> ms) {
    return ms.stream().anyMatch(λ -> IExtendedModifiersRank.find(λ) == IExtendedModifiersRank.FINAL);
  }

  static boolean hasAnnotation(final Collection<IExtendedModifier> ¢) {
    return ¢.stream().anyMatch(IExtendedModifier::isAnnotation);
  }

  public static boolean hasNoModifiers(final BodyDeclaration ¢) {
    return !¢.modifiers().isEmpty();
  }

  public static boolean hidings(final List<Statement> ss) {
    return new Predicate<List<Statement>>() {
      final Collection<String> dictionary = new HashSet<>();

      boolean ¢(final CatchClause ¢) {
        return ¢(¢.getException());
      }

      boolean ¢(final Collection<Expression> xs) {
        return xs.stream().anyMatch(λ -> iz.variableDeclarationExpression(λ) && ¢(az.variableDeclarationExpression(λ)));
      }

      boolean ¢(final ForStatement ¢) {
        return ¢(initializers(¢));
      }

      boolean ¢(final SimpleName ¢) {
        return ¢(identifier(¢));
      }

      boolean ¢(final SingleVariableDeclaration ¢) {
        return ¢(step.name(¢));
      }

      boolean ¢(final Statement ¢) {
        return ¢ instanceof VariableDeclarationStatement ? ¢((VariableDeclarationStatement) ¢) //
            : ¢ instanceof ForStatement ? ¢((ForStatement) ¢) //
                : ¢ instanceof TryStatement && ¢((TryStatement) ¢);
      }

      boolean ¢(final String ¢) {
        if (dictionary.contains(¢))
          return true;
        dictionary.add(¢);
        return false;
      }

      boolean ¢(final TryStatement ¢) {
        return ¢¢¢(resources(¢)) || ¢¢(catchClauses(¢));
      }

      boolean ¢(final VariableDeclarationExpression ¢) {
        return ¢¢¢¢(fragments(¢));
      }

      boolean ¢(final VariableDeclarationFragment ¢) {
        return ¢(step.name(¢));
      }

      boolean ¢(final VariableDeclarationStatement ¢) {
        return ¢¢¢¢(fragments(¢));
      }

      boolean ¢¢(final Collection<CatchClause> cs) {
        return cs.stream().anyMatch(this::¢);
      }

      boolean ¢¢¢(final Collection<VariableDeclarationExpression> xs) {
        return xs.stream().anyMatch(this::¢);
      }

      boolean ¢¢¢¢(final Collection<VariableDeclarationFragment> fs) {
        return fs.stream().anyMatch(this::¢);
      }

      @Override public boolean test(final List<Statement> ¢¢) {
        return ¢¢.stream().anyMatch(this::¢);
      }
    }.test(ss);
  }

  /** @param ¢ JD
   * @return */
  public static boolean methods(final AbstractTypeDeclaration ¢) {
    return step.methods(¢) != null && !step.methods(¢).isEmpty();
  }

  public static boolean sideEffects(final Expression ¢) {
    return !sideEffects.free(¢);
  }

  public static boolean unknownNumberOfEvaluations(final MethodDeclaration d) {
    final Block $ = body(d);
    return $ != null && statements($).stream().anyMatch(λ -> Coupling.unknownNumberOfEvaluations(d, λ));
  }

  public static boolean updates(Expression from) {
    return !new ExpressionBottomUp<List<ASTNode>>() {
      List<ASTNode> atomic(Expression operand) {
        return Collections.singletonList(operand);
      }

      @Override protected List<ASTNode> map(Assignment x) {
        return reduce(Collections.singletonList(to(x)), super.map(x));
      }

      @Override protected List<ASTNode> map(PostfixExpression ¢) {
        return reduce(Collections.singletonList(step.expression(¢)), super.map(¢));
      }

      @Override protected List<ASTNode> map(PrefixExpression ¢) {
        return reduce(!updating(¢) ? reduce() : atomic(¢.getOperand()), super.map(¢));
      }

      @Override public List<ASTNode> reduce(List<ASTNode> l1, List<ASTNode> l2) {
        l1.addAll(l2);
        return l1;
      }

      boolean updating(PrefixExpression ¢) {
        return in(¢.getOperator(), INCREMENT, DECREMENT);
      }
    }.map(from).isEmpty();
  }

  public static boolean variableDefinition(final ASTNode n) {
    final Wrapper<Boolean> $ = new Wrapper<>(Boolean.FALSE);
    n.accept(new ASTVisitor() {
      boolean continue¢(final Collection<VariableDeclarationFragment> fs) {
        return fs.stream().anyMatch(λ -> continue¢(step.name(λ)));
      }

      boolean continue¢(final SimpleName ¢) {
        if (iz.identifier("$", ¢))
          return false;
        $.set(Boolean.TRUE);
        return true;
      }

      @Override public boolean visit(final EnumConstantDeclaration ¢) {
        return continue¢(step.name(¢));
      }

      @Override public boolean visit(final FieldDeclaration node) {
        return continue¢(fragments(node));
      }

      @Override public boolean visit(final SingleVariableDeclaration node) {
        return continue¢(node.getName());
      }

      @Override public boolean visit(final VariableDeclarationExpression node) {
        return continue¢(fragments(node));
      }

      @Override public boolean visit(final VariableDeclarationFragment ¢) {
        return continue¢(step.name(¢));
      }

      @Override public boolean visit(final VariableDeclarationStatement ¢) {
        return continue¢(fragments(¢));
      }
    });
    return $.get().booleanValue();
  }
}
