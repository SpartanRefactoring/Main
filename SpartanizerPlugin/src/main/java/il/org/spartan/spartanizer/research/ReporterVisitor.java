package il.org.spartan.spartanizer.research;

import org.eclipse.jdt.core.dom.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class ReporterVisitor extends ASTVisitor {
  @Override public boolean visit(final MethodDeclaration ¢) {
    System.out.println(¢.getName() + " has " + countOccurences(¢.getJavadoc() + " NP", "[["));
    return false;
  }

  private static int countOccurences(String javadoc, String pattern) {
    int lastIndex = 0;
    int $ = 0;
    while (lastIndex != -1) {
      lastIndex = javadoc.indexOf(pattern, lastIndex);
      if (lastIndex != -1) {
        ++$;
        lastIndex += pattern.length();
      }
    }
    return $;
  }
}
