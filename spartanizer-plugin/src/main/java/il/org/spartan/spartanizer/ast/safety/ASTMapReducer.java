package il.org.spartan.spartanizer.ast.safety;

import static il.org.spartan.spartanizer.ast.navigate.step.arguments;
import static il.org.spartan.spartanizer.ast.navigate.step.bodyDeclarations;
import static il.org.spartan.spartanizer.ast.navigate.step.catchClauses;
import static il.org.spartan.spartanizer.ast.navigate.step.dimensions;
import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.expressions;
import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.from;
import static il.org.spartan.spartanizer.ast.navigate.step.initializers;
import static il.org.spartan.spartanizer.ast.navigate.step.resources;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.ast.navigate.step.then;
import static il.org.spartan.spartanizer.ast.navigate.step.to;
import static il.org.spartan.spartanizer.ast.navigate.step.updaters;
import static il.org.spartan.spartanizer.ast.navigate.step.values;
import static org.eclipse.jdt.core.dom.ASTNode.ANONYMOUS_CLASS_DECLARATION;
import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_ACCESS;
import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_CREATION;
import static org.eclipse.jdt.core.dom.ASTNode.ARRAY_INITIALIZER;
import static org.eclipse.jdt.core.dom.ASTNode.ASSERT_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.ASSIGNMENT;
import static org.eclipse.jdt.core.dom.ASTNode.BLOCK;
import static org.eclipse.jdt.core.dom.ASTNode.BOOLEAN_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.BREAK_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.CAST_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.CATCH_CLAUSE;
import static org.eclipse.jdt.core.dom.ASTNode.CHARACTER_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.CLASS_INSTANCE_CREATION;
import static org.eclipse.jdt.core.dom.ASTNode.CONDITIONAL_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.CONSTRUCTOR_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.CONTINUE_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.CREATION_REFERENCE;
import static org.eclipse.jdt.core.dom.ASTNode.DO_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.EMPTY_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.ENHANCED_FOR_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.EXPRESSION_METHOD_REFERENCE;
import static org.eclipse.jdt.core.dom.ASTNode.EXPRESSION_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.FIELD_ACCESS;
import static org.eclipse.jdt.core.dom.ASTNode.FOR_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.IF_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.INFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.INSTANCEOF_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.LABELED_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.LAMBDA_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.METHOD_DECLARATION;
import static org.eclipse.jdt.core.dom.ASTNode.METHOD_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.METHOD_REF;
import static org.eclipse.jdt.core.dom.ASTNode.METHOD_REF_PARAMETER;
import static org.eclipse.jdt.core.dom.ASTNode.MODIFIER;
import static org.eclipse.jdt.core.dom.ASTNode.NULL_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.NUMBER_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.PARENTHESIZED_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.POSTFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.PREFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.QUALIFIED_NAME;
import static org.eclipse.jdt.core.dom.ASTNode.RETURN_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.SIMPLE_NAME;
import static org.eclipse.jdt.core.dom.ASTNode.STRING_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.SUPER_CONSTRUCTOR_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.SUPER_METHOD_INVOCATION;
import static org.eclipse.jdt.core.dom.ASTNode.SYNCHRONIZED_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.THIS_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.THROW_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.TRY_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.TYPE_DECLARATION_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.TYPE_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.VARIABLE_DECLARATION_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.VARIABLE_DECLARATION_FRAGMENT;
import static org.eclipse.jdt.core.dom.ASTNode.VARIABLE_DECLARATION_STATEMENT;
import static org.eclipse.jdt.core.dom.ASTNode.WHILE_STATEMENT;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import fluent.ly.English;
import fluent.ly.as;
import fluent.ly.box;
import fluent.ly.note;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.utils.fault;

public abstract class ASTMapReducer<R> extends MapOfLeaves<R> {
  public R map(final ASTNode ¢) {
    return ¢ == null || iz.nodeTypeIn(¢, protect()) ? reduce()
        : iz.statement(¢) ? map(az.statement(¢))
            : iz.expression(¢) ? map(az.expression(¢))
                : ¢.getNodeType() == ANONYMOUS_CLASS_DECLARATION ? map((AnonymousClassDeclaration) ¢)
                    : ¢.getNodeType() == MODIFIER ? map((Modifier) ¢)
                        : ¢.getNodeType() == CATCH_CLAUSE ? map((CatchClause) ¢)
                            : ¢.getNodeType() == METHOD_REF ? map((MethodRef) ¢)
                                : ¢.getNodeType() == METHOD_REF_PARAMETER ? map((MethodRefParameter) ¢)
                                    : ¢.getNodeType() == METHOD_DECLARATION ? map((MethodDeclaration) ¢)
                                        : ¢.getNodeType() == VARIABLE_DECLARATION_FRAGMENT ? map((VariableDeclarationFragment) ¢) : reduce();
  }
  protected R composite(final List<? extends ASTNode> ns) {
    R $ = reduce();
    for (final ASTNode ¢ : ns)
      $ = reduce($, map(¢));
    return $;
  }
  protected R compound(final Expression... ¢) {
    return foldl(as.list(¢).stream().filter(λ -> λ != null).collect(Collectors.toList()));
  }
  protected R map(final List<Statement> ss) {
    R $ = reduce();
    if (ss != null)
      for (final ASTNode ¢ : ss)
        $ = reduce($, map(¢));
    return $;
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
    return map(statements(¢));
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
        return map((InfixExpression) ¢);
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
      case VARIABLE_DECLARATION_EXPRESSION:
        return map((VariableDeclarationExpression) ¢);
      case TYPE_LITERAL:
        return map((TypeLiteral) ¢);
      case CREATION_REFERENCE:
        return map((CreationReference) ¢);
      default:
        return note.bug("Unrecognized Node %s NodeType= %d %s", ¢.getClass(), box.it(¢.getNodeType()), ¢);
    }
  }
  protected R map(final MethodDeclaration ¢) {
    return reduce(map(¢.getJavadoc()), foldListModifiers(step.extendedModifiers(¢)), map(¢.getName()), foldl(step.parameters(¢)), map(step.body(¢)));
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
  protected R map(final Javadoc ¢) {
    return ¢ == null ? reduce() : step.tags(¢).stream().map(this::map).reduce(reduce(), this::reduce);
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
  protected R map(final InfixExpression ¢) {
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
        return map((ThrowStatement) ¢);
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
  // TODO yossi gil: check if it is needed (mapping a Type node)
  @SuppressWarnings("unused") protected R map(final Type ¢) {
    if (¢ == null)
      return reduce();
    ¢.getNodeType();
    assert true || fault.unreachable() : fault.specifically(String.format("Missing 'case' in switch for class: %s", wizard.nodeName(¢)));
    return reduce();
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
  protected R map(final ThrowStatement ¢) {
    return map(expression(¢));
  }
  protected R map(final TypeDeclarationStatement ¢) {
    return map(¢.getDeclaration());
  }
  protected R map(final VariableDeclarationExpression ¢) {
    return reduce(fragments(¢));
  }
  // TODO yossi gil: check if it's alright
  protected R map(final VariableDeclarationFragment ¢) {
    return reduce(as.list(¢));
  }
  protected R map(final VariableDeclarationStatement ¢) {
    return reduce(fragments(¢));
  }
  protected R map(final WhileStatement ¢) {
    return reduce(map(¢.getExpression()), map(¢.getBody()));
  }
  // TODO yossi gil: check if it's alright
  protected R map(@SuppressWarnings("unused") final CreationReference ¢) {
    return reduce();
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
