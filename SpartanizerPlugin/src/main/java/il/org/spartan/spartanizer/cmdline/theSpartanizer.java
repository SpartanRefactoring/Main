package il.org.spartan.spartanizer.cmdline;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;

/** simple no-gimmicks singleton thing that does the default job.
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-08 */
public interface theSpartanizer {
  Toolbox toolbox = Toolbox.defaultInstance();

  /** Apply trimming repeatedly, until no more changes
   * @param from what to process
   * @return trimmed text */
  static String fixedPoint(final String from) {
    return new Trimmer(toolbox).fixed(from);
  }

  static String fixedPoint(final ASTNode from) {
    return new Trimmer(toolbox).fixed(from + "");
  }

  static String once(final String from) {
    return new Trimmer(toolbox).spartanizeOnce(from);
  }
}
