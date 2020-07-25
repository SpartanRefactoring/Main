package il.org.spartan.spartanizer.engine;

import static il.org.spartan.spartanizer.ast.navigate.step.arguments;
import static il.org.spartan.spartanizer.ast.navigate.step.bodyDeclarations;
import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.from;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.parameters;
import static il.org.spartan.spartanizer.ast.navigate.step.receiver;
import static il.org.spartan.spartanizer.ast.navigate.step.right;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import fluent.ly.is;
import il.org.spartan.spartanizer.ast.navigate.wizard;

abstract class HidingDepth extends ScopeManager {
  private int depth;
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
  abstract boolean go(AbstractTypeDeclaration d);
  abstract boolean go(AnonymousClassDeclaration d);
  abstract boolean go(EnhancedForStatement s);
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
    for (ASTNode p = n.getParent(); p != null; p = p.getParent())
      if (unsafe(p)) {
        super.consider(n);
        return;
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
  @Override public boolean visit(final ExpressionMethodReference ¢) {
    ingore(¢.getName());
    recurse(¢.getExpression());
    return false;
  }
  @Override public boolean visit(final MethodInvocation ¢) {
    ingore(name(¢));
    recurse(receiver(¢));
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
    ingore(name(¢));
    return recurse(arguments(¢));
  }
  @Override public boolean visit(final VariableDeclarationFragment ¢) {
    return !declaredIn(¢) && recurse(¢.getInitializer());
  }
  @Override @SuppressWarnings({ "CloneDoesntDeclareCloneNotSupportedException", "CloneDoesntCallSuperClone" }) protected UsesCollector clone() {
    return new UsesCollector(result, focus);
  }
  void consider(final SimpleName candidate) {
    if (hit(candidate))
      result.add(candidate);
  }
  boolean declaredIn(final FieldDeclaration ¢) {
    return fragments(¢).stream().anyMatch(this::declaredIn);
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
  @Override boolean go(final EnhancedForStatement $) {
    final SimpleName name = $.getParameter().getName();
    if (name == focus || !declaredBy(name))
      return true;
    recurse($.getExpression());
    return recurse($.getBody());
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
    d.accept(new ASTVisitor(true) {
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
  private boolean declaredIn(final MethodDeclaration ¢) {
    return parameters(¢).stream().anyMatch(this::declaredIn);
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
    return wizard.eq(¢, focus);
  }
  /** This is where we forget all occurrences of {@link SimpleName} which are
   * not variable names, e.g., class name, function name, field name, etc.
   * @param __ JD */
  private void ingore(@SuppressWarnings("unused") final SimpleName __) {
    // We simply forget the parameter
  }
  private boolean recurse(final Iterable<? extends ASTNode> ¢) {
    ¢.forEach(this::recurse);
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

class StringCollector extends HidingDepth {
  private final List<String> result;
  private final String focus;

  StringCollector(final List<String> result, final String focus) {
    this.result = result;
    this.focus = focus;
  }
  StringCollector(final StringCollector c) {
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
    ingore(name(¢));
    recurse(receiver(¢));
    return recurse(arguments(¢));
  }
  @Override public boolean visit(final QualifiedName ¢) {
    return recurse(¢.getQualifier());
  }
  @Override public boolean visit(final SimpleName ¢) {
    consider(¢ + "");
    return false;
  }
  @Override public boolean visit(final SuperMethodInvocation ¢) {
    ingore(name(¢));
    return recurse(arguments(¢));
  }
  @Override public boolean visit(final VariableDeclarationFragment ¢) {
    return !declaredIn(¢) && recurse(¢.getInitializer());
  }
  @Override @SuppressWarnings({ "CloneDoesntDeclareCloneNotSupportedException", "CloneDoesntCallSuperClone" }) protected StringCollector clone() {
    return new StringCollector(result, focus);
  }
  void consider(final String candidate) {
    if (hit(candidate))
      result.add(candidate);
  }
  boolean declaredIn(final FieldDeclaration ¢) {
    return fragments(¢).stream().anyMatch(this::declaredIn);
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
  @Override boolean go(final EnhancedForStatement $) {
    final String name = $.getParameter().getName() + "";
    if (Objects.equals(name, focus) || !declaredBy(name))
      return true;
    recurse($.getExpression());
    return recurse($.getBody());
  }
  boolean recurse(final ASTNode ¢) {
    if (¢ != null && !hidden())
      ¢.accept(clone());
    return false;
  }
  private boolean declaredBy(final String ¢) {
    if (¢.equals(focus)) {
      result.add(¢);
      return false;
    }
    if (!hit(¢))
      return false;
    hide();
    return true;
  }
  private boolean declaredIn(final AbstractTypeDeclaration d) {
    d.accept(new ASTVisitor(true) {
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
  private boolean declaredIn(final MethodDeclaration ¢) {
    return parameters(¢).stream().anyMatch(this::declaredIn);
  }
  private boolean declaredIn(final SingleVariableDeclaration f) {
    return declaredBy(f.getName() + "");
  }
  private boolean declaredIn(final VariableDeclarationFragment ¢) {
    return declaredBy(¢.getName() + "");
  }
  private void declaresField(final ASTNode ¢) {
    ¢.accept(new DeclaredInFields(¢));
  }
  private boolean hit(final String ¢) {
    return ¢.equals(focus);
  }
  /** This is where we forget all occurrences of {@link SimpleName} which are
   * not variable names, e.g., class name, function name, field name, etc.
   * @param __ JD */
  private void ingore(@SuppressWarnings("unused") final SimpleName __) {
    // We simply forget the parameter
  }
  private boolean recurse(final Iterable<? extends ASTNode> ¢) {
    ¢.forEach(this::recurse);
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
  UsesCollectorIgnoreDefinitions(final UsesCollector c) {
    super(c);
  }
  UsesCollectorIgnoreDefinitions(final List<SimpleName> result, final SimpleName focus) {
    super(result, focus);
  }
  @Override public boolean visit(final Assignment ¢) {
    return recurse(from(¢));
  }
  @Override public boolean visit(final PostfixExpression ¢) {
    return !is.in(¢.getOperator(), PostfixExpression.Operator.INCREMENT, PostfixExpression.Operator.DECREMENT);
  }
  @Override public boolean visit(@SuppressWarnings("unused") final PrefixExpression __) {
    return false;
  }
  @Override @SuppressWarnings("CloneDoesntCallSuperClone") protected UsesCollectorIgnoreDefinitions clone() {
    return new UsesCollectorIgnoreDefinitions(this);
  }
}
