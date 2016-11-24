package il.org.spartan.spartanizer.athenizer;

import org.eclipse.core.commands.*;

import il.org.spartan.plugin.*;

public class AthensHandler extends AbstractHandler {
  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    applicator().defaultSelection().go();
    return null;
  }

  private static AthensApplicator applicator() {
    final AthensApplicator $ = AthensApplicator.defaultApplicator();
    final Augmenter a = new Augmenter();
    $.setRunAction(¢ -> a.commitChanges(¢, $.selection()));
    // TODO Roth: remove dialog
    $.runContext(¢ -> {
      Dialogs.message("Athenising " + $.selection() + " selection").open();
      ¢.run();
    });
    return $;
  }
}
