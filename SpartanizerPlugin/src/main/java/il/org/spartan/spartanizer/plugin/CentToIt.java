package il.org.spartan.spartanizer.plugin;

import org.eclipse.core.commands.*;

import il.org.spartan.plugin.preferences.revision.*;

/** Handler for the shortcut of changing the spartanizar prefrence from cent to
 * it.
 * @author dormaayn
 * @since 2017-05-05 */
public class CentToIt extends AbstractHandler {
  @Override public Object execute(final ExecutionEvent ¢) {
    System.out.println("Strted Press");
    PreferencesPage.changeCentToIt();
    return new TopMenuHandlers().execute(¢);
  }
}
