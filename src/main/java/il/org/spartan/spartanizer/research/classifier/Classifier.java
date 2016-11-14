package il.org.spartan.spartanizer.research.classifier;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.util.*;

/** @author Ori Marcovitch
 * @since Nov 13, 2016 */
public class Classifier extends ASTVisitor {
  final Map<String, List<ForStatement>> forLoops = new HashMap<>();
  final Map<String, List<EnhancedForStatement>> enhancedForLoops = new HashMap<>();
  static final Scanner input = new Scanner(System.in);

  @Override public boolean visit(final ForStatement node) {
    System.out.println(normalize.codeFragment(node + ""));
    final String classification = input.nextLine();
    forLoops.putIfAbsent(classification, new ArrayList<>());
    forLoops.get(classification).add(node);
    return super.visit(node);
  }
  @Override public boolean visit(final EnhancedForStatement node) {
    System.out.println(normalize.codeFragment(node + ""));
    final String classification = input.nextLine();
    enhancedForLoops.putIfAbsent(classification, new ArrayList<>());
    enhancedForLoops.get(classification).add(node);
    return super.visit(node);
  }
  public void summarize() {
    System.out.println(forLoops);
    System.out.println(enhancedForLoops);
  }
}
