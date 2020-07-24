package il.org.spartan.spartanizer.plugin;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import fluent.ly.note;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Taxon;
import il.org.spartan.spartanizer.traversal.Toolboxes;
import il.org.spartan.spartanizer.traversal.Traversal;
import il.org.spartan.spartanizer.traversal.TraversalImplementation;

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
  private static final String SPARTANIZATION_SHORT_PREFIX = "Tip: ";
  /** Empty prefix for brevity */
  public static final String EMPTY_PREFIX = "";
  /** the ID under which this builder is registered */
  public static final String BUILDER_ID = "spartan.tipper";
  public static final String MARKER_TYPE = "il.org.spartan.tip";
  /** the key in the marker's properties map under which the __ of the
   * spartanization is stored */
  public static final String SPARTANIZATION_TYPE_KEY = "il.org.spartan.spartanizer.spartanizationType";
  /** the key in the marker's properties map under which the __ of the tipper
   * used to create the marker is stored */
  public static final String SPARTANIZATION_TIPPER_KEY = "il.org.spartan.spartanizer.spartanizationTipper";
  /** Start of spartanization range. */
  public static final String SPARTANIZATION_CHAR_START = "il.org.spartan.spartanizer.spartanizationCharStart";
  /** End of spartanization range. */
  public static final String SPARTANIZATION_CHAR_END = "il.org.spartan.spartanizer.spartanizationCharEnd";

  /** deletes all spartanization tip markers
   * @param function the file from which to delete the markers
   * @throws CoreException if this method fails. Reasons include: This resource
   *         does not exist. This resource is a project that is not open.
   *         Resource changes are disallowed during certain types of resource
   *         change event notification¢ See {@link IResourceChangeEvent}¢for
   *         more details. */
  public static void deleteMarkers(final IResource ¢) throws CoreException {
    ¢.deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_ONE);
  }
  private static void incrementalBuild(final IResourceDelta d) throws CoreException {
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
  private static void addMarkers(final IFile ¢) throws CoreException {
    deleteMarkers(¢);
    try {
      addMarkers(¢, (CompilationUnit) makeAST.COMPILATION_UNIT.from(¢));
    } catch (final Throwable x) {
      note.bug(x);
    }
  }
  private static void addMarkers(final IResource f, final CompilationUnit u) throws CoreException {
    final Traversal s = new TraversalImplementation();
    s.useProjectPreferences();
    for (final Tip ¢ : s.collectTips(u)) // NANO
      if (¢ != null)
        ¢.intoMarker(f.createMarker(groupName(Toolboxes.groupOf(¢))));
  }
  private static String groupName(final Taxon ¢) {
    return ¢ == null || ¢.label() == null ? MARKER_TYPE : MARKER_TYPE + "." + ¢.label();
  }
  public static String prefix() {
    return SPARTANIZATION_SHORT_PREFIX;
  }
  @Override protected IProject[] build(final int kind, @SuppressWarnings({ "unused", "rawtypes" }) final Map __, final IProgressMonitor m)
      throws CoreException {
    if (m != null)
      m.beginTask("Checking for spartanization opportunities", IProgressMonitor.UNKNOWN);
    build(kind, m);
    if (m != null)
      m.done();
    return null;
  }
  private void fullBuild(final IProgressMonitor m) {
    try {
      getProject().accept(λ -> {
        if (m.isCanceled() || isInterrupted())
          return false;
        addMarkers(λ);
        return !m.isCanceled() && !isInterrupted();
      });
    } catch (final CoreException ¢) {
      note.bug(this, ¢);
    }
  }
  private void build(final IProgressMonitor ¢) throws CoreException {
    if (Eclipse.waitingForRefresh.remove(getProject()))
      fullBuild(¢);
    else
      incrementalBuild(getDelta(getProject()));
  }
  private void build(final int kind, final IProgressMonitor m) throws CoreException {
    if (kind != FULL_BUILD)
      build(m);
    else
      fullBuild(m);
  }
}