package il.org.spartan.spartanizer.java;

import static il.org.spartan.spartanizer.ast.navigate.step.body;
import static il.org.spartan.spartanizer.ast.navigate.step.catchClauses;
import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.identifier;
import static il.org.spartan.spartanizer.ast.navigate.step.parent;
import static il.org.spartan.spartanizer.ast.navigate.step.resources;
import static il.org.spartan.spartanizer.ast.navigate.step.returnType;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import il.org.spartan.Wrapper;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.Coupling;
import il.org.spartan.spartanizer.engine.Recurser;
import il.org.spartan.spartanizer.engine.collect;
import il.org.spartan.spartanizer.engine.type;
import il.org.spartan.spartanizer.research.analyses.notation;
import il.org.spartan.utils.fault;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil
 * @since 2016-09-12 */
public enum haz {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  public static boolean annotation(final VariableDeclarationFragment ¢) {
    final ASTNode $ = parent(¢);
    switch ($.getNodeType()) {
      case ASTNode.FIELD_DECLARATION:
        return haz.annotation((FieldDeclaration) $);
      case ASTNode.LAMBDA_EXPRESSION:
        return haz.annotation((LambdaExpression) $);
      case ASTNode.SINGLE_VARIABLE_DECLARATION:
        return haz.annotation((SingleVariableDeclaration) $);
      case ASTNode.VARIABLE_DECLARATION_EXPRESSION:
        return haz.annotation((VariableDeclarationExpression) $);
      case ASTNode.VARIABLE_DECLARATION_STATEMENT:
        return haz.annotation((VariableDeclarationStatement) $);
      default:
        assert fault.unreachable() : fault.specifically("Unexpected node __", $, ¢);
        return false;
    }
  }
  private static boolean annotation(final SingleVariableDeclaration $) {
    return !extract.annotations($).isEmpty();
  }
  private static boolean annotation(@SuppressWarnings("unused") final LambdaExpression $) {
    return false;
  }
  private static boolean annotation(final VariableDeclarationExpression ¢) {
    return !extract.annotations(¢).isEmpty();
  }
  private static boolean annotation(@SuppressWarnings("unused") final FieldDeclaration __) {
    return false;
  }
  public static boolean annotation(final VariableDeclarationStatement ¢) {
    return !extract.annotations(¢).isEmpty();
  }
  /** @param ¢ JD
   */
  public static boolean anyStatements(final MethodDeclaration ¢) {
    return ¢ != null && statements(¢) != null && !statements(¢).isEmpty();
  }
  public static boolean binding(final ASTNode ¢) {
    return ¢ != null && ¢.getAST() != null && ¢.getAST().hasResolvedBindings();
  }
  /** Determines whether the method's return __ is boolean.
   * @param ¢ method
   */
  public static boolean booleanReturnType(final MethodDeclaration ¢) {
    return ¢ != null && returnType(¢) != null && iz.booleanType(returnType(¢));
  }
  public static boolean cent(final ASTNode ¢) {
    return !collect.usesOf(notation.cent).inside(¢).isEmpty();
  }
  /** Determine whether an {@link ASTNode} contains as a children a
   * {@link ContinueStatement}
   * @param ¢ JD
   * @return {@code true } iff ¢ contains any continue statement
   * @see {@link convertWhileToFor} */
  @SuppressWarnings("boxing") public static boolean continueStatement(final ASTNode ¢) {
    return ¢ != null
        && new Recurser<>(¢, 0).postVisit(λ -> λ.getRoot().getNodeType() != ASTNode.CONTINUE_STATEMENT ? λ.getCurrent() : λ.getCurrent() + 1) > 0;
  }
  public static boolean dollar(final ASTNode ¢) {
    return !collect.usesOf("$").inside(¢).isEmpty();
  }
  public static boolean dollar(final Collection<SimpleName> ns) {
    return ns.stream().anyMatch(λ -> "$".equals(identifier(λ)));
  }
  public static boolean name(final ASTNode ¢, final String s) {
    return !collect.usesOf(s).inside(¢).isEmpty();
  }
  public static boolean name(final Collection<SimpleName> ns, final String s) {
    return ns.stream().anyMatch(λ -> s.equals(identifier(λ)));
  }
  /** @param ¢ JD
   */
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
      boolean ¢(final ForStatement ¢) {
        return ¢(step.initializers(¢));
      }
      boolean ¢(final Collection<Expression> xs) {
        return xs.stream().anyMatch(λ -> iz.variableDeclarationExpression(λ) && ¢(az.variableDeclarationExpression(λ)));
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
   */
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
  public static boolean variableDefinition(final ASTNode n) {
    final Wrapper<Boolean> $ = new Wrapper<>(Boolean.FALSE);
    n.accept(new ASTVisitor(true) {
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
  public static boolean hasObject(final List<Type> ¢) {
    return ¢ != null && ¢.stream().anyMatch(type::isObject);
  }
  public static boolean hasSafeVarags(final MethodDeclaration d) {
    return extract.annotations(d).stream().anyMatch(λ -> iz.identifier("SafeVarargs", λ.getTypeName()));
  }
  /** @param ns unknown number of nodes to check
   * @return whether one of the nodes is an Expression Statement of __ Post or
   *         Pre Expression with ++ or -- operator. false if none of them are or
   *         if the given parameter is null. */
  public static boolean containIncOrDecExp(final ASTNode... ns) {
    return ns != null && Stream.of(ns).anyMatch(λ -> λ != null && iz.updating(λ));
  }
  public static boolean updaters(final ForStatement ¢) {
    return !step.updaters(¢).isEmpty();
  }
  public static boolean initializers(final ForStatement ¢) {
    return !step.initializers(¢).isEmpty();
  }
}
