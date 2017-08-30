package op.begin;

import fluent.ly.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */
public class Go<S extends Events, Self extends Go<S, Self>> implements Selfie<Self> {
  private final Events.Delegator.Many<Events> listeners = new Events.Delegator.Many<>();

  public Events.Delegator.Many<Events> listeners() {
    return listeners;
  }
  public final Self withListener(Events.Listener ¢) {
    listeners().add(¢);
    return self();
  }
  public final <T> T go() {
    listeners().begin();
    return the.nil();
  }
}
