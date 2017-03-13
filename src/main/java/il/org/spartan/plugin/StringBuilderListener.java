package il.org.spartan.plugin;

import static il.org.spartan.plugin.Listener.*;

import il.org.spartan.*;
import il.org.spartan.Separator;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.utils.*;

/** A kind of {@link Listener} that records a long string of the message it got.
 * @author Yossi Gil  {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
public class StringBuilderListener implements Listener {
  private static final Tab tab = new Tab();
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
    $.append(newId()).append(": ");
    as.list(os).forEach(λ -> $.append(new Separator(", ")).append(trivia.gist(λ)));
    $.append('\n');
  }
}