package il.org.spartan.spartanizer.plugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;

import il.org.spartan.plugin.preferences.revision.PreferencesPage;
import il.org.spartan.spartanizer.research.analyses.notation;

/** Handler for the shortcut of changing the spartanizar prefrence from cent to
 * it.
 * @author dormaayn
 * @since 2017-05-05 */
public class CentToParam extends AbstractHandler {
  @Override public Object execute(final ExecutionEvent ¢) {
    if (!"¢".equals(notation.cent))
      PreferencesPage.changeBackToCent();
    else
      PreferencesPage.changeCentToParam();
    return new TopMenuHandlers().execute(¢);
  }
}
