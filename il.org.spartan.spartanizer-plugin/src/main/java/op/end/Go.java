package op.end;


/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */

public class Go<S extends Events, Self extends Go<S, Self>> extends  op.begin.Go<S, Self> {
  @Override public Events.Delegator.Many<Events> listeners() {
    return null;
  }

  {
    /** Add a listener which calls end */
    
    
    listeners().add(new op.begin.Events.Listener.Default() {

      @Override public void begin() {
listeners.end();
      }});
  }
}
