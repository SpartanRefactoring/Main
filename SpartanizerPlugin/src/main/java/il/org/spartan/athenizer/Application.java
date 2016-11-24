package il.org.spartan.athenizer;

import il.org.spartan.plugin.*;

/** A generic application that makes changes in java code. Being use by the
 * {@link Applicator}. [[SuppressWarningsSpartan]]
 * @author Ori Roth
 * @since Nov 25, 2016 */
public interface Application {
  /** Commits changes to a compilation unit from a selection.
   * @param u compilation unit wrapper
   * @param s selection, containing the compilation unit
   * @return how many changes were committed */
  Integer commitChanges(final WrappedCompilationUnit u, final AbstractSelection<?> s);

  /** @param s current selection
   * @return if the service is available for the specific selection */
  default boolean checkServiceAvailable(@SuppressWarnings("unused") final AbstractSelection<?> s) {
    return true;
  }
}
