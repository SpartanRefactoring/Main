package il.org.spartan.plugin.revision;

import java.util.*;

/** An abstract selection, containing files and possible text selection.
 * @author Ori Roth
 * @since 2.6 */
public abstract class AbstractSelection<F, T> {
  /** Files in selection. */
  public List<F> compilationUnits;
  /** Text selection in selection. Nullable. */
  public T textSelection;
  /** Selection's name. */
  public String name;

  /** @return true iff the selection is empty, i.e. contain no files */
  public boolean isEmpty() {
    return compilationUnits == null || compilationUnits.isEmpty();
  }
  
  /** @return selection's size in compilation units */
  public int size() {
    return isEmpty() ? 0 : compilationUnits.size();
  }

  /** Set compilation units for this selection.
   * @param ¢ JD
   * @return this selection */
  public AbstractSelection<F, T> setCompilationUnits(final List<F> ¢) {
    compilationUnits = ¢ != null ? ¢ : new ArrayList<>();
    return this;
  }

  /** Set text selection for this selection.
   * @param ¢ JD
   * @return this selection */
  public AbstractSelection<F, T> setTextSelection(final T ¢) {
    textSelection = ¢;
    return this;
  }

  /** Set name for this selection.
   * @param ¢ JD
   * @return this selection */
  public AbstractSelection<F, T> setName(final String ¢) {
    name = ¢;
    return this;
  }

  /** Add a compilation unit for this selection.
   * @param ¢ JD
   * @return this selection */
  public AbstractSelection<F, T> add(final F ¢) {
    if (¢ != null)
      compilationUnits.add(¢);
    return this;
  }

  /** Add compilation units for this selection.
   * @param ¢ JD
   * @return this selection */
  public AbstractSelection<F, T> add(final List<F> ¢) {
    if (¢ != null)
      compilationUnits.addAll(¢);
    return this;
  }

  /** Add compilation units for this selection.
   * @param ¢ JD
   * @return this selection [[SuppressWarningsSpartan]] */
  public AbstractSelection<F, T> add(@SuppressWarnings("unchecked") final F... ¢) {
    for (final F u : ¢)
      compilationUnits.add(u);
    return this;
  }

  /** Extend current selection using compilation units from another selection.
   * @param ¢ JD
   * @return this selection */
  public AbstractSelection<F, T> unify(final AbstractSelection<F, T> ¢) {
    compilationUnits.addAll(¢.compilationUnits);
    return this;
  }

  /** Factory method.
   * @return empty selection */
  public static Selection empty() {
    return new Selection(null, null, null);
  }
}
