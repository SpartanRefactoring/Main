// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.files.visitors;

import java.io.*;

import org.eclipse.jdt.annotation.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.strings.*;

/** A <b>Utility</b> class, providing the service of determining whether a file
 * is a ZIP or other archive file.
 * @author Yossi Gil, the Technion.
 * @since 24/08/2008 */
@Utility
public enum Zip {
  ;
  /** A list of all recognized extensions of archive file names. */
  private static final String[] ZIP_FILE_EXTENSIONS = { ".ZIP", ".jar", ".war", ".ear" };

  /** @param ¢ a file to examine
   * @return <code><b>true</b></code> <em>iff</em>the file is appears to be a
   *         ZIP file. */
  public static boolean isZipFile( final File ¢) {
    return Suffixed.by(¢, ZIP_FILE_EXTENSIONS);
  }
}
