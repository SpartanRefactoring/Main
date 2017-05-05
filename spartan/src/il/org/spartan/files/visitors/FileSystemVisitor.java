// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.files.visitors;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import org.jetbrains.annotations.*;

import fluent.ly.*;
import il.org.spartan.files.visitors.FileSystemVisitor.Action.*;
import il.org.spartan.files.visitors.FindClassFile.*;
import il.org.spartan.strings.*;
import il.org.spatan.iteration.*;

/** A class realizing a file system traversal algorithm, including delving into
 * archives such as ZIP and JAR files.
 * <p>
 * The traversal is carried out by calling the class constructor
 * {@link FileSystemVisitor#FileSystemVisitor(String[], il.org.spartan.files.visitors.FileSystemVisitor.Action, String[])}
 * to set up the traversal parameters, and then function {@link #go()} to
 * conduct the actual traversal.
 * <p>
 * @author Yossi Gil
 * @since 21/05/2007
 * @see #go()
 * @see FileSystemVisitor#FileSystemVisitor(String[],
 *      il.org.spartan.files.visitors.FileSystemVisitor.Action, String[])
 * @see Action */
public class FileSystemVisitor {
  @NotNull private static Iterable<File> asFiles(@NotNull final Iterable<String> fileNames) {
    @NotNull final List<File> $ = new ArrayList<>();
    for (@NotNull final String fileName : fileNames)
      $.add(new File(fileName));
    return $;
  }
  @NotNull private static Iterable<File> asFiles(final String... fileNames) {
    return asFiles(Iterables.toList(fileNames));
  }

  /** at which directories in the file system should the traversal start? */
  private final Iterable<File> from;
  /** what should be done for each file traversed? */
  private final Action visitor;
  /** for which extensions should the {@link Action} object be invoked. */
  private final String[] extensions;

  /** Create a new visitor object to scan an array of {@link File}s naming the
   * search locations.
   * @param from a non-<code><b>null</b></code> specifying the directory or
   *        archive file where the traversal should begin.
   * @param visitor a non-<code><b>null</b></code> specifying what to do in each
   *        visited file
   * @param extensions an array of non-<code><b>null</b></code> {@link String} s
   *        specifying which file extensions the visitor should call, e.g.,
   *        ".class", ".csv", etc. If this parameter is
   *        <code><b>null</b></code>, or of length 0, or contains a
   *        {@link String} of length 0, then the visitor is invoked for all
   *        files found in the supplied path.
   * @see #go
   * @see #FileSystemVisitor(Collection,
   *      il.org.spartan.files.visitors.FileSystemVisitor.Action, String[]) */
  public FileSystemVisitor(final File from, final Action visitor, final String... extensions) {
    this(visitor, Iterables.toList(from), extensions);
  }
  /** Create a new visitor object to scan an array of {@link File}s naming the
   * search locations.
   * @param from a non-<code><b>null</b></code> specifying the directories or
   *        archive files where the traversal should begin.
   * @param visitor a non-<code><b>null</b></code> specifying what to do in each
   *        visited file
   * @param extensions an array of non-<code><b>null</b></code> {@link String} s
   *        specifying which file extensions the visitor should call, e.g.,
   *        ".class", ".csv", etc. If this parameter is
   *        <code><b>null</b></code>, or of length 0, or contains a
   *        {@link String} of length 0, then the visitor is invoked for all
   *        files found in the supplied path.
   * @see #go
   * @see #FileSystemVisitor(Collection,
   *      il.org.spartan.files.visitors.FileSystemVisitor.Action, String[]) */
  public FileSystemVisitor(final File[] from, final Action visitor, final String... extensions) {
    this(visitor, Iterables.toList(from), extensions);
  }
  /** Create a new visitor object to scan an array of {@link String}s naming the
   * search locations.
   * @param from a non-<code><b>null</b></code> specifying where the traversal
   *        should begin. If this array is of length 0, then no traversal shall
   *        be carried out. Also, if the supplied directories are not disjoint,
   *        then the same file may be visited more than once.
   * @param visitor a non-<code><b>null</b></code> specifying what to do in each
   *        visited file
   * @param extensions an array of non-<code><b>null</b></code> {@link String} s
   *        specifying which file extensions the visitor shold call, e.g.,
   *        ".class", ".csv", etc. If this parameter is
   *        <code><b>null</b></code>, or of length 0, or contains a
   *        {@link String} of length 0, then the visitor is invoked for all
   *        files found in the supplied path.
   * @see #go
   * @see #FileSystemVisitor(Collection,
   *      il.org.spartan.files.visitors.FileSystemVisitor.Action, String[]) */
  public FileSystemVisitor(@NotNull final Iterable<String> from, final Action visitor, final String... extensions) {
    this(visitor, asFiles(from), extensions);
  }
  /** Create a new visitor object to scan a single directory.
   * @param from a non-<code><b>null</b></code> specifying where the traversal
   *        should begin.
   * @param visitor a non-<code><b>null</b></code> specifying what to do in each
   *        visited file
   * @param extensions an array of non-<code><b>null</b></code> {@link String} s
   *        specifying which file extensions the visitor shold call, e.g.,
   *        ".class", ".csv", etc. If this parameter is
   *        <code><b>null</b></code>, or of length 0, or contains a
   *        {@link String} of length 0, then the visitor is invoked for all
   *        files found in the supplied path.
   * @see #go
   * @see #FileSystemVisitor(Collection,
   *      il.org.spartan.files.visitors.FileSystemVisitor.Action, String[]) */
  public FileSystemVisitor(final String from, final Action visitor, final String[] extensions) {
    this(new String[] { from }, visitor, extensions);
  }
  /** Create a new visitor object to scan a {@link String} naming the search
   * location.
   * @param from a non-<code><b>null</b></code> specifying where the traversal
   *        should begin.
   * @param visitor a non-<code><b>null</b></code> specifying what to do in each
   *        visited file
   * @param extensions an array of non-<code><b>null</b></code> {@link String} s
   *        specifying which file extensions the visitor shold call, e.g.,
   *        ".class", ".csv", etc. If this parameter is
   *        <code><b>null</b></code>, or of length 0, or contains a
   *        {@link String} of length 0, then the visitor is invoked for all
   *        files found in the supplied path.
   * @see #go
   * @see #FileSystemVisitor(Collection,
   *      il.org.spartan.files.visitors.FileSystemVisitor.Action, String[]) */
  public FileSystemVisitor(final String from, final Searcher visitor, final String... extensions) {
    this(visitor, asFiles(from), extensions);
  }
  /** Create a new visitor object to scan an array of {@link String}s naming the
   * search locations.
   * @param from a non-<code><b>null</b></code> specifying where the traversal
   *        should begin. If this array is of length 0, then no traversal shall
   *        be carried out. Also, if the supplied directories are not disjoint,
   *        then the same file may be visited more than once.
   * @param visitor a non-<code><b>null</b></code> specifying what to do in each
   *        visited file
   * @param extensions an array of non-<code><b>null</b></code> {@link String} s
   *        specifying which file extensions the visitor shold call, e.g.,
   *        ".class", ".csv", etc. If this parameter is
   *        <code><b>null</b></code>, or of length 0, or contains a
   *        {@link String} of length 0, then the visitor is invoked for all
   *        files found in the supplied path.
   * @see #go
   * @see #FileSystemVisitor(Collection,
   *      il.org.spartan.files.visitors.FileSystemVisitor.Action, String[]) */
  public FileSystemVisitor(final String[] from, final Action visitor, final String... extensions) {
    this(visitor, asFiles(from), extensions);
  }
  /** Create a new visitor object to scan an array of {@link File}s naming the
   * search locations.
   * @param from a non-<code><b>null</b></code> specifying the names of the
   *        directories where the traversal should begin. If this array is of
   *        length 0, then no traversal shall be carried out. Also, if the
   *        supplied directories are not disjoint, then the same file may be
   *        visited more than once.
   * @param visitor a non-<code><b>null</b></code> specifying what to do in each
   *        visited file
   * @param extensions an array of non-<code><b>null</b></code> {@link String} s
   *        specifying which file extensions the visitor should call, e.g.,
   *        ".class", ".csv", etc. If this parameter is
   *        <code><b>null</b></code>, or of length 0, or contains a
   *        {@link String} of length 0, then the visitor is invoked for all
   *        files found in the supplied path.
   * @see #go
   * @see #FileSystemVisitor(Collection,
   *      il.org.spartan.files.visitors.FileSystemVisitor.Action, String[]) */
  private FileSystemVisitor(final Action visitor, final Iterable<File> from, final String... extensions) {
    this.from = from;
    this.visitor = visitor;
    this.extensions = extensions;
  }
  /** Conduct the traversal. For each file encountered during the traversal, the
   * {@link FileSystemVisitor} invokes one of
   * <ol>
   * <li>{@link Action#visitFile(File)},
   * <li>{@link Action#visitDirectory(File)},
   * <li>{@link #visitZipEntry(String, String, InputStreamReader)}, or
   * <li>{@link Action#visitZip(File)}
   * </ol>
   * functions, depending on the file type. If this function throws
   * {@link Action.StopTraversal} exception, then the traversal stops:
   * completely if the current file was a plain file, i.e.,
   * {@link Action#visitFile(File)} or
   * {@link #visitZipEntry(String, String, InputStreamReader)} was called, or
   * just of the contained files, if this was an archive or a directory file.
   * @throws IOException if the file system could not traversed for some reason
   * @throws StopTraversal if the visitor object requested to stop the
   *         visitation. */
  public void go() throws IOException, StopTraversal {
    for (@NotNull final File ¢ : from)
      recurse(¢);
  }
  /** Conduct recursive traversal starting at a given file
   * @param ¢ a file, which may be a directory, a ZIP, or a plain file, at which
   *        the traversal begins
   * @throws IOException if the file system could not traversed for some reason
   * @throws StopTraversal if the visitor object requested to stop the
   *         visitation. */
  private void recurse(@NotNull final File ¢) throws IOException, StopTraversal {
    if (¢.isDirectory())
      recurseDirectory(¢);
    else if (Zip.isZipFile(¢))
      scanZip(¢);
    else if (Suffixed.by(¢, extensions))
      visitor.visitFile(¢);
  }
  /** conduct recursive traversal of a directory
   * @param d a directory
   * @throws IOException if the file system could not be traversed for some
   *         reason */
  private void recurseDirectory(@NotNull final File d) throws IOException {
    try {
      visitor.visitDirectory(d);
      if (d.list() == null) // Weird directories such as
        // "System Volume Information"
        return;
      for (@Nullable final String name : d.list())
        if (name != null)
          recurse(new File(d, name));
    } catch (@NotNull final Action.StopTraversal __) {
      // do not visit children of this directory
    }
  }
  /** Scan entries of a ZIP file.
   * @param f a ZIP or other archive file
   * @throws StopTraversal if the visitor object requested to stop the
   *         visitation. However, if the visitor requested to stop the
   *         visitation of the ZIP file itself, the scanning of this ZIP file
   *         will stop, but the no exception is thrown, and the entire traversal
   *         continue. */
  private void scanZip(@NotNull final File f) throws StopTraversal {
    try {
      visitor.visitZip(f);
    } catch (@NotNull final Action.StopTraversal e) {
      return; // do not visit any elements of this ZIP file, but continue
      // traversal.
    }
    try (@NotNull ZipFile Z = new ZipFile(f.getAbsoluteFile())) {
      for (final Enumeration<? extends ZipEntry> es = Z.entries(); es.hasMoreElements();) {
        final ZipEntry e = es.nextElement();
        try {
          final InputStream is = Z.getInputStream(e);
          if (e.isDirectory()) {
            visitor.visitZipDirectory(Z.getName(), e.getName(), is);
            continue;
          }
          if (Suffixed.by(e.getName(), extensions))
            visitor.visitZipEntry(Z.getName(), e.getName(), is);
          is.close();
        } catch (@NotNull final StopTraversal x) {
          System.out.println("Found at ZIP!!!");
          throw x;
        } catch (@NotNull final IOException ¢) {
          System.err.println("Error reading " + Z + ": " + ¢.getMessage());
        }
      }
    } catch (@NotNull final IOException ¢) {
      System.err.println(f.getAbsolutePath() + ": " + ¢.getMessage());
    }
  }

  /** @author Yossi Gil
   * @since 21/05/2007 */
  public interface Action {
    /** action to conduct for each directory encountered throught the traversal.
     * @param f the directory file object
     * @throws StopTraversal in case the visitor wishes to stop the traversal of
     *         this directory.
     * @see #visitFile(File)
     * @see #visitZip(File)
     * @see #visitZipEntry(String,String,InputStream) */
    void visitDirectory(File f) throws StopTraversal;
    /** action to conduct for each ordinary, i.e., non-ZIP and non-directory,
     * file.
     * @param f the file to visit
     * @throws StopTraversal in case the visitor wishes to <i>completely</i>
     *         stop the traversal
     * @see #visitDirectory(File)
     * @see #visitZip(File) */
    void visitFile(File f) throws StopTraversal;
    /** action to conduct for each ZIP and other archive files encountered
     * throughout the traversal.
     * @param f the archive file object
     * @throws StopTraversal in case the visitor wishes to stop the traversal of
     *         this archive file.
     * @see #visitFile(File)
     * @see #visitDirectory(File) */
    void visitZip(File f) throws StopTraversal;
    /** action to conduct for each directory encountered in an archival file
     * scanned through the traversal.
     * @param zipName the name of the ZIP file from which this entry was taken
     * @param entryName the name of the visited entry in the ZIP file
     * @param s an open stream into the content of this entry
     * @throws StopTraversal in case the visitor wishes to terminate the entire
     *         traversal process
     * @see #visitFile(File)
     * @see #visitDirectory(File) */
    void visitZipDirectory(String zipName, String entryName, InputStream s) throws StopTraversal;
    /** action to conduct for each entry found in a ZIP file, encountered
     * throughout the traversal
     * @param zipName the name of the ZIP file from which this entry was taken
     * @param entryName the name of the visited entry in the ZIP file
     * @param s an open stream into the content of this entry
     * @throws StopTraversal in case the visitor wishes to terminate the entire
     *         traversal process */
    void visitZipEntry(String zipName, String entryName, InputStream s) throws StopTraversal;

    /** @author Yossi Gil
     * @since 21/05/2007 */
    class StopTraversal extends Exception {
      private static final long serialVersionUID = -0x40A795EBEA740DB0L;

      /** Create a new {@link StopTraversal} object */
      public StopTraversal() {}
      /** Create a new {@link StopTraversal} object with a specific message
       * @param message a message to record
       * @see Exception */
      public StopTraversal(final String message) {
        super(message);
      }
    }
  }

  /** A simplified {@link Action} with no exceptions thrown whose implementation
   * does absolutely nothing. It leaves it to the extending class to
   * re-implement concrete actions.
   * @author Yossi Gil
   * @since 18/06/2007 */
  public abstract static class EmptyAction implements NonStopAction {
    /** A do-nothing function, ignoring its arguments
     * @param __ ignored
     * @see il.org.spartan.files.visitors.FileSystemVisitor.Action#visitDirectory(java.io.File) */
    @Override public void visitDirectory(final File __) {
      forget.it(__);
    }
    /** A do-nothing function, ignoring its arguments
     * @param __ ignored
     * @see il.org.spartan.files.visitors.FileSystemVisitor.Action#visitFile(java.io.File) */
    @Override public void visitFile(final File __) {
      forget.it(__);
    }
    /** A do-nothing function, ignoring its arguments
     * @param __ ignored
     * @throws StopTraversal
     * @see il.org.spartan.files.visitors.FileSystemVisitor.Action#visitZip(java.io.File) */
    @Override public void visitZip(final File __) throws StopTraversal {
      forget.it(__);
    }
    /** A do-nothing function, ignoring its arguments
     * @param __ ignored
     * @param ____ ignored
     * @param ______ ignored
     * @see il.org.spartan.files.visitors.FileSystemVisitor.Action#visitZipDirectory(java.lang.String,
     *      java.lang.String, java.io.InputStream) */
    @Override public void visitZipDirectory(final String __, final String ____, final InputStream ______) {
      forget.em(__, ____, ______);
    }
    /** A do-nothing function, ignoring its arguments
     * @param __ ignored
     * @param ____ ignored
     * @param ______ ignored
     * @see il.org.spartan.files.visitors.FileSystemVisitor.Action#visitZipEntry(java.lang.String,
     *      java.lang.String, java.io.InputStream) */
    @Override public void visitZipEntry(final String __, final String ____, final InputStream ______) {
      forget.em(__, ____, ______);
    }
  }

  /** An <code><b>abstract</b></code> class, to be extended by those clients
   * interested in examining files only during a file system traversal as
   * carried out by class {@link FileSystemVisitor}. Such clients must give body
   * to two functions only:
   * <ol>
   * <li>{@link #visitFile(File)} - action to carry out on ordinary files,
   * <li>{@link #visitZipEntry(String, InputStream)} - action for files found in
   * an archive.
   * </ol>
   * Class {@link FileOnlyAction} is in fact a Facade offering a simplified
   * interface to the more general {@link Action}. Simplifications are:
   * <ol>
   * <li>no exceptions thrown
   * <li>partial implementation that does nothing for directories and archives,
   * and leaves it to the extending class to implement a concrete action for
   * files and archive entries visitation.
   * <li>In visiting archive entries it invokes function {@link #visitZip(File)}
   * (instead of {@link #visitZipDirectory(String, String, InputStream)}), i.e.,
   * the implementor of this function does is not bothered with the archive
   * name.
   * </ol>
   * @author Yossi Gil
   * @since 18/06/2007 */
  public abstract static class FileOnlyAction extends EmptyAction {
    @Override public abstract void visitFile(File f);
    public abstract void visitZipEntry(String entryName, InputStream s);
    /** Not to be used by clients.
     * @see il.org.spartan.files.visitors.FileSystemVisitor.EmptyAction#visitZipEntry(java.lang.String,
     *      java.lang.String, java.io.InputStream) */
    @Override public void visitZipEntry(final String zipName, final String entryName, final InputStream s) {
      forget.it(zipName);
      visitZipEntry(entryName, s);
    }
  }

  /** An <code><b>abstract</b></code> class, to be extended by those clients
   * interested in examining plain files only, i.e., files not contained in
   * archive, during a file system traversal as carried out by class
   * {@link FileSystemVisitor} . Such clients must give body to one function
   * only:
   * <ol>
   * <li>{@link #visitFile(File)} - action to carry out on ordinary files
   * </ol>
   * Class {@link PlainFileOnlyAction} is in fact a Facade offering a simplified
   * interface to the more general {@link Action} . Simplifications are:
   * <ol>
   * <li>no exceptions thrown
   * <li>partial implementation that does nothing for directories and archives,
   * and leaves it to the extending class to implement a concrete action for
   * files and archive entries visitation.
   * <li>In visiting archive entries it invokes function {@link #visitZip(File)}
   * (instead of {@link #visitZipDirectory(String,String,InputStream)} ), i.e.,
   * the implementor of this function does is not bothered with the archive
   * name.
   * </ol>
   * @author Yossi Gil
   * @since 16/05/2011 */
  public abstract static class PlainFileOnlyAction extends FileOnlyAction {
    @Override public abstract void visitFile(File f);
    @Override public final void visitZipEntry(final String entryName, final InputStream s) {
      forget.em(entryName, s);
    }
  }

  /** An <code><b>abstract</b></code> class, to be extended by those clients
   * interested in examining directories only during a file system traversal as
   * carried out by class {@link FileSystemVisitor}. Such clients must give body
   * to two functions only:
   * <dl>
   * <dt>{@link #visitDirectory(File)}</dt>
   * <dd>Action for ordinary directories</dd>
   * <dt>{@link #visitZipDirectory(String, InputStream)}</dt>
   * <dd>Action for directories found in an archive.</dd>
   * </dl>
   * <p>
   * Class {@link DirectoryOnlyAction} is in fact a Facade offering a simplified
   * interface to the more general {@link Action}. Simplifcation are:
   * <ol>
   * <li>no exceptions thrown
   * <li>partial implementation that does nothing for files and archives, and
   * leaves it to the extending class to implement concrete a concrete action
   * for directories and archived directories encountered in the course of the
   * visitation.
   * <li>in visiting directories found in archive entries it uses the
   * {@link #visitZipDirectory(String, InputStream)} (instead of the more
   * general {@link #visitZipDirectory(String, String, InputStream)}), that is,
   * the implementor is not supplied with the directory name.
   * </ol>
   * @author Yossi Gil
   * @since 18/06/2007 */
  protected abstract static class DirectoryOnlyAction extends EmptyAction {
    /** A function to be realized by an extending class, with actions to be
     * carried out for each encountered directory.
     * @param d the directory currently being visited.
     * @see il.org.spartan.files.visitors.FileSystemVisitor.EmptyAction#visitDirectory(java.io.File) */
    @Override public abstract void visitDirectory(File d);
    /** Not to be called by clients
     * @see il.org.spartan.files.visitors.FileSystemVisitor.EmptyAction#visitZipDirectory(java.lang.String,
     *      java.lang.String, java.io.InputStream) */
    @Override public final void visitZipDirectory(final String zipName, final String entryName, final InputStream s) {
      forget.em(zipName);
      visitZipDirectory(entryName, s);
    }
    /** Visit a directory entry contained in an archive.
     * @param entryName the name of the discovered directory
     * @param s to be used for opening it if necessary */
    protected abstract void visitZipDirectory(String entryName, InputStream s);
  }

  interface NonStopAction extends Action {
    @Override void visitDirectory(File f);
    @Override void visitFile(File f);
    @Override void visitZip(File f) throws StopTraversal;
    @Override void visitZipDirectory(String zipName, String entryName, InputStream s);
    @Override void visitZipEntry(String zipName, String entryName, InputStream s);
  }
}
