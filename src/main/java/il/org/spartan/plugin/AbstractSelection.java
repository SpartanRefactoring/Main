package il.org.spartan.plugin;

import java.util.*;

import org.eclipse.jface.text.*;

/** An abstract selection, containing files and possible text selection.
 * @author Ori Roth
 * @since 2.6 */
public abstract class AbstractSelection<Self extends AbstractSelection<?>> {
  /** Files in selection. */
  public List<WrappedCompilationUnit> inner;
  /** Text selection in selection. Nullable. */
  public ITextSelection textSelection;
  /** Selection's name. */
  public String name;

  /** @return true iff the selection is empty, i.e. contain no files */
  public boolean isEmpty() {
    return inner == null || inner.isEmpty();
  }

  /** @return selection's size in compilation units */
  public int size() {
    return isEmpty() ? 0 : inner.size();
  }

  /** Set compilation units for this selection.
   * @param ¢ JD
   * @return this selection */
  public Self setCompilationUnits(final List<WrappedCompilationUnit> ¢) {
    inner = ¢ != null ? ¢ : new ArrayList<>();
    return self();
  }

  /** Set text selection for this selection.
   * @param ¢ JD
   * @return this selection */
  public Self setTextSelection(final ITextSelection ¢) {
    textSelection = ¢;
    return self();
  }

  @SuppressWarnings("unchecked") public Self self() {
    return (Self) this;
  }

  /** Set name for this selection.
   * @param ¢ JD
   * @return this selection */
  public Self setName(final String ¢) {
    name = ¢;
    return self();
  }

  /** Add a compilation unit for this selection.
   * @param ¢ JD
   * @return this selection */
  public Self add(final WrappedCompilationUnit ¢) {
    if (¢ != null)
      inner.add(¢);
    return self();
  }

  /** Add compilation units for this selection.
   * @param ¢ JD
   * @return this selection */
  public Self add(final List<WrappedCompilationUnit> ¢) {
    if (¢ != null)
      inner.addAll(¢);
    return self();
  }

  /** Add compilation units for this selection.
   * @param ¢ JD
   * @return this selection [[SuppressWarningsSpartan]] */
  public Self add(final WrappedCompilationUnit... ¢) {
    for (final WrappedCompilationUnit u : ¢)
      inner.add(u);
    return self();
  }

  /** Extend current selection using compilation units from another selection.
   * @param ¢ JD
   * @return this selection */
  public Self unify(final Self ¢) {
    if (¢ != null && ¢.inner != null) {
      if (inner == null)
        inner = new LinkedList<>();
      inner.addAll(¢.inner);
    }
    return self();
  }
}
