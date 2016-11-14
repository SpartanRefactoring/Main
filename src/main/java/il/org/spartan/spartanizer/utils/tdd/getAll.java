package il.org.spartan.spartanizer.utils.tdd;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @author Dor Ma'ayan
 * @author Raviv Rachmiel
 * @author Kfir Marx
 * @author Omri Ben- Shmuel
 * @author Ward Mattar
 * @author Vivian Shehadeh
 * @since Oct 31, 2016 */
public enum getAll {
  ;
  /** Get all the parameters that the method invocation depends on
   * @author Vivian Shehadeh
   * @author Ward Mattar
   * @param ¢ is a MethodInvocation
   * @return List of the names of the methods */
  public static Set<String> invocations(final MethodInvocation ¢) {
    if (¢ == null)
      return null;
    final Set<String> $ = new TreeSet<>();
    ¢.accept(new ASTVisitor() {
      @Override public boolean visit(final SimpleName ¢¢) {
        if ((!iz.methodInvocation(step.parent(¢¢)) || !(step.name(az.methodInvocation(step.parent(¢¢))) + "").equals(¢¢ + ""))
            && (!iz.methodDeclaration(step.parent(¢¢)) || !(step.name(az.methodDeclaration(step.parent(¢¢))) + "").equals(¢¢ + "")))
          $.add(¢¢ + "");
        return true;
      }
    });
    return $;
  }

  /** Get all the methods invoked in m
   * @author Dor Ma'ayan
   * @param d JD
   * @return List of the names of the methods */
  public static Set<String> invocations(final MethodDeclaration ¢) {
    if (¢ == null)
      return null;
    final Set<String> $ = new TreeSet<>();
    if (¢.getBody().statements().isEmpty())
      return $;
    ¢.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodInvocation ¢¢) {
        $.add(¢¢.getName() + "");
        return true;
      }
    });
    return $;
  }

  /** Get list of names in a Block
   * @author Raviv Rachmiel
   * @author Kfir Marx
   * @param ¢ Block
   * @return List of the names in the block */
  public static List<Name> names(final Block b) {
    if (b == null)
      return null;
    final List<Name> $ = new ArrayList<>();
    b.accept(new ASTVisitor() {
      @Override public boolean visit(final SimpleName ¢) {
        $.add(¢);
        return true;
      }
    });
    return $;
  }

  /** returns a list of all instances of expressions at given method
   * @author Koby Ben Shimol
   * @author Yuval Simon
   * @since 16-11-01 */
  public static List<InstanceofExpression> instanceofs(final MethodDeclaration d) {
    if (d == null)
      return null;
    final List<InstanceofExpression> $ = new ArrayList<>();
    d.accept(new ASTVisitor() {
      @Override public boolean visit(final InstanceofExpression node) {
        $.add(node);
        return true;
      }
    });
    return $;
  }

  /** Takes a single parameter d, which is a MethodDeclaration. Returns a
   * List<CastExpression> which is all casts in d.
   * @param d a MethodDeclaration
   * @author Inbal Matityahu
   * @author Or Troyaner
   * @author Tom Nof */
  public static List<CastExpression> casts(final MethodDeclaration d) {
    if (d == null)
      return null;
    final List<CastExpression> $ = new ArrayList<>();
    d.accept(new ASTVisitor() {
      @Override public boolean visit(final CastExpression node) {
        $.add(node);
        return true;
      }
    });
    return $;
  }

  /** Takes a single parameter, which is an MethodDeclaration return a
   * List<VariableDeclaration> which is all String variable declarations in m
   * @param d a MethodDeclaration
   * @author Alexander Kaplan
   * @author Ariel Kolikant */
  public static List<VariableDeclaration> stringVariables(final MethodDeclaration d) {
    final List<VariableDeclaration> $ = new ArrayList<>();
    if (d == null)
      return null;
    d.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (¢ instanceof SingleVariableDeclaration && "String".equals(((SingleVariableDeclaration) ¢).getType() + ""))
          $.add((SingleVariableDeclaration) ¢);
        super.preVisit(¢);
      }
    });
    return $;
  }

  /** Takes a single parameter, which is a TypeDecleration returns a list of
   * public fields for this class (by fields' names)
   * @param ¢ TypeDecleration
   * @author Inbal Zukerman
   * @author Elia Traore */
  public static List<String> publicFields(final TypeDeclaration ¢) {
    if (¢ == null)
      return null;
    final List<String> $ = new ArrayList<>();
    ¢.accept(publicFieldsCollector($));
    return $;
  }

  private static ASTVisitor publicFieldsCollector(final List<String> $) {
    return new ASTVisitor() {
      @Override public boolean visit(final FieldDeclaration d) {
        if (iz.public¢(d))
          for (final VariableDeclarationFragment ¢ : fragments(d))
            $.add(step.name(¢) + "");
        return true;
      }
    };
  }

  /** Takes a single CompilationUnit parameter, returns a list of method
   * declaration within that compilation unit
   * @param CompilationUnit
   * @author RoeiRaz
   * @author RoeyMaor */
  public static List<MethodDeclaration> methods(final CompilationUnit u) {
    if (u == null)
      return null;
    final List<MethodDeclaration> $ = new ArrayList<>();
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodDeclaration ¢) {
        $.add(¢);
        return super.visit(¢);
      }
    });
    return $;
  }

  /** takes a single parameter, which is a TypeDeclaration. returns a list of
   * private fields for this class (by fields' names)
   * @param TypeDeclaration
   * @author yonzarecki
   * @author rodedzats
   * @author zivizhar */
  public static List<String> privateFields(final TypeDeclaration ¢) {
    final List<String> $ = new ArrayList<>();
    if (¢ == null)
      return $;
    ¢.accept(new ASTVisitor() { // traverse all FieldDeclaration
      @Override public boolean visit(final FieldDeclaration d) {
        if (d.getModifiers() == org.eclipse.jdt.core.dom.Modifier.PRIVATE)
          for (final VariableDeclarationFragment df : fragments(d))
            $.add(df.getName().getIdentifier());
        return true;
      }
    });
    return $;
  }
}