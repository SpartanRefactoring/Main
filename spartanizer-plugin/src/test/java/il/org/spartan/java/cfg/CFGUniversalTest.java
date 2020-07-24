package il.org.spartan.java.cfg;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.Test;

import il.org.spartan.spartanizer.ast.navigate.wizard;

/** TODO dormaayn: document class
 * @author dormaayn
 * @since 2017-10-05 */
public class CFGUniversalTest {
  final String[] cases = { "{int x; x = 1;}" };
  final List<ASTNode> nodes = Arrays.stream(cases).map(x -> wizard.ast(x)).collect(Collectors.toList());
  @Focus(wrapper = true) final List<Map<?, ?>> properties = nodes.stream().map(x -> {
    x.setProperty("dummy1", "");
    x.setProperty("dummy2", "");
    return x.properties();
  }).collect(Collectors.toList());

  @Test public void test() {}
}
