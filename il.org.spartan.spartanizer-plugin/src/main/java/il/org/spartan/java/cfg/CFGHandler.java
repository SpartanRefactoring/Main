package il.org.spartan.java.cfg;

import org.eclipse.core.commands.*;

import il.org.spartan.spartanizer.plugin.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-06-15 */
public class CFGHandler extends AbstractHandler {
  @Override public Object execute(@SuppressWarnings("unused") ExecutionEvent __) {
    final Selection s = Selection.Util.current();
    if (s == null || s.inner.isEmpty())
      return null;
    CFG<?> cfg = new VolatileCFG();
    cfg.register(CFGBuilders.all(cfg));
    cfg.compute(s.inner.get(0).build().compilationUnit);
    System.out.println(cfg);
    return null;
  }
}
