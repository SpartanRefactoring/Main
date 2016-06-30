package il.org.spartan.refactoring.utils;

import java.util.*;
import java.util.Map.Entry;

class ChainedHash<K, V> extends HashMap<K, LinkedList<V>> {
  /**
   * For serialization version control
   */
  private static final long serialVersionUID = -6008083316796560877L;

  public ChainedHash<K, V> chain(final K key, final V value) {
    if (!containsKey(key))
      put(key, new LinkedList<V>());
    get(key).add(value);
    return this;
  }
}

/**
 * The idea is to take a group of elements by key and value and get the common
 * unified group of values. What does it mean? You give me a group of elements,
 * e.g. -> {("a", 1), ("a", 2), ("b", 2), ("b", 3), ("c", 4)} And I'll give you
 * an iterator to a List containing: -> {1,2,3} {4} Why is that? ("a", 2) &
 * ("b", 2) is a bridge between "a" and "b" groups - therefore {1,2,3} are in
 * the same group. "c" is not linked to any other value - therefore he is alone
 * in the "c" group containing only 4. The contract is simple, you give me the
 * (key, value) group - and I'll give you the iterator to the list.
 *
 * @author Ofir Elmakias
 */
// Same notes - easier to read format:
// The idea is to take a group of elements by key and value and
// get the common unified group of values.
// What does it mean?
// You give me a group of elements, e.g.
// -> {("a", 1), ("a", 2), ("b", 2), ("b", 3), ("c", 4)}
// And I'll give you an iterator to a List containing:
// -> {1,2,3} {4}
// Why is that?
// ("a", 2) & ("b", 2) is a bridge between "a" and "b" groups -
// therefore {1,2,3} are in the same group.
// "c" is not linked to any other value - therefore he is alone in the "c"
// group containing only 4. The contract is simple, you give me the
// (key, value) group - and I'll give you the iterator to the list.
public class UnifiedGroup implements Iterable<LinkedList<Integer>> {
  final UnionFind unionFind;
  final ChainedHash<String, Integer> names = new ChainedHash<>();
  final int size;
  Base base = Base.OneBased; // Does the union find starts with 0 or 1

  /**
   * @param size - size of the group (number of lines)
   */
  public UnifiedGroup(final int size) {
    this.size = size;
    unionFind = new UnionFind(size);
  }

  /**
   * The counting base of the values - could be one or zero. The default is one
   * - because line representation in ASTnode.
   */
  public enum Base {
    /**
     * Zero based
     */
    ZeroBased,
    /**
     * one based
     */
    OneBased
  }

  /**
   * @param numOfLines - size of the group (number of lines)
   * @param countingBase - Counting base - type of the Base enum. Default value
   *          is OneBased.
   */
  public UnifiedGroup(final int numOfLines, final Base countingBase) {
    size = numOfLines;
    unionFind = new UnionFind(numOfLines);
    base = countingBase;
  }
  /**
   * @param name - The key, contains the name of the element in the line
   * @param line - The line in which the name exists
   * @return reference to this - for chaining
   */
  public UnifiedGroup add(final String name, final int line) {
    names.chain(name, Integer.valueOf(line));
    return this;
  }
  @SuppressWarnings("boxing") @Override public Iterator<LinkedList<Integer>> iterator() {
    final Iterator<Entry<String, LinkedList<Integer>>> it = names.entrySet().iterator();
    while (it.hasNext()) {
      final String name = it.next().getKey();
      final int first = names.get(name).getFirst().intValue();
      for (final int i : names.get(name))
        unionFind.union(first, i);
    }
    final ChainedHash<Integer, Integer> hm = new ChainedHash<>();
    for (int i = ast.bit(base == Base.OneBased); i < size; ++i)
      hm.chain(unionFind.find(i), i);
    return hm.values().iterator();
  }
  @Override public String toString() {
    final StringBuilder s = new StringBuilder();
    for (final List<Integer> l : this)
      s.append(l);
    return s.toString();
  }
}

class UnionFind {
  private final int[] parentOf; // parent for each number
  private final int[] sizeOf; // subtree dimension of the indexed element

  public UnionFind(final int size) {
    sizeOf = new int[size];
    Arrays.fill(sizeOf, 1);
    parentOf = new int[size];
    for (int i = 0; i < size; ++i)
      parentOf[i] = i;
  }
  public int union(final int n1, final int n2) {
    final int root1 = find(n1);
    final int root2 = find(n2);
    return root1 == root2 ? root1 : sizeOf[root1] < sizeOf[root2] ? hookOn(root1, root2) : hookOn(root2, root1);
  }
  private int hookOn(final int child, final int root) {
    sizeOf[root] += sizeOf[child];
    return parentOf[child] = root;
  }
  public int find(final int child) {
    final int size = parentOf.length;
    if (child < 0 || child >= size)
      throw new IndexOutOfBoundsException("Expected 0 to " + size + " but got " + child);
    // get the root
    int $ = child;
    while ($ != parentOf[$])
      $ = parentOf[$];
    // squeeze the path to the root
    squeeze(child, $);
    return $;
  }
  private void squeeze(final int from, final int root) {
    for (int child = from; child != root; child = parentOf[child])
      parentOf[child] = root;
  }
  @SuppressWarnings("boxing")//
  public static void main(final String[] args) {
    // TODO Ofir: Tests, should be made better by me,
    final ChainedHash<String, Integer> names = new ChainedHash<>();
    names.chain("bob", 2).chain("bob", 4).chain("bob", 8);
    names.chain("shraga", 1).chain("shraga", 3).chain("shraga", 5);
    for (final Integer i : names.get("bob"))
      System.out.print("Num: " + i + " ");
    final int N = 10;
    final UnionFind uf = new UnionFind(N);
    final int first = names.get("bob").getFirst();
    for (final Integer i : names.get("bob"))
      uf.union(first, i);
    final int first2 = names.get("shraga").getFirst();
    for (final Integer i : names.get("shraga"))
      uf.union(first2, i);
    uf.union(first, first2);
    final ChainedHash<Integer, Integer> hm = new ChainedHash<>();
    for (int i = 0; i < 10; ++i) {
      final int rootKey = uf.find(i);
      hm.chain(rootKey, i);
      System.out.print(uf.find(i));
    }
    final Iterator<Entry<Integer, LinkedList<Integer>>> it = hm.entrySet().iterator();
    while (it.hasNext()) {
      final Entry<Integer, LinkedList<Integer>> pair = it.next();
      System.out.println(pair.getKey() + " = " + pair.getValue());
    }
    System.out.println("================");
    /*****************/
    final UnifiedGroup ug2 = new UnifiedGroup(20);
    ug2.add("bob", 2).add("bob", 3).add("bob", 4).add("john", 8).add("g", 11).add("g", 12).add("bob", 12).add("john", 15);
    for (final List<Integer> l : ug2)
      System.out.println(">>>" + l.toString());
    final UnifiedGroup ug = new UnifiedGroup(6);
    ug.add("i", 1).add("k", 1).add("i", 2).add("j", 2).add("name", 3).add("s", 3).add("name", 4).add("name", 5).add("n", 5);
    for (final List<Integer> l : ug)
      System.out.println(">>>" + l.toString());
  }
}
