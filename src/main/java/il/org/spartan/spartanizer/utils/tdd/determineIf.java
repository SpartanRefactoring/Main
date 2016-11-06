package il.org.spartan.spartanizer.utils.tdd;

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
      @Override public boolean visit(@SuppressWarnings("unused") VariableDeclarationFragment __){
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
   * @param d
   * @return true iff the method has at least 10 statements */
  public static boolean hasManyStatements(@SuppressWarnings("unused") final MethodDeclaration __) {
    return true;
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
   * @param ¢ 
   * @return returns true iff the method contains a return null statement . */
  
public static boolean returnsNull (MethodDeclaration m) {
  Type type = m.getReturnType2();
  if (type.equals(int.class))
      return false;
  return true;
       }
}
  

