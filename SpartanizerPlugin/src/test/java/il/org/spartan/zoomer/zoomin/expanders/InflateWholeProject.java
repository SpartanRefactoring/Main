package il.org.spartan.zoomer.zoomin.expanders;

import org.eclipse.core.commands.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.bloater.*;
import il.org.spartan.plugin.*;

@Deprecated
public class InflateWholeProject extends AbstractHandler {
  private static final int MAX_PASSES = 20;

  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    final Selection s = Selection.Util.current();
    for (final WrappedCompilationUnit ¢ : s.inner) {
      for (int i = 0; i < MAX_PASSES; ++i) {
        if (!SingleFlater.commitChanges(SingleFlater.in(¢.build().compilationUnit).from(new InflaterProvider()),
            ASTRewrite.create(¢.compilationUnit.getAST()), ¢, null, null, null))
          break;
        ¢.dispose();
      }
      ¢.dispose();
    }
    return null;
  }
}
