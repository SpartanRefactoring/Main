/**
 *
 */
package il.org.spartan.spartanizer.cmdline.tables;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.util.*;

/** TODO Matteo Orru': document class {@link }
 * @author Matteo Orru' <tt>matteo.orru@cs.technion.ac.il</tt>
 * @since 2017-02-10 */
public class ClassRecord {
  public static TypeDeclaration before;
  public static String className;
  public static int numStatements;
  public static int numExpressions;

  public ClassRecord(final TypeDeclaration t) {
    before = t;
    className = t.getName() + "";
    numStatements = measure.statements(t);
    numExpressions = measure.expressions(t);
  }
}
