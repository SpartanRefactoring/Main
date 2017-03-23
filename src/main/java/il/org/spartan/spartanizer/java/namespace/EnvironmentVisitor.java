package il.org.spartan.spartanizer.java.namespace;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

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
  @NotNull String scopePath = "";

  EnvironmentVisitor(final LinkedHashSet<Entry<String, Binding>> $) {
    this.$ = $;
  }

  /** As of JSL3, AnonymousClassDeclaration's parent can be either
   * ClassInstanceCreation or EnumConstantDeclaration */
  @NotNull static String anonymousClassDeclarationParentName(@NotNull final AnonymousClassDeclaration ¢) {
    final ASTNode $ = ¢.getParent();
    if ($ instanceof ClassInstanceCreation)
      return az.classInstanceCreation($).getType() + "";
    assert $ instanceof EnumConstantDeclaration;
    return az.enumConstantDeclaration($).getName() + "";
  }

  @NotNull Entry<String, Binding> convertToEntry(@NotNull final AnnotationTypeMemberDeclaration ¢) {
    return new MapEntry<>(fullName(¢.getName()), createInformation(¢));
  }

  @NotNull @SuppressWarnings("hiding") Collection<Entry<String, Binding>> convertToEntry(@NotNull final FieldDeclaration d) {
    @NotNull final Collection<Entry<String, Binding>> $ = new ArrayList<>();
    @NotNull final type t = type.baptize(trivia.condense(d.getType()));
    $.addAll(fragments(d).stream().map(λ -> new MapEntry<>(fullName(λ.getName()), createInformation(λ, t))).collect(toList()));
    return $;
  }

  @NotNull Entry<String, Binding> convertToEntry(@NotNull final SingleVariableDeclaration ¢) {
    return new MapEntry<>(fullName(¢.getName()), createInformation(¢));
  }

  @NotNull @SuppressWarnings("hiding") Collection<Entry<String, Binding>> convertToEntry(@NotNull final VariableDeclarationExpression x) {
    @NotNull final Collection<Entry<String, Binding>> $ = new ArrayList<>();
    @NotNull final type t = type.baptize(trivia.condense(x.getType()));
    $.addAll(fragments(x).stream().map(λ -> new MapEntry<>(fullName(λ.getName()), createInformation(λ, t))).collect(toList()));
    return $;
  }

  @NotNull @SuppressWarnings("hiding") Collection<Entry<String, Binding>> convertToEntry(@NotNull final VariableDeclarationStatement s) {
    @NotNull final Collection<Entry<String, Binding>> $ = new ArrayList<>();
    @NotNull final type t = type.baptize(trivia.condense(s.getType()));
    $.addAll(fragments(s).stream().map(λ -> new MapEntry<>(fullName(λ.getName()), createInformation(λ, t))).collect(toList()));
    return $;
  }

  @NotNull Binding createInformation(@NotNull final AnnotationTypeMemberDeclaration ¢) {
    return new Binding(¢.getParent(), getHidden(fullName(¢.getName())), ¢, type.baptize(trivia.condense(¢.getType())));
  }

  @NotNull Binding createInformation(@NotNull final SingleVariableDeclaration ¢) {
    return new Binding(¢.getParent(), getHidden(fullName(¢.getName())), ¢, type.baptize(trivia.condense(¢.getType())));
  }

  @Nullable Binding createInformation(@NotNull final VariableDeclarationFragment ¢, final type t) {
    // VariableDeclarationFragment, that comes from either FieldDeclaration,
    // VariableDeclarationStatement or VariableDeclarationExpression,
    // does not contain its type. Hence, the type is sent from the parent in
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

  @NotNull @SuppressWarnings("hiding") String fullName(final SimpleName $) {
    return scopePath + "." + $;
  }

  static Binding get(@NotNull final Collection<Entry<String, Binding>> ss, @NotNull final String s) {
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
  Binding getHidden(@NotNull final String ¢) {
    for (@NotNull String s = parentNameScope(¢); s != null && !s.isEmpty(); s = parentNameScope(s)) {
      final Binding i = get($, s + "." + ¢.substring(¢.lastIndexOf(".") + 1));
      if (i != null)
        return i;
    }
    return null;
  }

  /** Similar to statementOrderAmongTypeInParent, {@link CatchClause}s only */
  static int orderOfCatchInTryParent(@NotNull final CatchClause c) {
    assert c.getParent() instanceof TryStatement;
    int $ = 0;
    for (final CatchClause ¢ : catchClauses((TryStatement) c.getParent())) {
      if (¢ == c)
        break;
      ++$;
    }
    return $;
  }

  @NotNull static String parentNameScope(@Nullable final String ¢) {
    return ¢ == null || ¢.isEmpty() ? "" : ¢.substring(0, ¢.lastIndexOf("."));
  }

  void restoreScopeName() {
    scopePath = parentNameScope(scopePath);
  }

  /** Order of the searched {@link Statement} in its parent {@link ASTNode},
   * among nodes of the same kind. Zero based.
   * @param s
   * @return The nodes index, according to order of appearance, among nodes of
   *         the same type. [[SuppressWarningsSpartan]] */
  static int statementOrderAmongTypeInParent(@NotNull final Statement s) {
    // extract.statements wouldn't work here - we need a shallow extract,
    // not a deep one.
    final ASTNode n = s.getParent();
    if (n == null || !(n instanceof Block) && !(n instanceof SwitchStatement))
      return 0;
    int $ = 0;
    for (@NotNull final Statement ¢ : iz.block(n) ? statements(az.block(n)) : statements(az.switchStatement(n))) {
      // This is intentionally '==' and not equals, meaning the exact same
      // Statement,
      // not just equivalence.
      if (¢ == s)
        break;
      if (¢.getNodeType() == s.getNodeType())
        ++$;
    }
    return $;
  }

  @Override public boolean visit(@NotNull final AnnotationTypeDeclaration ¢) {
    scopePath += "." + ¢.getName();
    return true;
  }

  @Override public boolean visit(@NotNull final AnnotationTypeMemberDeclaration ¢) {
    $.add(convertToEntry(¢));
    return true;
  }

  @Override public boolean visit(@NotNull final AnonymousClassDeclaration ¢) {
    scopePath += ".#anon_extends_" + anonymousClassDeclarationParentName(¢);
    return true;
  }

  @Override public boolean visit(@NotNull final Block ¢) {
    scopePath += ".#block" + statementOrderAmongTypeInParent(¢);
    return true;
  }

  @Override public boolean visit(@NotNull final CatchClause ¢) {
    scopePath += ".#catch" + orderOfCatchInTryParent(¢);
    return true;
  }

  @Override public boolean visit(@NotNull final DoStatement ¢) {
    scopePath += ".#do" + statementOrderAmongTypeInParent(¢);
    return true;
  }

  @Override public boolean visit(@NotNull final EnhancedForStatement ¢) {
    scopePath += ".#enhancedFor" + statementOrderAmongTypeInParent(¢);
    return true;
  }

  @Override public boolean visit(@NotNull final EnumConstantDeclaration ¢) {
    scopePath += "." + ¢.getName();
    return true;
  }

  @Override public boolean visit(@NotNull final EnumDeclaration ¢) {
    scopePath += "." + ¢.getName();
    return true;
  }

  @Override public boolean visit(@NotNull final FieldDeclaration ¢) {
    $.addAll(convertToEntry(¢));
    return true;
  }

  @Override public boolean visit(@NotNull final ForStatement ¢) {
    scopePath += ".#for" + statementOrderAmongTypeInParent(¢);
    return true;
  }

  @Override public boolean visit(@NotNull final IfStatement ¢) {
    scopePath += ".#if" + statementOrderAmongTypeInParent(¢);
    return true;
  }

  @Override public boolean visit(@NotNull final MethodDeclaration ¢) {
    scopePath += "." + ¢.getName();
    return true;
  }

  @Override public boolean visit(@NotNull final SingleVariableDeclaration ¢) {
    $.add(convertToEntry(¢));
    return true;
  }

  @Override public boolean visit(@NotNull final SwitchStatement ¢) {
    scopePath += ".#switch" + statementOrderAmongTypeInParent(¢);
    return true;
  }

  @Override public boolean visit(@NotNull final TryStatement ¢) {
    scopePath += ".#try" + statementOrderAmongTypeInParent(¢);
    return true;
  }

  @Override public boolean visit(@NotNull final TypeDeclaration ¢) {
    scopePath += "." + ¢.getName();
    return true;
  }

  @Override public boolean visit(@NotNull final VariableDeclarationExpression ¢) {
    $.addAll(convertToEntry(¢));
    return true;
  }

  @Override public boolean visit(@NotNull final VariableDeclarationStatement ¢) {
    $.addAll(convertToEntry(¢));
    return true;
  }

  @Override public boolean visit(@NotNull final WhileStatement ¢) {
    scopePath += ".#while" + statementOrderAmongTypeInParent(¢);
    return true;
  }
}