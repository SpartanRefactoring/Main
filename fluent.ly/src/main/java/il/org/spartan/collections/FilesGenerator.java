package il.org.spartan.collections;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.*;

import fluent.ly.as;

/** Provides, employing fluent API, a {@link Iterable} interface for iteration
 * over files in the file system.
 * <p>
 * Typical uses are<code>
 *
 * <pre>
 *   <b>for</b> ({@link File} f: <b>new</b> {@link FilesGenerator}(".java").from("."))
 *     System.out.println(f);
 * </pre>
 * <pre> <b>for</b> ({@link File} f: <b>new</b> {@link FilesGenerator}
 * (".java").from(".")) System.out.println(f); </pre>
 *
 * to recursively iterate over all files whose extension is ".java" in the
 * current directory, or
 * </code> to recursively iterate over all files whose extension is ".java" in
 * the current directory, or <code>
 *
 * <pre>
 *   <b>for</b> ({@link File} f: <b>new</b> {@link FilesGenerator}().from("/bin", "/home"))
 *     System.out.println(f);
 * </pre>
 * <pre> <b>for</b> ({@link File} f: <b>new</b> {@link FilesGenerator}
 * ().from("/bin", "/home")) System.out.println(f); </pre>
 *
 * to recursively iterate (over all files in the <code>/bin</code> and </code>
 * to recursively iterate (over all files in the <code>/bin</code> and
 * <code>/home</code> directories.
 * @author Yossi Gil
 * @since 2015-09-23. */
public class FilesGenerator {
  /** @param ____ ignored */
  public static void main(final String[] ____) {
    for (final File ¢ : new FilesGenerator(".java").from("."))
      System.out.println(¢);
  }

  /** @param directory should be a directory, but we still need to account for
   *        weird creatures such as "System Volume Information" */
  @Nullable static Iterator<File> directoryIterator(@Nullable final File directory) {
    if (directory == null || !directory.isDirectory() || directory.list() == null)
      return null;
    @NotNull final Iterator<String> $ = as.list(directory.list()).iterator();
    return new Iterator<File>() {
      @Nullable File next;

      @Override public boolean hasNext() {
        for (;;) {
          if (!$.hasNext())
            return false;
          final @Nullable String name = $.next();
          if (name == null)
            continue;
          next = new File(directory, name);
          return true;
        }
      }

      @Override @Nullable public File next() {
        return next;
      }
    };
  }

  @NotNull private static Iterable<File> asFiles(@NotNull final Iterable<String> fileNames) {
    @NotNull final List<File> $ = new ArrayList<>();
    for (@NotNull final String fileName : fileNames)
      $.add(new File(fileName));
    return $;
  }

  /** Which extensions we search for */
  @NotNull final Iterable<String> extensions;

  /** Instantiates this class. This instantiation makes the first step in the
   * call chain that makes the fluent API. The second (and last) such step is
   * provided by function {@link #from(String...)}.
   * @param extensions an array of non-<code><b>null</b></code> {@link String}s
   *        specifying the allowed extensions for files that the iterator should
   *        yield, e.g., ".java", ".class", ".doc", etc. If this parameter is
   *        <code><b>null</b></code>, or of length 0, or contains a
   *        {@link String} of length 0, then the iterator yields all files found
   *        in the scanned locations.
   * @see FilesGenerator#from */
  public FilesGenerator(final String... extensions) {
    this.extensions = as.list(extensions);
  }

  /** @param from an array of names of directories from which the traversal
   *        should begin
   * @return an instance of an internal (yet <code><b>public</b></code>)
   *         <code><b>class</b></code> which <code><b>implements</b></code> the
   *         {@link Iterable} <code><b>interface</b></code> */
  @NotNull public From from(@NotNull final Iterable<String> from) {
    return new From(asFiles(from));
  }

  /** @param from an array of names of directories from which the traversal
   *        should begin
   * @return an instance of an internal (yet <code><b>public</b></code>)
   *         <code><b>class</b></code> which <code><b>implements</b></code> the
   *         {@link Iterable} <code><b>interface</b></code> */
  @NotNull public From from(final String... from) {
    return from(as.list(from));
  }

  /** An internal (yet <code><b>public</b></code>) <code><b>class</b></code>
   * which <code><b>implements</b></code> the {@link Iterable}
   * <code><b>interface</b></code>.
   * @author Yossi Gil
   * @since 2015-09-23. */
  public class From implements Iterable<File> {
    final Iterable<File> from;

    From(final Iterable<File> from) {
      this.from = from;
    }

    @Override @NotNull public Iterator<File> iterator() {
      return new FilesIterator(from.iterator());
    }

    private class FilesIterator implements Iterator<File> {
      private File next;
      private final Stack<Iterator<File>> stack = new Stack<>();

      public FilesIterator(final Iterator<File> i) {
        stack.push(i);
      }

      @Override @SuppressWarnings("null") public boolean hasNext() {
        for (;;) {
          if (stack.isEmpty())
            return false;
          final @Nullable Iterator<File> currentIterator = stack.peek();
          if (currentIterator == null || !currentIterator.hasNext()) {
            stack.pop();
            continue;
          }
          next = currentIterator.next();
          if (next.isDirectory()) {
            stack.push(directoryIterator(next));
            continue;
          }
          if (ofInterest())
            return true;
        }
      }

      @Override public File next() {
        return next;
      }

      private boolean ofInterest() {
        for (@NotNull final String extension : extensions)
          if (next.getName().endsWith(extension))
            return true;
        return false;
      }
    }
  }
}
