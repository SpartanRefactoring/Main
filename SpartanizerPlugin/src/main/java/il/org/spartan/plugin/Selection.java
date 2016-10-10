package il.org.spartan.plugin;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
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

  public Selection fixEmptyTextSelection() {
    if (compilationUnits == null || compilationUnits.size() != 1 || textSelection == null || textSelection.getLength() > 0)
      return this;
    ICompilationUnit u = compilationUnits.get(0);
    IResource r = u.getResource();
    if (!(r instanceof IFile))
      return this;
    int o = textSelection.getOffset();
    try {
      for (IMarker m : ((IFile) r).findMarkers(Builder.MARKER_TYPE, true, IResource.DEPTH_INFINITE)) {
        int cs = ((Integer) m.getAttribute(IMarker.CHAR_START)).intValue();
        int ce = ((Integer) m.getAttribute(IMarker.CHAR_END)).intValue();
        if (cs <= o && ce >= o)
          return this.setTextSelection(new TextSelection(cs, ce - cs));
      }
    } catch (CoreException x) {
      monitor.log(x);
      return this;
    }
    return this;
  }

  @Override public String toString() {
    int s = compilationUnits == null ? 0 : compilationUnits.size();
    return "{" + (compilationUnits == null ? null : s + " " + RefactorerUtil.plurals("file", s)) + ", "
        + (textSelection == null ? null : printable(textSelection)) + "}";
  }

  public static String printable(ITextSelection ¢) {
    return "(" + ¢.getOffset() + "," + ¢.getLength() + ")";
  }
}
