package il.org.spartan.refactoring.preferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code> (original /
 *         2014/6/21)
 * @author Tomer Zeltzer <code><tomerr90 [at] gmail.com></code> (original /
 *         2014/6/21)
 * @since 2014/6/21 Preferences file handler. Getting file path, parsing it,
 *        etc...
 */
public class PreferencesFile {
  /**
   * @return Spartanization rules file header
   */
  public static String[] getSpartanTitle() {
    return new String[] { //
        "Preferences file for Spartanization rules profiles", //
        "Please avoid editing the file manually ", //
        "--------------------------------------------------" };
  }
  /**
   * @return Spartanization rules preferences file path.
   */
  public static String getPrefFilePath() {
    return SpartanizationPreferencePage.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "Sparta.pref";
  }
  /**
   * @return Preferences file as lines array
   */
  public static String[] parsePrefFile() {
    final String path = getPrefFilePath();
    if (!new File(path).exists())
      return null;
    try (final Scanner sc = new Scanner(new File(path))) {
      final List<String> $ = new ArrayList<>();
      while (sc.hasNextLine())
        $.add(sc.nextLine());
      return $.toArray(new String[0]);
    } catch (final FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }
}
