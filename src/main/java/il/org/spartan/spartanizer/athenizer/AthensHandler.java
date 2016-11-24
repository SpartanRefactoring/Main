package il.org.spartan.spartanizer.athenizer;

import org.eclipse.core.commands.*;

import il.org.spartan.plugin.*;

public class AthensHandler extends AbstractHandler {
  @Override public Object execute(@SuppressWarnings("unused") ExecutionEvent __) {
    Dialogs.message("ma ani sam ba intent?").open();
    return null;
  }
}
