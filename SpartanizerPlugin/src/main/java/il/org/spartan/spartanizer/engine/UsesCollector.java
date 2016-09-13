package il.org.spartan.spartanizer.engine;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.ast.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

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

  @Override public final boolean visit(final AnnotationTypeDeclaration ¢) {
    push();
    return go(¢);
  }

  @Override public final boolean visit(final AnonymousClassDeclaration ¢) {
    push();
    return go(¢);
  }

  @Override public final boolean visit(@SuppressWarnings("unused") final Block __) {
    return push();
  }

  @Override public final boolean visit(final EnhancedForStatement ¢) {
    push();
    return go(¢);
  }

  @Override public final boolean visit(final EnumDeclaration ¢) {
    push();
    return go(¢);
  }

  @Override public final boolean visit(@SuppressWarnings("unused") final ForStatement __) {
    return push();
  }

  @Override public final boolean visit(final TypeDeclaration ¢) {
    push();
    return go(¢);
  }

  abstract boolean go(final AbstractTypeDeclaration d);

  abstract boolean go(final AnonymousClassDeclaration d);

  abstract boolean go(final EnhancedForStatement s);

  abstract void pop();

  abstract boolean push();
}

class UnsafeUsesCollector extends UsesCollector {
  private static boolean unsafe(final ASTNode ¢) {
    return ¢ instanceof ClassInstanceCreation;
  }

  UnsafeUsesCollector(final List<SimpleName> result, final SimpleName focus) {
    super(result, focus);
  }

  @Override void consider(final SimpleName n) {
    ASTNode p = n.getParent();
    while (p != null) {
      if (unsafe(p)) {
        super.consider(n);
        return;
      }
      p = p.getParent();
    }
  }
}

class UsesCollector extends HidingDepth {
  private final List<SimpleName> result;
  private final SimpleName focus;

  UsesCollector(final List<SimpleName> result, final SimpleName focus) {
    this.result = result;
    this.focus = focus;
  }

  UsesCollector(final UsesCollector c) {
    this(c.result, c.focus);
  }

  @Override public boolean preVisit2(final ASTNode ¢) {
    return !hidden() && !(¢ instanceof Type);
  }

  @Override public boolean visit(final CastExpression ¢) {
    return recurse(right(¢));
  }

  @Override public boolean visit(final FieldAccess n) {
    return recurse(n.getExpression());
  }

  @Override public boolean visit(final MethodDeclaration ¢) {
    return !declaredIn(¢) && recurse(¢.getBody());
  }

  @Override public boolean visit(final MethodInvocation ¢) {
    ingore(step.name(¢));
    recurse(step.receiver(¢));
    return recurse(arguments(¢));
  }

  @Override public boolean visit(final QualifiedName ¢) {
    return recurse(¢.getQualifier());
  }

  @Override public boolean visit(final SimpleName ¢) {
    consider(¢);
    return false;
  }

  @Override public boolean visit(final SuperMethodInvocation ¢) {
    ingore(step.name(¢));
    return recurse(arguments(¢));
  }

  @Override public boolean visit(final VariableDeclarationFragment ¢) {
    return !declaredIn(¢) && recurse(¢.getInitializer());
  }

  @Override protected UsesCollector clone() {
    return new UsesCollector(result, focus);
  }

  void consider(final SimpleName candidate) {
    if (hit(candidate))
      result.add(candidate);
  }

  boolean declaredIn(final FieldDeclaration d) {
    for (final Object o : d.fragments())
      if (declaredIn((VariableDeclarationFragment) o))
        return true;
    return false;
  }

  @Override boolean go(final AbstractTypeDeclaration ¢) {
    ingore(¢.getName());
    return !declaredIn(¢) && recurse(bodyDeclarations(¢));
  }

  boolean go(final AnnotationTypeDeclaration ¢) {
    ingore(¢.getName());
    return !declaredIn(¢) && recurse(bodyDeclarations(¢));
  }

  @Override boolean go(final AnonymousClassDeclaration ¢) {
    return !declaredIn(¢) && recurse(bodyDeclarations(¢));
  }

  @Override boolean go(final EnhancedForStatement s) {
    final SimpleName name = s.getParameter().getName();
    if (name == focus || !declaredBy(name))
      return true;
    recurse(s.getExpression());
    return recurse(s.getBody());
  }

  boolean recurse(final ASTNode ¢) {
    if (¢ != null && !hidden())
      ¢.accept(clone());
    return false;
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

  private boolean declaredIn(final MethodDeclaration d) {
    for (final Object o : d.parameters())
      if (declaredIn((SingleVariableDeclaration) o))
        return true;
    return false;
  }

  private boolean declaredIn(final SingleVariableDeclaration f) {
    return declaredBy(f.getName());
  }

  private boolean declaredIn(final VariableDeclarationFragment ¢) {
    return declaredBy(¢.getName());
  }

  private void declaresField(final ASTNode ¢) {
    ¢.accept(new DeclaredInFields(¢));
  }

  private boolean hit(final SimpleName ¢) {
    return wizard.same(¢, focus);
  }

  /** This is where we ignore all occurrences of {@link SimpleName} which are
   * not variable names, e.g., class name, function name, field name, etc.
   * @param __ JD */
  private void ingore(@SuppressWarnings("unused") final SimpleName __) {
    // We simply ignore the parameter
  }

  private boolean recurse(final List<? extends ASTNode> ns) {
    for (final ASTNode n : ns)
      recurse(n);
    return false;
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
  public UsesCollectorIgnoreDefinitions(final UsesCollector c) {
    super(c);
  }

  UsesCollectorIgnoreDefinitions(final List<SimpleName> result, final SimpleName focus) {
    super(result, focus);
  }

  @Override public boolean visit(final Assignment ¢) {
    return recurse(from(¢));
  }

  @Override public boolean visit(final PostfixExpression it) {
    return !in(it.getOperator(), PostfixExpression.Operator.INCREMENT, PostfixExpression.Operator.DECREMENT);
  }

  // changed Prefix and Postfix on the two next visitors.
  @Override public boolean visit(@SuppressWarnings("unused") final PrefixExpression __) {
    return false;
  }

  @Override protected UsesCollectorIgnoreDefinitions clone() {
    return new UsesCollectorIgnoreDefinitions(this);
  }
}
