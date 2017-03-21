package il.org.spartan.plugin;

import java.text.*;
import java.util.*;

/** Eclipse common utilities.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-21 */
public class Eclipse {
  // TODO Roth: switch from system user to eclipse template user
  /** @return user name */
  public static String user() {
    return System.getProperty("user.name");
  }

  /** @return current date */
  public static String date() {
    return date("dd/MM/yyyy");
  }

  // TODO Roth: switch from system date to eclipse template date
  /** @param format date format
   * @return current date */
  public static String date(final String format) {
    return new SimpleDateFormat(format).format(new Date());
  }
}
