package il.org.spartan.plugin;

import java.util.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.*;

import il.org.spartan.spartanizer.utils.*;

/** A utility class to manage libraries at the users side, in front of the
 * ecilpse machine and specific code. Current implementation makes use of the
 * Users Libraries feature.
 * @author Ori Roth
 * @since Nov 25, 2016 */
public class LibrariesManagement {
  /** Absolute path of the spartan feature. */
  public static final IPath FEATURE_PATH;
  /** Eclipse's installation folder absolute path. */
  public static final IPath INSTALLATION_FOLDER;
  /** Library name, as seen by the user. */
  public static final String LIBRARY_NAME = "Spartan Library";
  /** The libraries qualified name, based upon folders hierarchy in the
   * SpartanFeature project. */
  public static final String LIBRARY_QULIFIED_NAME = "spartan.libraries";
  /** The class path container for our library. */
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

  /** @return true iff the spartan library exists within eclipse. */
  public static boolean libraryExists() {
    @SuppressWarnings("restriction") List<String> userLibrariesNames = Arrays.asList(new UserLibraryManager().getUserLibraryNames());
    return userLibrariesNames.contains(LIBRARY_NAME);
  }

  /** @param p JD
   * @return true iff the project uses the spartan library. */
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

  /** Adding the spartan library to a project.
   * @param p JD
   * @return true upon success */
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

  /** If the project does not make use of the spartan library, we try to add it.
   * @param ¢ JD
   * @return true iff the project uses the spartan library */
  public static boolean checkLibrary(final IJavaProject ¢) {
    return ¢ != null && (hasLibrary(¢) || addLibrary(¢));
  }

  /** Add the spartan library to eclipse's user libraries.
   * @throws CoreException */
  public static void initializeUserLibraries() throws CoreException {
    if (!libraryExists())
      JavaCore.getClasspathContainerInitializer(JavaCore.USER_LIBRARY_CONTAINER_ID)
          .requestClasspathContainerUpdate((new Path(JavaCore.USER_LIBRARY_CONTAINER_ID)).append(LIBRARY_NAME), null, LIBRARY_PATH_CONTAINER);
  }
}
