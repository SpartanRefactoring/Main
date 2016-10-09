package il.org.spartan.plugin;

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.*;

/** Describes a selection, containing selected compilation unit(s) and text
 * selection
 * @author Ori Roth
 * @since 2016 */
public class Selection {
  public List<ICompilationUnit> compilationUnits;
  public ITextSelection textSelection;

  public Selection(final List<ICompilationUnit> compilationUnits, final ITextSelection textSelection) {
    this.compilationUnits = compilationUnits != null ? compilationUnits : new ArrayList<>();
    this.textSelection = textSelection;
  }

  public Selection setCompilationUnits(final List<ICompilationUnit> ¢) {
    compilationUnits = ¢ != null ? ¢ : new ArrayList<>();
    return this;
  }

  public Selection setTextSelection(final ITextSelection ¢) {
    textSelection = ¢;
    return this;
  }

  public Selection add(final ICompilationUnit ¢) {
    if (¢ != null)
      compilationUnits.add(¢);
    return this;
  }

  public Selection add(final List<ICompilationUnit> ¢) {
    if (¢ != null)
      compilationUnits.addAll(¢);
    return this;
  }

  /** [[SuppressWarningsSpartan]] */
  public Selection add(final ICompilationUnit... ¢) {
    for (final ICompilationUnit u : ¢)
      compilationUnits.add(u);
    return this;
  }

  public Selection unify(final Selection ¢) {
    compilationUnits.addAll(¢.compilationUnits);
    return this;
  }

  public static Selection empty() {
    return new Selection(null, null);
  }

  public static Selection of(final List<ICompilationUnit> ¢) {
    return new Selection(¢, null);
  }

  public static Selection of(final ICompilationUnit ¢) {
    return new Selection(¢ == null ? null : Collections.singletonList(¢), null);
  }

  public static Selection of(final ICompilationUnit u, final ITextSelection s) {
    return new Selection(u == null ? null : Collections.singletonList(u), s);
  }

  public static Selection of(final ICompilationUnit[] ¢) {
    return new Selection(Arrays.asList(¢), null);
  }
}
