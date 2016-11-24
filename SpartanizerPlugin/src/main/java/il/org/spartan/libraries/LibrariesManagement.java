package il.org.spartan.libraries;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.*;

import il.org.spartan.plugin.*;
import il.org.spartan.plugin.Plugin;
import il.org.spartan.spartanizer.utils.*;

public class LibrariesManagement {
  public static final IPath FEATURE_PATH;
  public static final IPath INSTALLATION_FOLDER;
  public static final String LIBRARY_NAME = "Spartan Library";
  public static final String LIBRARY_QULIFIED_NAME = "spartan.libraries";
  public static final IClasspathContainer LIBRARY_PATH_CONTAINER;
  static {
    INSTALLATION_FOLDER = new Path(Platform.getInstallLocation().getURL().getPath());
    // TODO: update version 2.6.3 upon release. DO NOT remove this todo.
    FEATURE_PATH = INSTALLATION_FOLDER.append("features/SpartanFeature_2.6.4.jar");
    LIBRARY_PATH_CONTAINER = new IClasspathContainer() {
      @Override public IPath getPath() {
        return new Path(JavaCore.USER_LIBRARY_CONTAINER_ID).append(LIBRARY_NAME);
      }

      @Override public int getKind() {
        return K_APPLICATION;
      }

      @Override public String getDescription() {
        return LIBRARY_NAME;
      }

      @Override public IClasspathEntry[] getClasspathEntries() {
        return new IClasspathEntry[] { JavaCore.newLibraryEntry(FEATURE_PATH, null, null) };
      }
    };
  }

  public static boolean libraryExists() {
    @SuppressWarnings("restriction") List<String> userLibrariesNames = Arrays.asList(new UserLibraryManager().getUserLibraryNames());
    return userLibrariesNames.contains(LIBRARY_NAME);
  }

  public static boolean hasLibrary(final IJavaProject p) {
    if (p == null)
      return false;
    try {
      for (final IClasspathEntry ¢ : p.getRawClasspath())
        if (LIBRARY_PATH_CONTAINER.getPath().equals(¢.getPath()))
          return true;
    } catch (JavaModelException ¢) {
      monitor.log(¢);
    }
    return false;
  }

  public static boolean checkLibrary(final IJavaProject ¢) {
    return ¢ != null && (hasLibrary(¢) || addLibrary(¢));
  }

  public static boolean addLibrary(IJavaProject p) {
    if (p == null)
      return false;
    final List<IClasspathEntry> nes = new LinkedList<>();
    final IClasspathEntry[] es;
    try {
      es = p.getRawClasspath();
    } catch (final JavaModelException ¢) {
      monitor.log(¢);
      return false;
    }
    if (es != null)
      nes.addAll(Arrays.asList(es));
    nes.add(JavaCore.newContainerEntry(LIBRARY_PATH_CONTAINER.getPath(), null, null, false));
    try {
      p.setRawClasspath(nes.toArray(new IClasspathEntry[nes.size()]), null);
    } catch (final JavaModelException ¢) {
      monitor.log(¢);
      return false;
    }
    return true;
  }

  /** [[SuppressWarningsSpartan]] --bug */
  public static void initializeUserLibraries() throws CoreException {
    final ClasspathContainerInitializer initializer = JavaCore.getClasspathContainerInitializer(JavaCore.USER_LIBRARY_CONTAINER_ID);
    if (libraryExists())
      return;
    final IPath containerPath = new Path(JavaCore.USER_LIBRARY_CONTAINER_ID);
    initializer.requestClasspathContainerUpdate(containerPath.append(LIBRARY_NAME), null, LIBRARY_PATH_CONTAINER);
  }

  @Deprecated public static IPath getPluginJarPath() throws IOException {
    return new Path(FileLocator.resolve(FileLocator.find(Plugin.plugin().getBundle(), new Path(""), null)).getPath().replaceAll("!.*", "")
        .replaceAll("^file..", ""));
  }

  @Deprecated public static URL getPluginJarPath(final IPath ¢) throws IOException {
    return FileLocator.resolve(FileLocator.find(Plugin.plugin().getBundle(), ¢, null));
  }

  /** [[SuppressWarningsSpartan]] --bug */
  @Deprecated public static boolean addLibrary(final IPath path) {
    return touchLibrary(path, (l, p) -> l.add(JavaCore.newLibraryEntry(p, null, null)));
  }

  @Deprecated public static boolean addLibrary(final String path) {
    return addLibrary(new Path(path));
  }

  /** [[SuppressWarningsSpartan]] --bug */
  @Deprecated public static boolean removeLibrary(final IPath path) {
    return touchLibrary(path, (l, p) -> {
      final List<IClasspathEntry> x = new LinkedList<>();
      for (final IClasspathEntry e : l)
        if (path.equals(e.getPath()))
          x.add(e);
      l.removeAll(x);
    });
  }

  @Deprecated public static boolean removeLibrary(final String path) {
    return removeLibrary(new Path(path));
  }

  @Deprecated private static boolean touchLibrary(final IPath path, final BiConsumer<List<IClasspathEntry>, IPath> operation) {
    final IProject p = Selection.Util.project();
    if (p == null)
      return false;
    final IJavaProject jp = JavaCore.create(p);
    if (jp == null)
      return false;
    final List<IClasspathEntry> nes = new LinkedList<>();
    final IClasspathEntry[] es;
    try {
      es = jp.getRawClasspath();
    } catch (final JavaModelException ¢) {
      monitor.log(¢);
      return false;
    }
    if (es != null)
      nes.addAll(Arrays.asList(es));
    operation.accept(nes, path);
    try {
      jp.setRawClasspath(nes.toArray(new IClasspathEntry[nes.size()]), null);
    } catch (final JavaModelException ¢) {
      monitor.log(¢);
      return false;
    }
    return true;
  }
}
