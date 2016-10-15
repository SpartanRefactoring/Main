package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.jface.text.*;

/** An abstract selection, containing files and possible text selection.
 * @author Ori Roth
 * @since 2.6 */
public abstract class AbstractSelection {
  /** Files in selection. */
  public List<CU> compilationUnits;
  /** Text selection in selection. Nullable. */
  public ITextSelection textSelection;
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
  public AbstractSelection setCompilationUnits(final List<CU> ¢) {
    compilationUnits = ¢ != null ? ¢ : new ArrayList<>();
    return this;
  }

  /** Set text selection for this selection.
   * @param ¢ JD
   * @return this selection */
  public AbstractSelection setTextSelection(final ITextSelection ¢) {
    textSelection = ¢;
    return this;
  }

  /** Set name for this selection.
   * @param ¢ JD
   * @return this selection */
  public AbstractSelection setName(final String ¢) {
    name = ¢;
    return this;
  }

  /** Add a compilation unit for this selection.
   * @param ¢ JD
   * @return this selection */
  public AbstractSelection add(final CU ¢) {
    if (¢ != null)
      compilationUnits.add(¢);
    return this;
  }

  /** Add compilation units for this selection.
   * @param ¢ JD
   * @return this selection */
  public AbstractSelection add(final List<CU> ¢) {
    if (¢ != null)
      compilationUnits.addAll(¢);
    return this;
  }

  /** Add compilation units for this selection.
   * @param ¢ JD
   * @return this selection [[SuppressWarningsSpartan]] */
  public AbstractSelection add(final CU... ¢) {
    for (final CU u : ¢)
      compilationUnits.add(u);
    return this;
  }

  /** Extend current selection using compilation units from another selection.
   * @param ¢ JD
   * @return this selection */
  public AbstractSelection unify(final AbstractSelection ¢) {
    compilationUnits.addAll(¢.compilationUnits);
    return this;
  }
}
