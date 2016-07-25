package il.org.spartan.refactoring.builder;

import il.org.spartan.refactoring.contexts.*;
import il.org.spartan.refactoring.contexts.CurrentAST.*;
import il.org.spartan.refactoring.suggestions.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.contexts.CurrentAST.*;

/**
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code> @since
 *         2014/6/16 (v3)
 * @author Tomer Zeltzer <code><tomerr90 [at] gmail.com></code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2013/07/01
 */
public class Builder extends IncrementalProjectBuilder {
  /** Long prefix to be used in front of all suggestions */
  public static final String SPARTANIZATION_LONG_PREFIX = "Spartanization suggestion: ";
  /** Short prefix to be used in front of all suggestions */
  public static final String SPARTANIZATION_SHORT_PREFIX = "Spartanize: ";
  /** Empty prefix for brevity */
  public static final String EMPTY_PREFIX = "";

  private static String prefix() {
    return SPARTANIZATION_SHORT_PREFIX;
  }

  /** the ID under which this builder is registered */
  public static final String BUILDER_ID = "org.spartan.refactoring.BuilderID";
  private static final String MARKER_TYPE = "org.spartan.refactoring.spartanizationSuggestion";
  /**
   * the key in the marker's properties map under which the type of the
   * spartanization is stored
   */
  public static final String SPARTANIZATION_TYPE_KEY = "org.spartan.refactoring.spartanizationType";

  @Override protected IProject[] build(final int kind, @SuppressWarnings({ "unused", "rawtypes" }) final Map __, final IProgressMonitor m) throws CoreException {
    build(kind, m);
    return null;
  }
  /**
   * TODO Javadoc(2016): automatically generated for method <code>build</code>
   *
   * @param kind
   * @param m
   *          void TODO Javadoc(2016) automatically generated for returned value
   *          of method <code>build</code>
   */
  private void build(final int kind, final IProgressMonitor m) {
    inContext().set(m).new Current() {
      @Override protected void go() {
        context.progressMonitor().beginTask("Searching for spartanization suggestions", IProgressMonitor.UNKNOWN);
        if (kind == FULL_BUILD)
          fullBuild();
        else
          build(getDelta(getProject()));
        context.progressMonitor().done();
      }
    }.go();
  }
  /**
   * Build for a given delta
   *
   * @param d
   *          JD
   */
  void build(final IResourceDelta d) {
    if (d == null)
      fullBuild();
    else
      incrementalBuild(d);
  }
  void fullBuild() {
    try {
      getProject().accept(r -> {
        addMarkers(r);
        return true;
      });
    } catch (final CoreException e) {
      e.printStackTrace();
    }
  }
  static void addMarkers(final IResource r) throws CoreException {
    if (r instanceof IFile && r.getName().endsWith(".java"))
      addMarkers((IFile) r);
  }
  private static void addMarkers(final IFile f) throws CoreException {
    deleteMarkers(f);
    addMarkers(f, (CompilationUnit) ast.COMPILIATION_UNIT.from(f));
  }
  private static void addMarkers(final IFile f, final CompilationUnit u) throws CoreException {
    final CurrentAST c = inContext().set(u).set(new Toolbox());
    for (final @NonNull Suggestion s : s.collect(u))
      addMarker(s, f.createMarker(MARKER_TYPE));
  }
  private static void addMarker(final Suggestion s, final IMarker m) throws CoreException {
    m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
    m.setAttribute(SPARTANIZATION_TYPE_KEY, s.toString());
    m.setAttribute(IMarker.MESSAGE, prefix() + s.description());
    m.setAttribute(IMarker.CHAR_START, s.range().from());
    m.setAttribute(IMarker.CHAR_END, s.to());
    m.setAttribute(IMarker.TRANSIENT, false);
    m.setAttribute(IMarker.LINE_NUMBER, s.lineNumber());
  }
  /**
   * deletes all spartanization suggestion markers
   *
   * @param f
   *          the file from which to delete the markers
   * @throws CoreException
   *           if this method fails. Reasons include: This resource does not
   *           exist. This resource is a project that is not open. Resource
   *           changes are disallowed during certain types of resource change
   *           event notification. See {@link IResourceChangeEvent} for more
   *           details.
   */
  public static void deleteMarkers(final IFile f) throws CoreException {
    f.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ONE);
  }
  /**
   * @param d
   *          JD
   * @throws CoreException
   */
  public static void incrementalBuild(final IResourceDelta d) {
    try {
      d.accept(v -> {
        if (found(v.getKind()).in(IResourceDelta.ADDED, IResourceDelta.CHANGED))
          addMarkers(v.getResource());
        return true;
      });
    } catch (final CoreException e) {
      e.printStackTrace();
    }
  }
}
