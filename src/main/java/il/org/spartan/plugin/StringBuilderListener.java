package il.org.spartan.plugin;

import static il.org.spartan.plugin.Listener.*;

import il.org.spartan.utils.*;

/** A kind of {@link Listener} that records a long string of the message it got.
 * @author Yossi Gil
 * @since 2016 */
public class StringBuilderListener implements Listener {
  private static Tab tab = new Tab();

  private static String trim(final Object ¢) {
    return (¢ + "").substring(1, 35);
  }

  private final StringBuilder $ = new StringBuilder();

  public String $() {
    return $ + "";
  }

  @Override public void pop(final Object... ¢) {
    $.append(tab.end());
    Listener.super.pop(¢);
  }

  @Override public void push(final Object... ¢) {
    $.append(tab.begin());
    Listener.super.push(¢);
  }

  @Override public void tick(final Object... os) {
    $.append(newId() + ": ");
    for (final Object ¢ : os)
      $.append(new Separator(", ") + trim(¢));
    $.append('\n');
  }
}