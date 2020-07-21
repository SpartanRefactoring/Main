package il.org.spartan.spartanizer.java.namespace;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.utils.*;

/** Three groups of visitors here: 1. Non-declarations with a name. 2.
 * Non-declarations without a name. 3. Actual Declarations. First two groups are
 * those in which variable declarations can be made. Since we want to be able to
 * distinguish variables of different scopes, but with, perhaps, equal names,
 * need to keep the scope. The full scope might contain things that do not have
 * a name, hence the need keep to visit ASTNodes without a name such as
 * {@link Block}s, {@link ForStatement}s, etc. Since there can be more than one
 * such node in a parent, they are distinguished by their order of appearance.
 * The third group is the one in which actual addition to the Environment is
 * made.
 * @author Alex + Dan
 * @since 2016 */
@SuppressWarnings("unused")
final class EnvironmentVisitor extends ASTVisitor {
  private final LinkedHashSet<Entry<String, Binding>> $;
  // Holds the current scope full name (Path).
  String scopePath = "";

  EnvironmentVisitor(final LinkedHashSet<Entry<String, Binding>> $) {
    this.$ = $;
  }
  /** As of JSL3, AnonymousClassDeclaration's parent can be either
   * ClassInstanceCreation or EnumConstantDeclaration */
  static String anonymousClassDeclarationParentName(final AnonymousClassDeclaration ¢) {
    final ASTNode ret = ¢.getParent();
    if (ret instanceof ClassInstanceCreation)
      return az.classInstanceCreation(ret).getType() + "";
    assert ret instanceof EnumConstantDeclaration;
    return az.enumConstantDeclaration(ret).getName() + "";
  }
  Entry<String, Binding> convertToEntry(final AnnotationTypeMemberDeclaration ¢) {
    return new MapEntry<>(fullName(¢.getName()), createInformation(¢));
  }
   Collection<Entry<String, Binding>> convertToEntry(final FieldDeclaration d) {
    final Collection<Entry<String, Binding>> ret = an.empty.list();
    final type t = type.baptize(Trivia.condense(d.getType()));
    ret.addAll(fragments(d).stream().map(λ -> new MapEntry<>(fullName(λ.getName()), createInformation(λ, t))).collect(toList()));
    return ret;
  }
  Entry<String, Binding> convertToEntry(final SingleVariableDeclaration ¢) {
    return new MapEntry<>(fullName(¢.getName()), createInformation(¢));
  }
   Collection<Entry<String, Binding>> convertToEntry(final VariableDeclarationExpression x) {
    final Collection<Entry<String, Binding>> ret = an.empty.list();
    final type t = type.baptize(Trivia.condense(x.getType()));
    ret.addAll(fragments(x).stream().map(λ -> new MapEntry<>(fullName(λ.getName()), createInformation(λ, t))).collect(toList()));
    return ret;
  }
   Collection<Entry<String, Binding>> convertToEntry(final VariableDeclarationStatement s) {
    final Collection<Entry<String, Binding>> ret = an.empty.list();
    final type t = type.baptize(Trivia.condense(s.getType()));
    ret.addAll(fragments(s).stream().map(λ -> new MapEntry<>(fullName(λ.getName()), createInformation(λ, t))).collect(toList()));
    return ret;
  }
  Binding createInformation(final AnnotationTypeMemberDeclaration ¢) {
    return new Binding(¢.getParent(), getHidden(fullName(¢.getName())), ¢, type.baptize(Trivia.condense(¢.getType())));
  }
  Binding createInformation(final SingleVariableDeclaration ¢) {
    return new Binding(¢.getParent(), getHidden(fullName(¢.getName())), ¢, type.baptize(Trivia.condense(¢.getType())));
  }
  Binding createInformation(final VariableDeclarationFragment ¢, final type t) {
    // VariableDeclarationFragment, that comes from either FieldDeclaration,
    // VariableDeclarationStatement or VariableDeclarationExpression,
    // does not contain its __. Hence, the __ is sent from the parent in
    // the convertToEntry calls.
    return new Binding(¢.getParent(), getHidden(fullName(¢.getName())), ¢, t);
  }
  // Everything besides the actual variable declaration was visited for
  // nameScope reasons. Once their visit is over, the nameScope needs to be
  // restored.
  @Override public void endVisit(final AnnotationTypeDeclaration __) {
    restoreScopeName();
  }
  @Override public void endVisit(final AnonymousClassDeclaration __) {
    restoreScopeName();
  }
  @Override public void endVisit(final Block __) {
    restoreScopeName();
  }
  @Override public void endVisit(final CatchClause __) {
    restoreScopeName();
  }
  @Override public void endVisit(final DoStatement __) {
    restoreScopeName();
  }
  @Override public void endVisit(final EnhancedForStatement __) {
    restoreScopeName();
  }
  @Override public void endVisit(final EnumConstantDeclaration __) {
    restoreScopeName();
  }
  @Override public void endVisit(final EnumDeclaration __) {
    restoreScopeName();
  }
  @Override public void endVisit(final ForStatement __) {
    restoreScopeName();
  }
  @Override public void endVisit(final IfStatement __) {
    restoreScopeName();
  }
  @Override public void endVisit(final MethodDeclaration __) {
    restoreScopeName();
  }
  @Override public void endVisit(final SwitchStatement __) {
    restoreScopeName();
  }
  @Override public void endVisit(final TryStatement __) {
    restoreScopeName();
  }
  @Override public void endVisit(final TypeDeclaration __) {
    restoreScopeName();
  }
  @Override public void endVisit(final WhileStatement __) {
    restoreScopeName();
  }
  @SuppressWarnings("hiding") String fullName(final SimpleName $) {
    return scopePath + "." + $;
  }
  static Binding get(final Collection<Entry<String, Binding>> ss, final String s) {
    return ss.stream().filter(λ -> s.equals(λ.getKey())).map(Entry::getValue).findFirst().orElse(null);
  }
  /** Returns the {@link Binding} of the declaration the current declaration is
   * hiding.
   * @param ¢ the fullName of the declaration.
   * @return The hidden node's Information */
  /* Implementation notes: Should go over result set, and search for declaration
   * which shares the same variable name in the parents. Should return the
   * closest match: for example, if we search for a match to .A.B.C.x, and
   * result set contains .A.B.x and .A.x, we should return .A.B.x.
   *
   * If a result is found in the result set, return said result.
   *
   * To consider: what if said hidden declaration will not appear in
   * 'declaresDown', but will appear in 'declaresUp'? Should we search for it in
   * 'declaresUp' result set? Should we leave the result as it is? I (Dan
   * Greenstein) lean towards searching 'declaresUp'. Current implementation
   * only searches declaresDown.
   *
   * If no match is found, return null. */
  Binding getHidden(final String ¢) {
    for (String s = parentNameScope(¢); s != null && !s.isEmpty(); s = parentNameScope(s)) {
      final Binding ret = get($, s + "." + ¢.substring(¢.lastIndexOf(".") + 1));
      if (ret != null)
        return ret;
    }
    return null;
  }
  /** Similar to statementOrderAmongTypeInParent, {@link CatchClause}s only */
  static int orderOfCatchInTryParent(final CatchClause c) {
    assert c.getParent() instanceof TryStatement;
    int ret = 0;
    for (final CatchClause ¢ : catchClauses((TryStatement) c.getParent())) {
      if (¢ == c)
        break;
      ++ret;
    }
    return ret;
  }
  static String parentNameScope(final String ¢) {
    return ¢ == null || ¢.isEmpty() ? "" : ¢.substring(0, ¢.lastIndexOf("."));
  }
  void restoreScopeName() {
    scopePath = parentNameScope(scopePath);
  }
  /** Order of the searched {@link Statement} in its parent {@link ASTNode},
   * among nodes of the same kind. zero based.
   * @param s
   * @return The nodes index, according to order of appearance, among nodes
   *         ofthe same __. [[SuppressWarningsSpartan]] */
  static int statementOrderAmongTypeInParent(final Statement s) {
    // extract.statements wouldn't work here - we need a shallow extract,
    // not a deep one.
    int $ = 0;
    final List<Statement> statements = statements(s.getParent());
    if (statements == null)
      return $;
    for (final Statement ¢ : statements) {
      // This is intentionally '==' and not equals, meaning the exact same
      // statement,not just equivalence.
      if (¢ == s)
        break;
      if (¢.getNodeType() == s.getNodeType())
        ++$;
    }
    return $;
  }
  @Override public boolean visit(final AnnotationTypeDeclaration ¢) {
    scopePath += "." + ¢.getName();
    return true;
  }
  @Override public boolean visit(final AnnotationTypeMemberDeclaration ¢) {
    $.add(convertToEntry(¢));
    return true;
  }
  @Override public boolean visit(final AnonymousClassDeclaration ¢) {
    scopePath += ".#anon_extends_" + anonymousClassDeclarationParentName(¢);
    return true;
  }
  @Override public boolean visit(final Block ¢) {
    scopePath += ".#block" + statementOrderAmongTypeInParent(¢);
    return true;
  }
  @Override public boolean visit(final CatchClause ¢) {
    scopePath += ".#catch" + orderOfCatchInTryParent(¢);
    return true;
  }
  @Override public boolean visit(final DoStatement ¢) {
    scopePath += ".#do" + statementOrderAmongTypeInParent(¢);
    return true;
  }
  @Override public boolean visit(final EnhancedForStatement ¢) {
    scopePath += ".#enhancedFor" + statementOrderAmongTypeInParent(¢);
    return true;
  }
  @Override public boolean visit(final EnumConstantDeclaration ¢) {
    scopePath += "." + ¢.getName();
    return true;
  }
  @Override public boolean visit(final EnumDeclaration ¢) {
    scopePath += "." + ¢.getName();
    return true;
  }
  @Override public boolean visit(final FieldDeclaration ¢) {
    $.addAll(convertToEntry(¢));
    return true;
  }
  @Override public boolean visit(final ForStatement ¢) {
    scopePath += ".#for" + statementOrderAmongTypeInParent(¢);
    return true;
  }
  @Override public boolean visit(final IfStatement ¢) {
    scopePath += ".#if" + statementOrderAmongTypeInParent(¢);
    return true;
  }
  @Override public boolean visit(final MethodDeclaration ¢) {
    scopePath += "." + ¢.getName();
    return true;
  }
  @Override public boolean visit(final SingleVariableDeclaration ¢) {
    $.add(convertToEntry(¢));
    return true;
  }
  @Override public boolean visit(final SwitchStatement ¢) {
    scopePath += ".#switch" + statementOrderAmongTypeInParent(¢);
    return true;
  }
  @Override public boolean visit(final TryStatement ¢) {
    scopePath += ".#try" + statementOrderAmongTypeInParent(¢);
    return true;
  }
  @Override public boolean visit(final TypeDeclaration ¢) {
    scopePath += "." + ¢.getName();
    return true;
  }
  @Override public boolean visit(final VariableDeclarationExpression ¢) {
    $.addAll(convertToEntry(¢));
    return true;
  }
  @Override public boolean visit(final VariableDeclarationStatement ¢) {
    $.addAll(convertToEntry(¢));
    return true;
  }
  @Override public boolean visit(final WhileStatement ¢) {
    scopePath += ".#while" + statementOrderAmongTypeInParent(¢);
    return true;
  }
}