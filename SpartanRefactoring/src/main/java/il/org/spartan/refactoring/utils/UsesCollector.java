package il.org.spartan.refactoring.utils;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;

abstract class HidingDepth extends ScopeManager {
  private int depth = 0;
  private int hideDepth = Integer.MAX_VALUE;

  boolean hidden() {
    return depth >= hideDepth;
  }
  void hide() {
    hideDepth = depth;
  }
  @Override void pop() {
    if (--depth < hideDepth)
      hideDepth = Integer.MAX_VALUE;
  }
  @Override final boolean push() {
    ++depth;
    return !hidden();
  }
}

abstract class ScopeManager extends ASTVisitor {
  @Override public final void endVisit(@SuppressWarnings("unused") final AnnotationTypeDeclaration __) {
    pop();
  }
  @Override public final void endVisit(@SuppressWarnings("unused") final AnonymousClassDeclaration __) {
    pop();
  }
  @Override public final void endVisit(@SuppressWarnings("unused") final Block __) {
    pop();
  }
  @Override public final void endVisit(@SuppressWarnings("unused") final EnhancedForStatement __) {
    pop();
  }
  @Override public final void endVisit(@SuppressWarnings("unused") final ForStatement __) {
    pop();
  }
  @Override public final void endVisit(@SuppressWarnings("unused") final TypeDeclaration __) {
    pop();
  }
  abstract boolean go(final AbstractTypeDeclaration ¢);
  abstract boolean go(final AnonymousClassDeclaration ¢);
  abstract boolean go(final EnhancedForStatement ¢);
  abstract void pop();
  abstract boolean push();
  @Override public final boolean visit(final AnnotationTypeDeclaration ¢) {
    push();
    return go(¢);
  }
  @Override public final boolean visit(final AnonymousClassDeclaration ¢d) {
    push();
    return go(¢d);
  }
  @Override public final boolean visit(@SuppressWarnings("unused") final Block __) {
    return push();
  }
  @Override public final boolean visit(final EnhancedForStatement ¢s) {
    push();
    return go(¢s);
  }
  @Override public final boolean visit(final EnumDeclaration ¢d) {
    push();
    return go(¢d);
  }
  @Override public final boolean visit(@SuppressWarnings("unused") final ForStatement __) {
    return push();
  }
  @Override public final boolean visit(final TypeDeclaration ¢d) {
    push();
    return go(¢d);
  }
}

class UsesCollector extends HidingDepth {
  @SuppressWarnings("unchecked") private static List<? extends ASTNode> declarations(final AbstractTypeDeclaration ¢) {
    return ¢.bodyDeclarations();
  }
  @SuppressWarnings("unchecked") private static List<? extends ASTNode> declarations(final AnonymousClassDeclaration ¢) {
    return ¢.bodyDeclarations();
  }

  private final List<SimpleName> result;
  private final SimpleName focus;

  UsesCollector(final List<SimpleName> result, final SimpleName focus) {
    this.result = result;
    this.focus = focus;
  }
  UsesCollector(final UsesCollector ¢) {
    this(¢.result, ¢.focus);
  }
  @Override protected UsesCollector clone() {
    return new UsesCollector(result, focus);
  }
  void consider(final SimpleName candidate) {
    if (hit(candidate))
      result.add(candidate);
  }
  private boolean declaredBy(final SimpleName ¢) {
    if (¢ == focus) {
      result.add(¢);
      return false;
    }
    if (!hit(¢))
      return false;
    hide();
    return true;
  }
  private boolean declaredIn(final AbstractTypeDeclaration d) {
    d.accept(new ASTVisitor() {
      @Override public boolean visit(final FieldDeclaration ¢) {
        return !hidden() && !declaredIn(¢);
      }
    });
    return hidden();
  }
  private boolean declaredIn(final AnonymousClassDeclaration ¢) {
    declaresField(¢);
    return hidden();
  }
  boolean declaredIn(final FieldDeclaration d) {
    for (final Object o : d.fragments())
      if (declaredIn((VariableDeclarationFragment) o))
        return true;
    return false;
  }
  private boolean declaredIn(final MethodDeclaration ¢) {
    for (final SingleVariableDeclaration o : expose.parameters(¢))
      if (declaredIn(o))
        return true;
    return false;
  }
  private boolean declaredIn(final SingleVariableDeclaration ¢) {
    return declaredBy(¢.getName());
  }
  private boolean declaredIn(final VariableDeclarationFragment ¢) {
    return declaredBy(¢.getName());
  }
  private void declaresField(final ASTNode ¢) {
    ¢.accept(new DeclaredInFields(¢));
  }
  @Override boolean go(final AbstractTypeDeclaration ¢) {
    ingore(¢.getName());
    return !declaredIn(¢) && recurse(declarations(¢));
  }
  boolean go(final AnnotationTypeDeclaration ¢) {
    ingore(¢.getName());
    return !declaredIn(¢) && recurse(declarations(¢));
  }
  @Override boolean go(final AnonymousClassDeclaration ¢) {
    return !declaredIn(¢) && recurse(declarations(¢));
  }
  @Override boolean go(final EnhancedForStatement ¢) {
    final SimpleName name = ¢.getParameter().getName();
    if (name == focus || !declaredBy(name))
      return true;
    recurse(¢.getExpression());
    return recurse(¢.getBody());
  }
  private boolean hit(final SimpleName ¢) {
    return same(¢, focus);
  }
  /**
   * This is where we ignore all occurrences of {@link SimpleName} which are not
   * variable names, e.g., class name, function name, field name, etc.
   *
   * @param _
   *          JD
   */
  private void ingore(@SuppressWarnings("unused") final SimpleName __) {
    // We simply ignore the parameter
  }
  @Override public boolean preVisit2(final ASTNode ¢) {
    return !hidden() && !(¢ instanceof Type);
  }
  boolean recurse(final ASTNode ¢) {
    if (¢ != null && !hidden())
      ¢.accept(clone());
    return false;
  }
  private boolean recurse(final Iterable<? extends ASTNode> ns) {
    for (final ASTNode ¢ : ns)
      recurse(¢);
    return false;
  }
  @Override public boolean visit(final CastExpression ¢) {
    return recurse(right(¢));
  }
  @Override public boolean visit(final FieldAccess ¢) {
    return recurse(¢.getExpression());
  }
  @Override public boolean visit(final MethodDeclaration ¢) {
    return !declaredIn(¢) && recurse(¢.getBody());
  }
  @Override public boolean visit(final MethodInvocation ¢) {
    ingore(¢.getName());
    recurse(¢.getExpression());
    return recurse(expose.arguments(¢));
  }
  @Override public boolean visit(final QualifiedName ¢) {
    return recurse(¢.getQualifier());
  }
  @Override public boolean visit(final SimpleName ¢) {
    consider(¢);
    return false;
  }
  @Override public boolean visit(final SuperMethodInvocation ¢) {
    ingore(¢.getName());
    return recurse(expose.arguments(¢));
  }
  @Override public boolean visit(final VariableDeclarationFragment ¢) {
    return !declaredIn(¢) && recurse(¢.getInitializer());
  }

  private final class DeclaredInFields extends ASTVisitor {
    private final ASTNode parent;

    DeclaredInFields(final ASTNode parent) {
      this.parent = parent;
    }
    @Override public boolean visit(final FieldDeclaration ¢) {
      return ¢.getParent() == parent && !hidden() && !declaredIn(¢);
    }
  }
}

class UsesCollectorIgnoreDefinitions extends UsesCollector {
  UsesCollectorIgnoreDefinitions(final List<SimpleName> result, final SimpleName focus) {
    super(result, focus);
  }
  public UsesCollectorIgnoreDefinitions(final UsesCollector ¢) {
    super(¢);
  }
  @Override protected UsesCollectorIgnoreDefinitions clone() {
    return new UsesCollectorIgnoreDefinitions(this);
  }
  @Override public boolean visit(final Assignment ¢) {
    return recurse(right(¢));
  }
  @Override public boolean visit(@SuppressWarnings("unused") final PostfixExpression __) {
    return false;
  }
  @Override public boolean visit(final PrefixExpression it) {
    return !in(it.getOperator(), PrefixExpression.Operator.INCREMENT, PrefixExpression.Operator.DECREMENT);
  }
}