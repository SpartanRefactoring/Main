package il.org.spartan.spartanizer.ast.safety;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.utils.*;
import nano.ly.*;

public abstract class ASTMapReducer<R> extends MapOfLeaves<R> {
  public R map(final ASTNode ¢) {
    return iz.nodeTypeIn(¢, protect()) ? reduce()//
        : iz.statement(¢) ? map(az.statement(¢))//
            : iz.expression(¢) ? map(az.expression(¢))//
                : ¢.getNodeType() == ANONYMOUS_CLASS_DECLARATION ? map((AnonymousClassDeclaration) ¢)//
                    : ¢.getNodeType() == MODIFIER ? map((Modifier) ¢) //
                        : ¢.getNodeType() == CATCH_CLAUSE ? map((CatchClause) ¢)//
                            : ¢.getNodeType() == METHOD_REF ? map((MethodRef) ¢)
                                : ¢.getNodeType() == METHOD_REF_PARAMETER ? map((MethodRefParameter) ¢) //
                                    : reduce();
  }

  protected R composite(final List<? extends ASTNode> ns) {
    R $ = reduce();
    for (final ASTNode ¢ : ns)
      $ = reduce($, map(¢));
    return $;
  }

  protected R compound(final Expression... ¢) {
    return foldl(as.list(¢));
  }

  protected final R foldl(final Iterable<? extends ASTNode> ns) {
    R $ = reduce();
    if (ns != null)
      for (final ASTNode ¢ : ns)
        $ = reduce($, map(¢));
    return $;
  }

  protected R foldListModifiers(final List<IExtendedModifier> ms) {
    R $ = reduce();
    for (final IExtendedModifier ¢ : ms)
      $ = reduce($, map(¢));
    return $;
  }

  @SuppressWarnings("static-method") protected int[] protect() {
    return new int[] {};
  }

  protected R map(final AbstractTypeDeclaration ¢) {
    return reduce(map(¢.getJavadoc()), foldListModifiers(step.extendedModifiers(¢)), map(¢.getName()), foldl(step.bodyDeclarations(¢)));
  }

  protected R map(final Annotation ¢) {
    return ¢ instanceof MarkerAnnotation ? map((MarkerAnnotation) ¢) //
        : ¢ instanceof NormalAnnotation ? map((NormalAnnotation) ¢) //
            : ¢ instanceof SingleMemberAnnotation ? map((SingleMemberAnnotation) ¢) //
                : note.bug("Unrecognized Annotation; __=%s", English.name(¢.getClass())) //
    ;
  }

  protected R map(final AnonymousClassDeclaration ¢) {
    return bodyDeclarations(¢).stream().map(this::map).reduce(this::reduce).orElse(reduce());
  }

  protected R map(final ArrayAccess ¢) {
    return reduce(map(¢.getArray()), map(¢.getIndex()));
  }

  protected R map(final ArrayCreation ¢) {
    return reduce(foldl(dimensions(¢)), map(¢.getInitializer()));
  }

  protected R map(final ArrayInitializer ¢) {
    return foldl(expressions(¢));
  }

  protected R map(final AssertStatement ¢) {
    return compound(¢.getExpression(), ¢.getMessage());
  }

  protected R map(final Assignment ¢) {
    return reduce(map(to(¢)), map(from(¢)));
  }

  protected R map(final Block ¢) {
    return foldl(statements(¢));
  }

  protected R map(final BreakStatement ¢) {
    return compound(¢.getLabel());
  }

  protected R map(final CastExpression ¢) {
    return map(¢.getExpression());
  }

  protected R map(final CatchClause ¢) {
    return reduce(map(¢.getException()), map(¢.getBody()));
  }

  protected R map(final ClassInstanceCreation ¢) {
    return reduce(map(¢.getExpression()), foldl(arguments(¢)));
  }

  protected R map(final ConditionalExpression ¢) {
    return reduce(map(¢.getExpression()), map(then(¢)), map(elze(¢)));
  }

  protected R map(final ConstructorInvocation ¢) {
    return foldl(arguments(¢));
  }

  protected R map(final ContinueStatement ¢) {
    return compound(¢.getLabel());
  }

  protected R map(final DoStatement ¢) {
    return map(¢.getBody());
  }

  protected R map(final EnhancedForStatement ¢) {
    return map(¢.getBody());
  }

  protected R map(final Expression ¢) {
    if (¢ == null)
      return reduce();
    switch (¢.getNodeType()) {
      case SIMPLE_NAME:
        return map((SimpleName) ¢);
      case QUALIFIED_NAME:
        return map((QualifiedName) ¢);
      case THIS_EXPRESSION:
        return map((ThisExpression) ¢);
      case CHARACTER_LITERAL:
        return map((CharacterLiteral) ¢);
      case NUMBER_LITERAL:
        return map((NumberLiteral) ¢);
      case NULL_LITERAL:
        return map((NullLiteral) ¢);
      case STRING_LITERAL:
        return map((StringLiteral) ¢);
      case PREFIX_EXPRESSION:
        return map((PrefixExpression) ¢);
      case INFIX_EXPRESSION:
        return foldl(extract.allOperands((InfixExpression) ¢));
      case CONDITIONAL_EXPRESSION:
        return map((ConditionalExpression) ¢);
      case INSTANCEOF_EXPRESSION:
        return map((InstanceofExpression) ¢);
      case ARRAY_ACCESS:
        return map((ArrayAccess) ¢);
      case PARENTHESIZED_EXPRESSION:
        return map(extract.core(¢));
      case ASSIGNMENT:
        return map((Assignment) ¢);
      case ARRAY_INITIALIZER:
        return map((ArrayInitializer) ¢);
      case ARRAY_CREATION:
        return map((ArrayCreation) ¢);
      case CLASS_INSTANCE_CREATION:
        return map((ClassInstanceCreation) ¢);
      case POSTFIX_EXPRESSION:
        return map((PostfixExpression) ¢);
      case METHOD_INVOCATION:
        return map((MethodInvocation) ¢);
      case SUPER_METHOD_INVOCATION:
        return map((SuperMethodInvocation) ¢);
      case BOOLEAN_LITERAL:
        return map((BooleanLiteral) ¢);
      case CAST_EXPRESSION:
        return map((CastExpression) ¢);
      case LAMBDA_EXPRESSION:
        return map((LambdaExpression) ¢);
      case FIELD_ACCESS:
        return map((FieldAccess) ¢);
      case EXPRESSION_METHOD_REFERENCE:
        return map((ExpressionMethodReference) ¢);
      default:
        return note.bug("Unrecognized Node %s NodeType= %d %s", ¢.getClass(), box.it(¢.getNodeType()), ¢);
    }
  }

  protected R map(final MethodRefParameter ¢) {
    return reduce(map(¢.getType()), map(¢.getName()));
  }

  protected R map(final MethodRef ¢) {
    return reduce(map(¢), map(¢));
  }

  protected R map(final ExpressionMethodReference ¢) {
    return reduce(map(¢.getExpression()), map(¢.getName()));
  }

  protected R map(final FieldAccess ¢) {
    return reduce(map(¢.getExpression()), map(¢.getName()));
  }

  protected R map(final ExpressionStatement ¢) {
    return compound(¢.getExpression());
  }

  protected R map(final ForStatement ¢) {
    return reduce(composite(initializers(¢)), map(¢.getExpression()), composite(updaters(¢)), map(¢.getBody()));
  }

  protected R map(final IExtendedModifier ¢) {
    return ¢.isAnnotation() ? map((Annotation) ¢) //
        : ¢.isModifier() ? map((Modifier) ¢) //
            : note.bug("Unrecognized IExtendedModifier; __=%s", English.name(¢.getClass()));
  }

  protected R map(final IfStatement ¢) {
    return reduce(map(expression(¢)), map(then(¢)), map(elze(¢)));
  }

  protected R map(final InstanceofExpression ¢) {
    return map(¢.getLeftOperand());
  }

  protected R map(final Javadoc j) {
    return step.tags(j).stream().map(λ -> map(λ)).reduce(reduce(), (x1, x2) -> reduce(x1, x2));
  }

  protected R map(final LabeledStatement ¢) {
    return reduce(map(¢.getLabel()), map(¢.getBody()));
  }

  /** TODO: fix this! */
  protected R map(@SuppressWarnings("unused") final LambdaExpression fixMe) {
    return reduce();
  }

  protected R map(final MarkerAnnotation ¢) {
    return reduce(map(¢.getTypeName()));
  }

  protected R map(final MethodInvocation ¢) {
    return reduce(map(expression(¢)), foldl(arguments(¢)));
  }

  protected R map(final NormalAnnotation ¢) {
    return reduce(map(¢.getTypeName()), composite(values(¢)));
  }

  protected R map(final PostfixExpression ¢) {
    return map(expression(¢));
  }

  protected R map(final PrefixExpression ¢) {
    return map(expression(¢));
  }

  protected R map(final QualifiedName ¢) {
    return reduce(map(¢.getQualifier()), map(¢.getName()));
  }

  protected R map(final ReturnStatement ¢) {
    return compound(¢.getExpression());
  }

  protected R map(final SingleMemberAnnotation ¢) {
    return reduce(map(¢.getTypeName()), map(¢.getValue()));
  }

  protected R map(final Statement ¢) {
    if (¢ == null)
      return reduce();
    switch (¢.getNodeType()) {
      case ASSERT_STATEMENT:
        return map((AssertStatement) ¢);
      case BLOCK:
        return map((Block) ¢);
      case BREAK_STATEMENT:
        return map((BreakStatement) ¢);
      case CONSTRUCTOR_INVOCATION:
        return map((ConstructorInvocation) ¢);
      case CONTINUE_STATEMENT:
        return map((ContinueStatement) ¢);
      case DO_STATEMENT:
        return map((DoStatement) ¢);
      case EMPTY_STATEMENT:
        return map((EmptyStatement) ¢);
      case ENHANCED_FOR_STATEMENT:
        return map((EnhancedForStatement) ¢);
      case EXPRESSION_STATEMENT:
        return map((ExpressionStatement) ¢);
      case FOR_STATEMENT:
        return map((ForStatement) ¢);
      case IF_STATEMENT:
        return map((IfStatement) ¢);
      case LABELED_STATEMENT:
        return map((LabeledStatement) ¢);
      case RETURN_STATEMENT:
        return map((ReturnStatement) ¢);
      case SUPER_CONSTRUCTOR_INVOCATION:
        return map((SuperConstructorInvocation) ¢);
      case SYNCHRONIZED_STATEMENT:
        return map((SynchronizedStatement) ¢);
      case THROW_STATEMENT:
        return map((SuperConstructorInvocation) ¢);
      case TRY_STATEMENT:
        return map((TryStatement) ¢);
      case TYPE_DECLARATION_STATEMENT:
        return map(¢);
      case VARIABLE_DECLARATION_STATEMENT:
        return map((VariableDeclarationStatement) ¢);
      case WHILE_STATEMENT:
        return map((WhileStatement) ¢);
      default:
        assert fault.unreachable() : fault.specifically(String.format("Missing 'case' in switch for class: %s", wizard.nodeName(¢)));
        return reduce();
    }
  }

  protected R map(final SuperConstructorInvocation ¢) {
    return reduce(map(expression(¢)), foldl(arguments(¢)));
  }

  protected R map(final SuperMethodInvocation ¢) {
    return reduce(map(expression(¢)), foldl(arguments(¢)));
  }

  protected R map(final SwitchStatement ¢) {
    return reduce(map(¢.getExpression()), composite(statements(¢)));
  }

  protected R map(final SynchronizedStatement ¢) {
    return reduce(map(¢.getExpression()), map(¢.getBody()));
  }

  protected R map(final ThisExpression ¢) {
    return map(¢.getQualifier());
  }

  protected R map(final TryStatement ¢) {
    return reduce(//
        reduceResources(¢), //
        map(¢.getBody()), //
        reduceCatches(¢), //
        map(¢.getFinally())//
    );
  }

  protected R map(final TypeDeclarationStatement ¢) {
    return map(¢.getDeclaration());
  }

  protected R map(final VariableDeclarationExpression ¢) {
    return reduce(fragments(¢));
  }

  protected R map(final VariableDeclarationStatement ¢) {
    return reduce(fragments(¢));
  }

  protected R map(final WhileStatement ¢) {
    return reduce(map(¢.getExpression()), map(¢.getBody()));
  }

  protected R reduce(final Collection<VariableDeclarationFragment> ¢) {
    return ¢.stream().map(VariableDeclarationFragment::getInitializer).map(this::map).reduce(this::reduce).orElse(reduce());
  }

  protected R reduceCatches(final TryStatement ¢) {
    return catchClauses(¢).stream().map(CatchClause::getBody).map(this::map).reduce(this::reduce).orElse(reduce());
  }

  protected R reduceResources(final TryStatement ¢) {
    return resources(¢).stream().map(this::map).reduce(this::reduce).orElse(reduce());
  }
}
