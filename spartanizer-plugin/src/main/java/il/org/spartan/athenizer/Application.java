package il.org.spartan.athenizer;

import il.org.spartan.spartanizer.plugin.AbstractSelection;
import il.org.spartan.spartanizer.plugin.Applicator;
import il.org.spartan.spartanizer.plugin.WrappedCompilationUnit;

/** A generic application that makes changes in java code. Being use by the
 * {@link Applicator}.
 * @author Ori Roth
 * @since Nov 25, 2016 */
@FunctionalInterface
public interface Application {
  /** Commits changes to a compilation unit from a selection.
   * @param u compilation unit wrapper
   * @param s selection, containing the compilation unit
   * @return how many changes were committed */
  Integer commitChanges(WrappedCompilationUnit u, AbstractSelection<?> s);
}
