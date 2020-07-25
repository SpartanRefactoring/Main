package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.fragments;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;

import fluent.ly.as;
import il.org.spartan.spartanizer.ast.navigate.countOf;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.engine.Inliner;
import il.org.spartan.spartanizer.engine.collect;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class LocalInitializedStatement extends LocalInitialized {
  private static final long serialVersionUID = 1;

  @Override protected ASTNode[] span() {
    return as.array(current, nextStatement);
  }
  protected int waste() {
    return uses().size() * (Metrics.size(Inliner.protect(initializer)) - 1);
  }
  private List<SimpleName> uses() {
    return collect.usesOf(name).in(nextStatement);
  }
  protected int saving() {
    return countOf.nodes(!singleFragment() ? current : declaration);
  }
  private boolean singleFragment() {
    return fragments(declaration).size() == 1;
  }
}