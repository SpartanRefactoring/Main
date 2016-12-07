package il.org.spartan.spartanizer.utils;

import java.io.*;
import java.nio.file.*;

/** @author Ori Marcovitch
 * @since Dec 4, 2016 */
public enum file {
  ;
  public static void delete(final String path) {
    if (file.exists(path))
      new File(path).delete();
  }

  public static void rename(final String from, final String to) {
    file.delete(to);
    final Path source = Paths.get(from);
    try {
      Files.move(source, source.resolveSibling(to));
    } catch (final IOException x) {
      x.printStackTrace();
    }
  }

  public static boolean exists(final String path) {
    final File f = new File(path);
    return f.exists() && !f.isDirectory();
  }
}
