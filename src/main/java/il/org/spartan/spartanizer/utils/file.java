package il.org.spartan.spartanizer.utils;

import java.io.*;
import java.nio.file.*;

/** @author Ori Marcovitch
 * @since Dec 4, 2016 */
public enum file {
  ;
  public static void delete(String path) {
    if (file.exists(path))
      (new File(path)).delete();
  }

  public static void rename(final String from, final String to) {
    file.delete(to);
    Path source = Paths.get(from);
    try {
      Files.move(source, source.resolveSibling(to));
    } catch (IOException x) {
      x.printStackTrace();
    }
  }

  public static boolean exists(final String path) {
    File f = new File(path);
    return f.exists() && !f.isDirectory();
  }
}
