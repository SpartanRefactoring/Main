package il.org.spartan.spartanizer.utils.tdd;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** @author Ori Marcovitch
 * @author Dor Ma'ayan
 * @author Raviv Rachmiel
 * @author Kfir Marx
 * @since Oct 31, 2016 */
public enum getAll {
  ;
  /** Get all the methods invoked in m
   * @param d JD
   * @return List of the names of the methods */
  public static Set<String> invocations(@SuppressWarnings("unused") final MethodDeclaration __) {
    return null;
  }

  /** Get list of names in a Block
   * @param b Block
   * @return List of the names in the block */
  public static List<Name> names(final Block b) {
    return null;
  }
  // For you to implement! Let's TDD and get it on!
}
