package il.org.spartan.spartanizer.java.namespace;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;
import java.util.Map.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Interface to environment. Holds all the names defined till current PC. In
 * other words the 'names Environment' at every point of the program tree. */
public interface Environment {
  /** @return true iff {@link Environment} doesn't have an entry with a given
   *         name. */
  default boolean doesntHave(final String name) {
    return !has(name);
  }
  static Environment of(ASTNode n) {
    ASTNode root = n.getRoot();
    if (iz.compilationUnit(root))
      return of(az.compilationUnit(root));
    return null;
  }
  static Nested of(CompilationUnit u) {
    return Environment.EMPTY.spawn().visit(u);
  }


  /** Return true iff {@link Environment} is empty. */
  boolean empty();

  default List<Entry<String, Binding>> fullEntries() {
    final List<Entry<String, Binding>> $ = new ArrayList<>(entries());
    if (nest() != null)
      $.addAll(nest().fullEntries());
    return $;
  }

  List<Entry<String, Binding>> entries();

  /** Get full path of the current {@link Environment} (all scope hierarchy).
   * Used for full names of the variables. */
  default String fullName() {
    final String $ = nest() == null || nest() == EMPTY ? null : nest().fullName();
    return ($ == null ? "" : $ + ".") + name();
  }

  /** @return all the full names of the {@link Environment}. */
  default LinkedHashSet<String> fullNames() {
    final LinkedHashSet<String> $ = new LinkedHashSet<>(keys());
    if (nest() != null)
      $.addAll(nest().fullNames());
    return $;
  }

  default int fullSize() {
    return size() + (nest() == null ? 0 : nest().fullSize());
  }

  /** @return null iff the name is not in use in the {@link Environment} */
  default Binding get(@SuppressWarnings("unused") final String name) {
    return null;
  }

  /** Answer the question whether the name is in use in the current
   * {@link Environment} */
  boolean has(final String name);

  /** @return null iff the name is not hiding anything from outer scopes,
   *         otherwise Information about hided instance (with same name) */
  default Binding hiding(final String name) {
    return nest() == null ? null : nest().get(name);
  }

  String name();

  /** @return The names used in the current scope. */
  Set<String> keys();

  /** @return null at the most outer block. This method is similar to the
   *         'next()' method in a linked list. */
  Environment nest();

  /** Should return the hidden entry, or null if no entry hidden by this one.
   * Note: you will have to assume multiple definitions in the same block, this
   * is a compilation error, but nevertheless, let a later entry with of a
   * certain name to "hide" a former entry with the same name. */
  default Binding put(final String name, final Binding b) {
    throw new IllegalArgumentException(name + "/" + b);
  }

  int size();

  /** Used when new definition block (scope) is opened. */
  default Nested spawn() {
    return addChild(new Nested(this));
  }

  Nested addChild(Nested child);

  /** Used when new definition block (scope) is opened. */
  default Environment spawn(String name) {
    return new Nested(this,name);
  }

  /** The Environment structure is in some like a Linked list, where EMPTY is
   * like the NULL at the end. */
  Environment EMPTY = new Environment() {
    @Override public boolean empty() {
      return true;
    }

    @Override public boolean has(@SuppressWarnings("unused") String name) {
      return false;
    }

    @Override public List<Entry<String, Binding>> entries() {
      return Collections.emptyList();
    }

    @Override public String name() {
      return "";
    }

    @Override public Set<String> keys() {
      return Collections.emptySet();
    }

    @Override public Environment nest() {
      return null;
    }

    @Override public int size() {
      return 0;
    }

    @Override public Nested addChild(@SuppressWarnings("unused") Nested child) {
      return null;
    }
  };
  LinkedHashSet<Entry<String, Binding>> upEnv = new LinkedHashSet<>();

  static Binding makeBinding(final VariableDeclarationFragment ¢, final type t) {
    return new Binding(¢.getParent(), getHidden(fullName(¢.getName())), ¢, t);
  }

  /** @param ¢ JD
   * @return All declarations in given {@link Statement}, without entering the
   *         contained ({@link Block}s. If the {@link Statement} is a
   *         {@link Block}, (also IfStatement, ForStatement and so on...) return
   *         empty Collection. */
  static List<Entry<String, Binding>> declarationsOf(final Statement ¢) {
    final List<Entry<String, Binding>> $ = new ArrayList<>();
    switch (¢.getNodeType()) {
      case VARIABLE_DECLARATION_STATEMENT:
        $.addAll(declarationsOf(az.variableDeclrationStatement(¢)));
        break;
      default:
        return $;
    }
    return $;
  }

  static List<Entry<String, Binding>> declarationsOf(final VariableDeclarationStatement s) {
    final List<Entry<String, Binding>> $ = new ArrayList<>();
    final type t = type.baptize(wizard.condense(s.getType()));
    final String path = fullName(s);
    $.addAll(fragments(s).stream().map(¢ -> new MapEntry<>(path + "." + ¢.getName(), makeBinding(¢, t))).collect(Collectors.toList()));
    return $;
  }

  /** @return set of entries declared in the node, including all hiding. */
  static LinkedHashSet<Entry<String, Binding>> declaresDown(final ASTNode ¢) {
    // Holds the declarations in the subtree and relevant siblings.
    final LinkedHashSet<Entry<String, Binding>> $ = new LinkedHashSet<>();
    ¢.accept(new EnvironmentVisitor($));
    return $;
  }

  /** Gets declarations made in ASTNode's Ancestors */
  static LinkedHashSet<Entry<String, Binding>> declaresUp(final ASTNode n) {
    for (Block PB = getParentBlock(n); PB != null; PB = getParentBlock(PB))
      for (final Statement ¢ : statements(PB))
        upEnv.addAll(declarationsOf(¢));
    return upEnv;
  }

  static String fullName(final ASTNode ¢) {
    return ¢ == null ? "" : fullName(¢.getParent()) + name(¢);
  }

  /** Spawns the first nested {@link Environment}. Should be used when the first
   * block is opened. */
  static Environment genesis() {
    return EMPTY.spawn();
  }

  static Binding get(final LinkedHashSet<Entry<String, Binding>> ss, final String s) {
    for (final Entry<String, Binding> $ : ss)
      if (s.equals($.getKey()))
        return $.getValue();
    return null;
  }

  static Binding getHidden(final String s) {
    final String shortName = s.substring(s.lastIndexOf(".") + 1);
    for (String ¢ = parentNameScope(s); !"".equals(¢); ¢ = parentNameScope(¢)) {
      final Binding $ = get(upEnv, ¢ + "." + shortName);
      if ($ != null)
        return $;
    }
    return null;
  }

  static Block getParentBlock(final ASTNode ¢) {
    return az.block(¢.getParent());
  }

  static String name(@SuppressWarnings("unused") final ASTNode __) {
    return "???";
  }

  static String name(final VariableDeclarationFragment ¢) {
    return ¢.getName() + "";
  }

  static String parentNameScope(final String ¢) {
    assert "".equals(¢) || ¢.lastIndexOf(".") != -1 : "nameScope malfunction!";
    return "".equals(¢) ? "" : ¢.substring(0, ¢.lastIndexOf("."));
  }

  /** @return set of entries used in a given node. this includes the list of
   *         entries that were defined in the node */
  static LinkedHashSet<Entry<String, Binding>> uses(@SuppressWarnings("unused") final ASTNode __) {
    return new LinkedHashSet<>();
  }

  /** Dictionary with a parent. Insertions go the current node, searches start
   * at the current note and Delegate to the parent unless it is null. */
  final class Nested implements Environment {
    public final String name;
    public final Environment nest;
    public final Map<String, Binding> flat = new LinkedHashMap<>();
    public final List<Environment> children = new ArrayList<>();

    Nested(final Environment nest) {
      this(nest, "");
    }

    Nested visit(ASTNode n) {
      n.accept(new ASTVisitor() {
        @Override
        public boolean preVisit2(ASTNode ¢) {
          if (iz.statement(¢)) {
            return true;
          }
          if (iz.expression(¢))
            return true;
          return true;
        }
        
      });
      
      return this;
    }

    public Nested(Environment nest, String name) {
      this.nest = nest;
      this.name = name;
    }


    /** @return <code><b>true</b></code> <em>iff</em> {@link Environment} is
     *         empty. */
    @Override public boolean empty() {
      return flat.isEmpty() && nest.empty();
    }

    /** @return Map entries used in the current scope. */
    @Override public List<Map.Entry<String, Binding>> entries() {
      return new ArrayList<>(flat.entrySet());
    }

    /** @return The information about the name in current {@link Environment}
     *         . */
    @Override public Binding get(final String identifier){
      final Binding $ = flat.get(identifier);
      return $ != null ? $ : nest.get(identifier);
    }

    /** Check whether the {@link Environment} already has the name. */
    @Override public boolean has(final String identifier) {
      return flat.containsKey(identifier) || nest.has(identifier);
    }

    /** @return names used the {@link Environment} . */
    @Override public LinkedHashSet<String> keys() {
      return new LinkedHashSet<>(flat.keySet());
    }

    /** One step up in the {@link Environment} tree. Funny but it even sounds
     * like next(). */
    @Override public Environment nest() {
      return nest;
    }

    /** Add name to the current scope in the {@link Environment} . */
    @Override public Binding put(final String identifier, final Binding value) {
      flat.put(identifier, value);
      assert !flat.isEmpty();
      return hiding(identifier);
    }

    @Override public String name() {
      return name;
    }

    @Override public int size() {
      return flat.size(); 
    }

    @Override public Nested addChild(Nested child) {
      children.add(child);
      return this;
    }

  }
}