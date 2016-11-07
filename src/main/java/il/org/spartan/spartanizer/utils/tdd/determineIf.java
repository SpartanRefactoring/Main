package il.org.spartan.spartanizer.utils.tdd;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
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
    final int expectedNoOfParams = 3;
    final int expectedNoOfVars = 5;
    if (d == null)
      return false;
    final Int declaredVarsCounter = new Int();
    declaredVarsCounter.inner = 0;
    d.accept(new ASTVisitor() {
      @Override public boolean visit(@SuppressWarnings("unused") VariableDeclarationFragment __) {
        ++declaredVarsCounter.inner;
        return true;
      }
    });
    return d.parameters().size() >= expectedNoOfParams && declaredVarsCounter.inner >= expectedNoOfVars;
  }

  // For you to implement! Let's TDD and get it on!
  /** see issue #716 for more details
   * @author Ron Gatenio
   * @author Roy Shchory
   * @since 16-11-02
   * @param ¢
   * @return true iff the method has at least 10 statements */
  public static boolean hasManyStatements(@SuppressWarnings("unused") final MethodDeclaration ¢) {
    return ¢ != null && true;
  }

  /** see issue #714 for more details
   * @author Arthur Sapozhnikov
   * @author Assaf Lustig
   * @author Dan Abramovich
   * @since 16-11-02
   * @param m
   * @return true iff the class contains only final fields */
  public static boolean isImmutable(@SuppressWarnings("unused") final TypeDeclaration m) {
    return true;
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
  public static boolean returnsNull(MethodDeclaration mDec) {
    if (mDec == null)
      return false;

 
     List<ReturnStatement> statementList = new ArrayList<>();
    mDec.accept (new ASTVisitor() {
       @Override public boolean visit ( @SuppressWarnings("unused") LambdaExpression e1) {
         
         return false;
        }
       @Override public boolean visit ( @SuppressWarnings("unused") AnonymousClassDeclaration anonymClassDec) {
         
         return false;
        }
       @Override public boolean visit ( @SuppressWarnings("unused") TypeDeclaration t) {
         
         return false;
        }
       @Override public boolean visit (ReturnStatement ¢) {
          statementList.add (¢);
          return true;
        }
      });
      for(ReturnStatement ¢ : statementList)
        if (¢.getClass().equals(ReturnStatement.class) && ¢.getExpression().getClass().equals(NullLiteral.class))
          return true;  
    return false;
  }

  /** see issue #774 for more details
   * @author Amit Ohayon
   * @author Yosef Raisman
   * @author Entony Lekhtman
   * @since 16-11-06
   * @param n
   * @param name
   * @return returns true iff the name is used in the node as a Name. */
  public static boolean uses(ASTNode n, String name) {
    return (n instanceof SimpleName && ((SimpleName) n).getIdentifier().equals(name))
        || (n instanceof QualifiedName && ((QualifiedName) n).getFullyQualifiedName().equals(name));
  }
}
