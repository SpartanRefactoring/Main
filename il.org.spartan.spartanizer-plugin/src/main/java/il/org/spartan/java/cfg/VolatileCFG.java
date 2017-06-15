package il.org.spartan.java.cfg;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-06-15 */
public class VolatileCFG extends CFG<VolatileCFG> {
  private static final int PRINT_THRESHOLD = 20;
  public final Map<ASTNode, List<ASTNode>> ins = new HashMap<>();
  public final Map<ASTNode, List<ASTNode>> outs = new HashMap<>();
  public final Collection<ASTNode> roots = new LinkedList<>();

  @Override public List<ASTNode> in(ASTNode n) {
    if (!ins.containsKey(n))
      ins.put(n, new LinkedList<>());
    return ins.get(n);
  }
  @Override public List<ASTNode> out(ASTNode n) {
    if (!outs.containsKey(n))
      outs.put(n, new LinkedList<>());
    return outs.get(n);
  }
  @Override public void acknowledgeRoot(ASTNode n) {
    roots.add(n);
  }
  @Override public String toString() {
    final StringBuilder $ = new StringBuilder();
    final Comparator<ASTNode> c = new Comparator<ASTNode>() {
      @Override public int compare(ASTNode o1, ASTNode o2) {
        return o1.getStartPosition() - o2.getStartPosition();
      }
    };
    final SortedMap<ASTNode, List<ASTNode>> sins = new TreeMap<>(c), souts = new TreeMap<>(c);
    sins.putAll(ins);
    souts.putAll(outs);
    $.append("* Roots *\n");
    $.append(shorten(roots) + "\n");
    $.append("* In *\n");
    for (ASTNode n : sins.keySet())
      $.append(shorten(n) + " <-\n\t" + shorten(sins.get(n)) + "\n");
    $.append("* Out *\n");
    for (ASTNode n : souts.keySet())
      $.append(shorten(n) + " ->\n\t" + shorten(souts.get(n)) + "\n");
    return $.toString();
  }
  private static String shorten(ASTNode n) {
    return n == null ? String.valueOf(null) : English.trimAbsolute(n.toString().replaceAll("\\s+", " "), PRINT_THRESHOLD, "...");
  }
  private static String shorten(Collection<ASTNode> list) {
    return list == null ? String.valueOf(null) : list.stream().map(x -> shorten(x)).collect(Collectors.toList()).toString();
  }
}
