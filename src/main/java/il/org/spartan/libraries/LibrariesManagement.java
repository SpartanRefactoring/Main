package il.org.spartan.libraries;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;

import il.org.spartan.plugin.*;
import il.org.spartan.plugin.Plugin;
import il.org.spartan.spartanizer.utils.*;

public class LibrariesManagement {
  public static IPath getPluginJarPath() throws IOException {
    return new Path(FileLocator.resolve(FileLocator.find(Plugin.plugin().getBundle(), new Path(""), null)).getPath().replaceAll("!.*", "")
        .replaceAll("^file..", ""));
  }

  public static URL getPluginJarPath(IPath ¢) throws IOException {
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
      final List<IClasspathEntry> x = new LinkedList<>();
      for (final IClasspathEntry e : l)
        if (path.equals(e.getPath()))
          x.add(e);
      l.removeAll(x);
    });
  }

  public static boolean removeLibrary(final String path) {
    return removeLibrary(new Path(path));
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
    } catch (final JavaModelException x) {
      monitor.log(x);
      return false;
    }
    if (es != null)
      nes.addAll(Arrays.asList(es));
    operation.accept(nes, path);
    try {
      jp.setRawClasspath(nes.toArray(new IClasspathEntry[nes.size()]), null);
    } catch (final JavaModelException x) {
      monitor.log(x);
      return false;
    }
    return true;
  }
}
