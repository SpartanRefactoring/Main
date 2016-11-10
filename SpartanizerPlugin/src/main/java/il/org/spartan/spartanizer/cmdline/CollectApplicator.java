package il.org.spartan.spartanizer.cmdline;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.plugin.*;

/** This is an applicator that collects data of a project
 * @author Matteo Orru */
public class CollectApplicator extends Applicator {
  private static Collect$Applicator a = new Collect$Applicator();
  private final int PASSES_FEW = 1;

  @Override public void go() {
    if (selection() != null && listener() != null && passes() > 0 && !selection().isEmpty())
      for (final CompilationUnit ¢ : ((CommandLineSelection) selection()).getCompilationUnits()) {
        assert ¢ != null;
        a.go(¢);
      }
  }
  public static Applicator defaultApplicator() {
    return new CollectApplicator().defaultSettings();
  }
  /** @return this */
  private Applicator defaultSettings() {
    return defaultListenerSilent().defaultPassesFew();
    // .defaultRunContext()
    // .defaultSelection()
    // .defaultRunAction();
  }
  /** @return this */
  private CollectApplicator defaultPassesFew() {
    passes(PASSES_FEW);
    return this;
  }
  /** @return this */
  private CollectApplicator defaultListenerSilent() {
    listener((final Object... __) -> {
      //
    });
    // listener(EventListener.simpleListener(event.class,
    // e -> {
    // // empty
    // },
    // (e, o) -> {
    // // empty
    // }));
    return this;
  }
}
