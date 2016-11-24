package il.org.spartan.libraries;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.*;

import il.org.spartan.plugin.*;
import il.org.spartan.plugin.Plugin;
import il.org.spartan.spartanizer.utils.*;

public class LibrariesManagement {
  // TODO: update version 2.6.3 upon release. DO NOT remove this todo.
  public static final IPath FEATURE_PATH = new Path("features/SpartanFeature_2.6.4.jar");
  public static final IPath INSTALLATION_FOLDER;
  public static final String LIBRARY_NAME = "Spartan Libraries";
  static {
    INSTALLATION_FOLDER = new Path(Platform.getInstallLocation().getURL().getPath());
  }

  public static IPath getPluginJarPath() throws IOException {
    return new Path(FileLocator.resolve(FileLocator.find(Plugin.plugin().getBundle(), new Path(""), null)).getPath().replaceAll("!.*", "")
        .replaceAll("^file..", ""));
  }

  public static URL getPluginJarPath(final IPath ¢) throws IOException {
    return FileLocator.resolve(FileLocator.find(Plugin.plugin().getBundle(), ¢, null));
  }

  /** [[SuppressWarningsSpartan]] --bug */
  public static boolean addLibrary(final IPath path) {
    return touchLibrary(path, (l, p) -> l.add(JavaCore.newLibraryEntry(p, null, null)));
  }

  public static boolean addLibrary(final String path) {
    return addLibrary(new Path(path));
  }

  /** [[SuppressWarningsSpartan]] --bug */
  public static boolean removeLibrary(final IPath path) {
    return touchLibrary(path, (l, p) -> {
      final List<IClasspathEntry> x = l.stream().filter(e -> path.equals(e.getPath())).collect(Collectors.toCollection(LinkedList::new));
      l.removeAll(x);
    });
  }

  public static boolean removeLibrary(final String path) {
    return removeLibrary(new Path(path));
  }

<<<<<<< HEAD
  /** 
   * @throws IOException */
  public static void initializeUserLibraries() throws CoreException, IOException {
    final ClasspathContainerInitializer initializer = JavaCore.getClasspathContainerInitializer(JavaCore.USER_LIBRARY_CONTAINER_ID);
    @SuppressWarnings("restriction") final List<String> userLibrariesNames = Arrays.asList(new UserLibraryManager().getUserLibraryNames());
    for (final String librariesPathSuffice : librariesPathSuffices) {
      final String libraryName = librariesNames[0];
      if (userLibrariesNames.contains(libraryName))
        continue;
      final IPath libraryPath = getPluginJarPath().append(librariesPathSuffices[0]);
      initializer.requestClasspathContainerUpdate((new Path(JavaCore.USER_LIBRARY_CONTAINER_ID)).append(libraryName), null,
          new IClasspathContainer() {
            @Override public IPath getPath() {
              return new Path(JavaCore.USER_LIBRARY_CONTAINER_ID).append(libraryName);
            }

            @Override public int getKind() {
              return K_APPLICATION;
            }

            @Override public String getDescription() {
              return libraryName;
            }

            @Override public IClasspathEntry[] getClasspathEntries() {
              return new IClasspathEntry[] { JavaCore.newLibraryEntry(libraryPath, null, null) };
            }
          });
    }
=======
  /** [[SuppressWarningsSpartan]] --bug*/
  public static void initializeUserLibraries() throws CoreException {
    final ClasspathContainerInitializer initializer = JavaCore.getClasspathContainerInitializer(JavaCore.USER_LIBRARY_CONTAINER_ID);
    @SuppressWarnings("restriction") List<String> userLibrariesNames = Arrays.asList(new UserLibraryManager().getUserLibraryNames());
    final String libraryName = LIBRARY_NAME;
    if (userLibrariesNames.contains(libraryName))
      return;
    final IPath libraryPath = INSTALLATION_FOLDER.append(FEATURE_PATH);
    final IPath containerPath = new Path(JavaCore.USER_LIBRARY_CONTAINER_ID);
    initializer.requestClasspathContainerUpdate(containerPath.append(libraryName), null, new IClasspathContainer() {
      @Override public IPath getPath() {
        return new Path(JavaCore.USER_LIBRARY_CONTAINER_ID).append(libraryName);
      }

      @Override public int getKind() {
        return K_APPLICATION;
      }

      @Override public String getDescription() {
        return libraryName;
      }

      @Override public IClasspathEntry[] getClasspathEntries() {
        return new IClasspathEntry[] { JavaCore.newLibraryEntry(libraryPath, null, null) };
      }
    });
>>>>>>> c10ec559e5c6585ea2048caf199c13ff9df67f6f
  }

  private static boolean touchLibrary(final IPath path, final BiConsumer<List<IClasspathEntry>, IPath> operation) {
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
