package il.org.spartan.plugin.revision;

import java.util.*;

import il.org.spartan.spartanizer.dispatch.*;

/** An {@link Applicator} suitable for the command line.
 * @author Matteo Orru'
 * @param EventListener<event>
 * @since 2016 */
public class CommandLineApplicator extends Applicator<EventListener<event>> {

  private static final int PASSES_FEW = 1;
  private static final int PASSES_MANY = 20;
  
  /* (non-Javadoc)
   * @see il.org.spartan.plugin.revision.Applicator#go()
   */
  @Override public void go() {
    
    if(selection() == null || listener() == null || passes() <= 0 || selection().isEmpty())
      return;
    runContext().accept(() -> {
      final int l = passes();
      for (int pass = 0; pass < l; ++pass){
        final List<CU> alive = new LinkedList<>();
        alive.addAll(selection().compilationUnits);
        final List<CU> dead = new LinkedList<>();
        for (final CU ¢ : alive) {
          if(!runAction().apply(¢.build()).booleanValue())
            dead.add(¢);
        }
      }
    });
  }
  
  public static CommandLineApplicator defaultApplicator(){
    return new CommandLineApplicator().defaultSettings();
  }

  /**
   * @return this
   */
  
  private CommandLineApplicator defaultSettings() {
    return defaultListenerSilent().defaultPassesFew()
                                  .defaultRunContext()
                                  .defaultSelection();
  }

  /**
   * @return this
   */
  private CommandLineApplicator defaultSelection() {
    selection(CommandLineSelection.Util.get());
    return this;
  }

  /**
   * @return this
   */
  private CommandLineApplicator defaultRunContext() {
    runContext(r -> r.run());
    return this;
  }

  /**
   * @return this
   */
  private CommandLineApplicator defaultPassesFew() {
    passes(PASSES_FEW);
    return this;
  }
  
  /**
   * @return this
   */
  private CommandLineApplicator defaultPassesMany() {
    passes(PASSES_MANY);
    return this;
  }

  /**
   * @return this
   */
  private CommandLineApplicator defaultListenerSilent() {
    listener(EventListener.simpleListener(event.class, 
        e -> {
          // empty
        },  
        (e, o) -> {
          // empty
        }));
    return this;
  }

  /**
   * @return this
   */
  public CommandLineApplicator defaultRunAction() {
//    final Trimmer t = new Trimmer();
    final Spartanizer$Applicator s = new Spartanizer$Applicator();
    runAction(u -> Boolean.valueOf(s.apply(u, selection())));
    return this;
  }
  
}
