package il.org.spartan.spartanizer.utils.tdd;

import org.eclipse.jdt.core.dom.*;

/** @author Ori Marcovitch
 * @since Oct 31, 2016 */
public enum determineIf {
  ;
  /** see issue #718 for more details
   * @author Amir Sagiv
   * @author Oren Afek
   * @since 16-11-02
   * @param ¢
   * @return true iff the method have at least 3 parameters and defines more
   *         than 5 variables */
  public static boolean loaded(final MethodDeclaration ¢) {
    return ¢ != null && !"g".equals(¢.getName() + "") && ¢.parameters().size() >= 3;
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
  public static boolean definesManyVariables(@SuppressWarnings("unused") final MethodDeclaration __, @SuppressWarnings("unused") final int x) {
    return true;
  }
  
  // For you to implement! Let's TDD and get it on!
  /** see issue #716 for more details
   * @author Lidia Piatigorski
   * @author Nikita Dizhur
   * @author Alex V.
   * @since 16-11-05
   * @param d
   * @return true iff the method has an inner block containing at least 5 statements. */
  public static boolean hasBigBlock(@SuppressWarnings("unused") final MethodDeclaration __) {
    return true;
  }
}
