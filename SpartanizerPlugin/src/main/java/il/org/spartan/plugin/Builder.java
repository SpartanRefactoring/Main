package il.org.spartan.plugin;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.plugin.PreferencesResources.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code> @since
 *         2014/6/16 (v3)
 * @author Tomer Zeltzer <code><tomerr90 [at] gmail.com></code>
 * @since 2013/07/01
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code> */
public final class Builder extends IncrementalProjectBuilder {
  /** Long prefix to be used in front of all tips */
  public static final String SPARTANIZATION_LONG_PREFIX = "Laconic tip: ";
  /** Short prefix to be used in front of all tips */
  public static final String SPARTANIZATION_SHORT_PREFIX = "Tip: ";
  /** Empty prefix for brevity */
  public static final String EMPTY_PREFIX = "";
  /** the ID under which this builder is registered */
  public static final String BUILDER_ID = "spartan.tipper";
  public static final String MARKER_TYPE = "il.org.spartan.tip";
  /** the key in the marker's properties map under which the type of the
   * spartanization is stored */
  public static final String SPARTANIZATION_TYPE_KEY = "il.org.spartan.spartanizer.spartanizationType";
  /** the key in the marker's properties map under which the type of the tipper
   * used to create the marker is stored */
  public static final String SPARTANIZATION_TIPPER_KEY = "il.org.spartan.spartanizer.spartanizationTipper";

  /** deletes all spartanization tip markers
   * @param f the file from which to delete the markers
   * @throws CoreException if this method fails. Reasons include: This resource
   *         does not exist. This resource is a project that is not open.
   *         Resource changes are disallowed during certain types of resource
   *         change event notification¢ See {@link IResourceChangeEvent}¢for
   *         more details. */
  public static void deleteMarkers(final IFile ¢) throws CoreException {
    ¢.deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_ONE);
  }

  public static void incrementalBuild(final IResourceDelta d) throws CoreException {
    d.accept(internalDelta -> {
      final int k = internalDelta.getKind();
      // return true to continue visiting children.
      if (k != IResourceDelta.ADDED && k != IResourceDelta.CHANGED)
        return true;
      addMarkers(internalDelta.getResource());
      return true;
    });
  }

  static void addMarkers(final IResource ¢) throws CoreException {
    if (¢ instanceof IFile && ¢.getName().endsWith(".java"))
      addMarkers((IFile) ¢);
  }

  private static void addMarker(final AbstractGUIApplicator a, final Tip r, final IMarker m) throws CoreException {
    m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
    m.setAttribute(SPARTANIZATION_TYPE_KEY, a + "");
    m.setAttribute(SPARTANIZATION_TIPPER_KEY, r.tipperClass);
    m.setAttribute(IMarker.MESSAGE, prefix() + r.description);
    m.setAttribute(IMarker.CHAR_START, r.from);
    m.setAttribute(IMarker.CHAR_END, r.to);
    m.setAttribute(IMarker.TRANSIENT, false);
    m.setAttribute(IMarker.LINE_NUMBER, r.lineNumber);
  }

  private static void addMarkers(final IFile ¢) throws CoreException {
    Tips.reset();
    deleteMarkers(¢);
    addMarkers(¢, (CompilationUnit) makeAST.COMPILATION_UNIT.from(¢));
  }

  private static void addMarkers(final IFile f, final CompilationUnit u) throws CoreException {
    for (final AbstractGUIApplicator s : Tips.all())
      for (final Tip ¢ : s.collectSuggesions(u))
        if (¢ != null) {
          final TipperGroup group = Toolbox.groupFor(¢.tipperClass);
          addMarker(s, ¢, f.createMarker(group == null || group.id == null ? MARKER_TYPE : MARKER_TYPE + "." + group.name()));
        }
  }

  private static String prefix() {
    return SPARTANIZATION_SHORT_PREFIX;
  }

  @Override protected IProject[] build(final int kind, @SuppressWarnings({ "unused", "rawtypes" }) final Map __, final IProgressMonitor m)
      throws CoreException {
    if (m != null)
      m.beginTask("Checking for spartanization opportunities", IProgressMonitor.UNKNOWN);
    Toolbox.refresh();
    build(kind);
    if (m != null)
      m.done();
    return null;
  }

  protected void fullBuild() {
    try {
      getProject().accept(r -> {
        addMarkers(r);
        return true; // to continue visiting children.
      });
    } catch (final CoreException ¢) {
      monitor.logCancellationRequest(this, ¢);
    }
  }

  private void build() throws CoreException {
    final IResourceDelta d = getDelta(getProject());
    if (d == null)
      fullBuild();
    else
      incrementalBuild(d);
  }

  private void build(final int kind) throws CoreException {
    if (kind != FULL_BUILD)
      build();
    else
      fullBuild();
  }
}