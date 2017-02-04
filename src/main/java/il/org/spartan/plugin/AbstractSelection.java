package il.org.spartan.plugin;

import java.util.*;

import org.eclipse.jface.text.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An abstract selection, containing files and possible text selection.
 * @author Ori Roth
 * @since 2.6 */
public abstract class AbstractSelection<Self extends AbstractSelection<?>> {
  /** Files in selection. */
  @Nullable public List<WrappedCompilationUnit> inner;
  /** Text selection in selection. Nullable. */
  public ITextSelection textSelection;
  /** Selection's name. */
  public String name;
  /** True iff the selection is textual. */
  public boolean isTextSelection;

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
   * @return <code><b>this</b></code> selection */
  @NotNull public Self setCompilationUnits(@Nullable final List<WrappedCompilationUnit> ¢) {
    inner = ¢ != null ? ¢ : new ArrayList<>();
    return self();
  }

  /** Set text selection for this selection.
   * @param ¢ JD
   * @return <code><b>this</b></code> selection */
  @NotNull public Self setTextSelection(final ITextSelection ¢) {
    textSelection = ¢;
    return self();
  }

  @NotNull @SuppressWarnings("unchecked") private Self self() {
    return (Self) this;
  }

  /** Set name for this selection.
   * @param ¢ JD
   * @return <code><b>this</b></code> selection */
  @NotNull Self setName(final String ¢) {
    name = ¢;
    return self();
  }

  /** Add a compilation unit for this selection.
   * @param ¢ JD
   * @return <code><b>this</b></code> selection */
  @NotNull Self add(@Nullable final WrappedCompilationUnit ¢) {
    if (¢ != null)
      inner.add(¢);
    return self();
  }

  /** Add compilation units for this selection.
   * @param ¢ JD
   * @return <code><b>this</b></code> selection */
  @NotNull public Self add(@Nullable final List<WrappedCompilationUnit> ¢) {
    if (¢ != null)
      inner.addAll(¢);
    return self();
  }

  /** Add compilation units for this selection.
   * @param ¢ JD
   * @return <code><b>this</b></code> selection */
  @NotNull public Self add(final WrappedCompilationUnit... ¢) {
    Collections.addAll(inner, ¢);
    return self();
  }

  /** Extend current selection using compilation units from another selection.
   * @param ¢ JD
   * @return <code><b>this</b></code> selection */
  @NotNull Self unify(@Nullable final Self ¢) {
    if (¢ != null && ¢.inner != null) {
      if (inner == null)
        inner = new ArrayList<>();
      inner.addAll(¢.inner);
    }
    return self();
  }

  @NotNull Self setIsTextSelection(final boolean isTextSelection) {
    this.isTextSelection = isTextSelection;
    return self();
  }
}
