package il.org.spartan.spartanizer.utils.tdd;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since Oct 31, 2016 */
public enum determineIf {
  ;
  /** see issue #718 for more details
   * @author Amir Sagiv
   * @author Oren Afek
   * @since 16-11-02
   * @param d
   * @return true iff the method have at least 3 parameters and defines more
   *         than 5 variables */
  public static boolean loaded(final MethodDeclaration d) {
    final int $ = 3, expectedNoOfVars = 5;
    if (d == null)
      return false;
    final Int declaredVarsCounter = new Int();
    declaredVarsCounter.inner = 0;
    d.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") final VariableDeclarationFragment __) {
        ++declaredVarsCounter.inner;
        return true;
      }
    });
    return d.parameters().size() >= $ && declaredVarsCounter.inner >= expectedNoOfVars;
  }

  // For you to implement! Let's TDD and get it on!
  /** see issue #716 for more details
   * @author Ron Gatenio
   * @author Roy Shchory
   * @since 16-11-02
   * @param d
   * @return true iff the method has at least 10 statements */
  public static boolean hasManyStatements(final MethodDeclaration d) {
    if (d == null)
      return false;
    final Int $ = new Int();
    $.inner = 0;
    d.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (iz.statement(¢))
          ++$.inner;
      }
    });
    return $.inner >= 11;
  }

  /** see issue #714 for more details
   * @author Arthur Sapozhnikov
   * @author Assaf Lustig
   * @author Dan Abramovich
   * @since 16-11-02
   * @param m
   * @return true iff the class contains only final fields */
  public static boolean isImmutable(final TypeDeclaration m) {
    return m == null || as.list(fields(m)).stream().allMatch(f -> modifiers(f).stream().anyMatch(λ -> ((Modifier) λ).isFinal()));
  }

  // For you to implement! Let's TDD and get it on!
  /** see issue #719 for more details
   * @author YaelAmitay
   * @author koralchapnik
   * @since 16-11-04
   * @param d
   * @param x
   * @return true iff the method defines at least x variables. */
  public static boolean definesManyVariables(final MethodDeclaration d, final int x) {
    if (d == null)
      return false;
    final Int $ = new Int();
    $.inner = 0;
    d.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") final VariableDeclarationFragment ¢) {
        ++$.inner;
        return true;
      }
    });
    return $.inner >= x;
  }

  // For you to implement! Let's TDD and get it on!
  /** see issue #717 for more details
   * @author Lidia Piatigorski
   * @author Nikita Dizhur
   * @author Alex V.
   * @since 16-11-05
   * @param d
   * @return true iff the method has an inner block containing at least 5
   *         statements. */
  public static boolean hasBigBlock(final MethodDeclaration ¢) {
    return ¢ != null && ¢.getBody() != null && ¢.getBody().statements().size() >= 5;
  }

  /** see issue #710 for more details
   * @author David Cohen
   * @author Shahar Yair
   * @author Zahi Mizrahi
   * @since 16-11-06
   * @param d
   * @return returns true iff the method contains a return null statement . */
  public static boolean returnsNull(final MethodDeclaration mDec) {
    if (mDec == null)
      return false;
    final List<ReturnStatement> $ = new ArrayList<>();
    mDec.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") final LambdaExpression e1) {
        return false;
      }

      @Override public boolean visit(@SuppressWarnings("unused") final AnonymousClassDeclaration anonymClassDec) {
        return false;
      }

      @Override public boolean visit(@SuppressWarnings("unused") final TypeDeclaration __) {
        return false;
      }

      @Override public boolean visit(final ReturnStatement ¢) {
        $.add(¢);
        return true;
      }
    });
    return $.stream().anyMatch(λ -> λ.getClass().equals(ReturnStatement.class) && λ.getExpression().getClass().equals(NullLiteral.class));
  }

  /** see issue #774 for more details
   * @author Amit Ohayon
   * @author Yosef Raisman
   * @author Entony Lekhtman
   * @since 16-11-06
   * @param n
   * @param name
   * @return returns true iff the name is used in the node as a Name. */
  public static boolean uses(final ASTNode n, final String name) {
    if (n == null)
      return false;
    final Bool $ = new Bool();
    $.inner = false;
    n.accept(new ASTVisitor() {
      void innerVisit(final Name node) {
        $.inner = node.getFullyQualifiedName().equals(name);
      }

      @Override public boolean visit(final QualifiedName node) {
        if (!$.inner)
          innerVisit(node);
        return !$.inner;
      }

      @Override public boolean visit(final SimpleName node) {
        if (!$.inner)
          innerVisit(node);
        return !$.inner;
      }
    });
    return $.inner;
  }
}
