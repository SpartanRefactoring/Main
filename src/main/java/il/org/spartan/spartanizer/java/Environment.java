package il.org.spartan.spartanizer.java;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;
import java.util.Map.*;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Interface to Environment. Holds all the names defined till current PC. In
 * other words the 'names Environment' at every point of the program flow. */
@SuppressWarnings({ "unused" })
public interface Environment {
  /** Information about a variable in the Environment - its {@link ASTNode}, his
   * parent's, its {@link type}, and which other variables does it hide. this
   * class is intentionally package level, and intentionally defined local. For
   * now, clients should not be messing with it
   * @since 2016 */
  static class Information {
    public static boolean eq(final Object o1, final Object o2) {
      return o1 == o2 || o1 == null && o2 == null || o2.equals(o1);
    }

    /** For Information purposes, {@link type}s are equal if their key is
     * equal. */
    static boolean eq(final type t1, final type t2) {
      return t1 == null ? t2 == null : t2 != null && t1.key().equals(t2.key());
    }

    /** The containing block, whose death marks the death of this entry; not
     * sure, but I think this entry can be shared by many nodes at the same
     * level */
    public final ASTNode blockScope;
    /** What do we know about an entry hidden by this one */
    public final Information hiding;
    /** The node at which this entry was created */
    public final ASTNode self;
    /** What do we know about the type of this definition */
    public final type prudentType;

    // For now, nothing is known, we only maintain lists
    public Information() {
      blockScope = self = null;
      prudentType = null;
      hiding = null;
    }

    public Information(final ASTNode blockScope, final Information hiding, final ASTNode self, final type prudentType) {
      this.blockScope = blockScope;
      this.hiding = hiding;
      this.self = self;
      this.prudentType = prudentType;
    }

    public Information(final type t) {
      blockScope = self = null;
      prudentType = t;
      hiding = null;
    }

    public boolean equals(final Information ¢) {
      return eq(blockScope, ¢.blockScope) && eq(hiding, ¢.hiding) && eq(prudentType, ¢.prudentType) && eq(self, ¢.self);
    }

    /** @param ¢
     * @return <code><b>true</b></code> <em>iff</em> the ASTNode (self) and its
     *         parent (blockScope) are the same ones, the type's key() is the
     *         same, and if the Information nodes hidden are equal. */
    // Required for MapEntry equality, which is, in turn, required for Set
    // containment check, which is required for testing.
    @Override public boolean equals(final Object ¢) {
      return ¢ == this || ¢ != null && getClass() == ¢.getClass() && equals((Information) ¢);
    }

    // Required for MapEntry equality, which is, in turn, required for Set
    // containment check, which is required for testing.
    @Override public int hashCode() {
      return (self == null ? 0 : self.hashCode())
          + 31 * ((hiding == null ? 0 : hiding.hashCode()) + 31 * ((blockScope == null ? 0 : blockScope.hashCode()) + 31));
    }
  }

  /** Dictionary with a parent. Insertions go the current node, searches start
   * at the current note and Delegate to the parent unless it is null. */
  final class Nested implements Environment {
    public final Map<String, Information> flat = new LinkedHashMap<>();
    public final Environment nest;

    Nested(final Environment parent) {
      nest = parent;
    }

    /** @return <code><b>true</b></code> <em>iff</em> {@link Environment} is
     *         empty. */
    @Override public boolean empty() {
      return flat.isEmpty() && nest.empty();
    }

    /** @return Map entries used in the current scope. */
    @Override public LinkedHashSet<Map.Entry<String, Information>> entries() {
      return new LinkedHashSet<>(flat.entrySet());
    }

    /** @return The information about the name in current {@link Environment}
     *         . */
    @Override public Information get(final String name) {
      final Information $ = flat.get(name);
      return $ != null ? $ : nest.get(name);
    }

    /** Check whether the {@link Environment} already has the name. */
    @Override public boolean has(final String name) {
      return flat.containsKey(name) || nest.has(name);
    }

    /** @return Names used the {@link Environment} . */
    @Override public LinkedHashSet<String> names() {
      return new LinkedHashSet<>(flat.keySet());
    }

    /** One step up in the {@link Environment} tree. Funny but it even sounds
     * like next(). */
    @Override public Environment nest() {
      return nest;
    }

    /** Add name to the current scope in the {@link Environment} . */
    @Override public Information put(final String name, final Information value) {
      flat.put(name, value);
      assert !flat.isEmpty();
      return hiding(name);
    }
  }

  /** The Environment structure is in some like a Linked list, where EMPTY is
   * like the NULL at the end. */
  final Environment EMPTY = new Environment() {
    // This class is intentionally empty
  };
  /** Initializer for EMPTY */
  final LinkedHashSet<Entry<String, Information>> emptyEntries = new LinkedHashSet<>();
  /** Initializer for EMPTY */
  final LinkedHashSet<String> emptySet = new LinkedHashSet<>();
  // Holds the declarations in the subtree and relevant siblings.
  final LinkedHashSet<Entry<String, Information>> upEnv = new LinkedHashSet<>();

  static Information createInformation(final VariableDeclarationFragment ¢, final type t) {
    return new Information(¢.getParent(), getHidden(fullName(¢.getName())), ¢, t);
  }

  /** @param ¢ JD
   * @return All declarations in given {@link Statement}, without entering the
   *         contained ({@link Block}s. If the {@link Statement} is a
   *         {@link Block}, (also IfStatement, ForStatement and so on...) return
   *         empty Collection. */
  static List<Entry<String, Information>> declarationsOf(final Statement ¢) {
    final List<Entry<String, Information>> $ = new ArrayList<>();
    switch (¢.getNodeType()) {
      case VARIABLE_DECLARATION_STATEMENT:
        $.addAll(declarationsOf(az.variableDeclrationStatement(¢)));
        break;
      default:
        return $;
    }
    return $;
  }

  static List<Entry<String, Information>> declarationsOf(final VariableDeclarationStatement s) {
    final List<Entry<String, Information>> $ = new ArrayList<>();
    final type t = type.baptize(wizard.condense(s.getType()));
    final String path = fullName(s);
    $.addAll(fragments(s).stream().map(¢ -> new MapEntry<>(path + "." + ¢.getName(), createInformation(¢, t))).collect(Collectors.toList()));
    return $;
  }

  /** @return set of entries declared in the node, including all hiding. */
  static LinkedHashSet<Entry<String, Information>> declaresDown(final ASTNode n) {
    // Holds the declarations in the subtree and relevant siblings.
    final LinkedHashSet<Entry<String, Information>> $ = new LinkedHashSet<>();
    n.accept(new ASTVisitor() {
      /* Three groups of visitors here: 1. Non-declarations with a name. 2.
       * Non-declarations without a name. 3. Actual Declarations.
       *
       * First two groups are those in which variable declarations can be made.
       * Since we want to be able to distinguish variables of different scopes,
       * but with, perhaps, equal names, need to keep the scope. The full scope
       * might contain things that do not have a name, hence the need keep to
       * visit ASTNodes without a name such as {@link Block}s, {@link
       * ForStatement}s, etc.
       *
       * Since there can be more than one such node in a parent, they are
       * distinguished by their order of appearance.
       *
       * The third group is the one in which actual addition to the Environment
       * is made. */
      // Holds the current scope full name (Path).
      String scopePath = "";

      String anonymousClassDeclarationParentName(final AnonymousClassDeclaration d) {
        // As of JSL3, AnonymousClassDeclaration's parent can be either
        // ClassInstanceCreation or EnumConstantDeclaration
        @SuppressWarnings("hiding") final ASTNode n = d.getParent();
        if (n instanceof ClassInstanceCreation)
          return az.classInstanceCreation(n).getType() + "";
        assert n instanceof EnumConstantDeclaration;
        return az.enumConstantDeclaration(n).getName() + "";
      }

      Entry<String, Information> convertToEntry(final AnnotationTypeMemberDeclaration ¢) {
        return new MapEntry<>(fullName(¢.getName()), createInformation(¢));
      }

      @SuppressWarnings("hiding") List<Entry<String, Information>> convertToEntry(final FieldDeclaration d) {
        final List<Entry<String, Information>> $ = new ArrayList<>();
        final type t = type.baptize(wizard.condense(d.getType()));
        $.addAll(fragments(d).stream().map(¢ -> new MapEntry<>(fullName(¢.getName()), createInformation(¢, t))).collect(Collectors.toList()));
        return $;
      }

      Entry<String, Information> convertToEntry(final SingleVariableDeclaration ¢) {
        return new MapEntry<>(fullName(¢.getName()), createInformation(¢));
      }

      @SuppressWarnings("hiding") List<Entry<String, Information>> convertToEntry(final VariableDeclarationExpression x) {
        final List<Entry<String, Information>> $ = new ArrayList<>();
        final type t = type.baptize(wizard.condense(x.getType()));
        $.addAll(fragments(x).stream().map(¢ -> new MapEntry<>(fullName(¢.getName()), createInformation(¢, t))).collect(Collectors.toList()));
        return $;
      }

      @SuppressWarnings("hiding") List<Entry<String, Information>> convertToEntry(final VariableDeclarationStatement s) {
        final List<Entry<String, Information>> $ = new ArrayList<>();
        final type t = type.baptize(wizard.condense(s.getType()));
        $.addAll(fragments(s).stream().map(¢ -> new MapEntry<>(fullName(¢.getName()), createInformation(¢, t))).collect(Collectors.toList()));
        return $;
      }

      Information createInformation(final AnnotationTypeMemberDeclaration ¢) {
        return new Information(¢.getParent(), getHidden(fullName(¢.getName())), ¢, type.baptize(wizard.condense(¢.getType())));
      }

      Information createInformation(final SingleVariableDeclaration ¢) {
        return new Information(¢.getParent(), getHidden(fullName(¢.getName())), ¢, type.baptize(wizard.condense(¢.getType())));
      }

      Information createInformation(final VariableDeclarationFragment ¢, final type t) {
        // VariableDeclarationFragment, that comes from either FieldDeclaration,
        // VariableDeclarationStatement or VariableDeclarationExpression,
        // does not contain its type. Hence, the type is sent from the parent in
        // the convertToEntry calls.
        return new Information(¢.getParent(), getHidden(fullName(¢.getName())), ¢, t);
      }

      // Everything besides the actual variable declaration was visited for
      // nameScope reasons. Once their visit is over, the nameScope needs to be
      // restored.
      @Override public void endVisit(final AnnotationTypeDeclaration __) {
        restoreScopeName();
      }

      @Override public void endVisit(final AnonymousClassDeclaration __) {
        restoreScopeName();
      }

      @Override public void endVisit(final Block __) {
        restoreScopeName();
      }

      @Override public void endVisit(final CatchClause __) {
        restoreScopeName();
      }

      @Override public void endVisit(final DoStatement __) {
        restoreScopeName();
      }

      @Override public void endVisit(final EnhancedForStatement __) {
        restoreScopeName();
      }

      @Override public void endVisit(final EnumConstantDeclaration __) {
        restoreScopeName();
      }

      @Override public void endVisit(final EnumDeclaration __) {
        restoreScopeName();
      }

      @Override public void endVisit(final ForStatement __) {
        restoreScopeName();
      }

      @Override public void endVisit(final IfStatement __) {
        restoreScopeName();
      }

      @Override public void endVisit(final MethodDeclaration __) {
        restoreScopeName();
      }

      @Override public void endVisit(final SwitchStatement __) {
        restoreScopeName();
      }

      @Override public void endVisit(final TryStatement __) {
        restoreScopeName();
      }

      @Override public void endVisit(final TypeDeclaration __) {
        restoreScopeName();
      }

      @Override public void endVisit(final WhileStatement __) {
        restoreScopeName();
      }

      @SuppressWarnings("hiding") String fullName(final SimpleName $) {
        return scopePath + "." + $;
      }

      Information get(final LinkedHashSet<Entry<String, Information>> ss, final String s) {
        System.out.println($);
        for (final Entry<String, Information> ¢ : ss)
          if (s.equals(¢.getKey()))
            return ¢.getValue();
        return null;
      }

      /** Returns the {@link Information} of the declaration the current
       * declaration is hiding.
       * @param ¢ the fullName of the declaration.
       * @return The hidden node's Information */
      /* Implementation notes: Should go over result set, and search for
       * declaration which shares the same variable name in the parents. Should
       * return the closest match: for example, if we search for a match to
       * .A.B.C.x, and result set contains .A.B.x and .A.x, we should return
       * .A.B.x.
       *
       * If a result is found in the result set, return said result.
       *
       * To consider: what if said hidden declaration will not appear in
       * 'declaresDown', but will appear in 'declaresUp'? Should we search for
       * it in 'declaresUp' result set? Should we leave the result as it is? I
       * (Dan Greenstein) lean towards searching 'declaresUp'. Current
       * implementation only searches declaresDown.
       *
       * If no match is found, return null. */
      Information getHidden(final String ¢) {
        final String shortName = ¢.substring(¢.lastIndexOf(".") + 1);
        for (String s = parentNameScope(¢); !"".equals(s); s = parentNameScope(s)) {
          final Information i = get($, s + "." + shortName);
          if (i != null)
            return i;
        }
        return null;
      }

      /** Similar to statementOrderAmongTypeInParent, {@link CatchClause}s
       * only */
      int orderOfCatchInTryParent(final CatchClause c) {
        assert c.getParent() instanceof TryStatement;
        @SuppressWarnings("hiding") int $ = 0;
        for (final CatchClause ¢ : catchClauses((TryStatement) c.getParent())) {
          if (¢ == c)
            break;
          ++$;
        }
        return $;
      }

      String parentNameScope(final String ¢) {
        assert "".equals(¢) || ¢.lastIndexOf(".") != -1 : "nameScope malfunction!";
        return "".equals(¢) ? "" : ¢.substring(0, ¢.lastIndexOf("."));
      }

      void restoreScopeName() {
        scopePath = parentNameScope(scopePath);
      }

      /** Order of the searched {@link Statement} in its parent {@link ASTNode},
       * among nodes of the same kind. Zero based.
       * @param s
       * @return The nodes index, according to order of appearance, amongnodesof
       *         the same type. [[SuppressWarningsSpartan]] */
      int statementOrderAmongTypeInParent(final Statement s) {
        // extract.statements wouldn't work here - we need a shallow extract,
        // not a deep one.
        @SuppressWarnings("hiding") final ASTNode n = s.getParent();
        if (n == null || !(n instanceof Block) && !(n instanceof SwitchStatement))
          return 0;
        @SuppressWarnings("hiding") int $ = 0;
        for (final Statement ¢ : n instanceof Block ? statements((Block) n) : statements((SwitchStatement) n)) {
          // This is intentionally '==' and not equals, meaning the exact same
          // Statement,
          // not just equivalence.
          if (¢ == s)
            break;
          if (¢.getNodeType() == s.getNodeType())
            ++$;
        }
        return $;
      }

      @Override public boolean visit(final AnnotationTypeDeclaration ¢) {
        scopePath += "." + ¢.getName();
        return true;
      }

      @Override public boolean visit(final AnnotationTypeMemberDeclaration ¢) {
        $.add(convertToEntry(¢));
        return true;
      }

      @Override public boolean visit(final AnonymousClassDeclaration ¢) {
        scopePath += "." + "#anon_extends_" + anonymousClassDeclarationParentName(¢);
        return true;
      }

      @Override public boolean visit(final Block ¢) {
        scopePath += "." + "#block" + statementOrderAmongTypeInParent(¢);
        return true;
      }

      @Override public boolean visit(final CatchClause ¢) {
        scopePath += "." + "#catch" + orderOfCatchInTryParent(¢);
        return true;
      }

      @Override public boolean visit(final DoStatement ¢) {
        scopePath += "." + "#do" + statementOrderAmongTypeInParent(¢);
        return true;
      }

      @Override public boolean visit(final EnhancedForStatement ¢) {
        scopePath += "." + "#enhancedFor" + statementOrderAmongTypeInParent(¢);
        return true;
      }

      @Override public boolean visit(final EnumConstantDeclaration ¢) {
        scopePath += "." + ¢.getName();
        return true;
      }

      @Override public boolean visit(final EnumDeclaration ¢) {
        scopePath += "." + ¢.getName();
        return true;
      }

      @Override public boolean visit(final FieldDeclaration ¢) {
        $.addAll(convertToEntry(¢));
        return true;
      }

      @Override public boolean visit(final ForStatement ¢) {
        scopePath += "." + "#for" + statementOrderAmongTypeInParent(¢);
        return true;
      }

      @Override public boolean visit(final IfStatement ¢) {
        scopePath += "." + "#if" + statementOrderAmongTypeInParent(¢);
        return true;
      }

      @Override public boolean visit(final MethodDeclaration ¢) {
        scopePath += "." + ¢.getName();
        return true;
      }

      @Override public boolean visit(final SingleVariableDeclaration ¢) {
        $.add(convertToEntry(¢));
        return true;
      }

      @Override public boolean visit(final SwitchStatement ¢) {
        scopePath += "." + "#switch" + statementOrderAmongTypeInParent(¢);
        return true;
      }

      @Override public boolean visit(final TryStatement ¢) {
        scopePath += "." + "#try" + statementOrderAmongTypeInParent(¢);
        return true;
      }

      @Override public boolean visit(final TypeDeclaration ¢) {
        scopePath += "." + ¢.getName();
        return true;
      }

      @Override public boolean visit(final VariableDeclarationExpression ¢) {
        $.addAll(convertToEntry(¢));
        return true;
      }

      @Override public boolean visit(final VariableDeclarationStatement ¢) {
        $.addAll(convertToEntry(¢));
        return true;
      }

      @Override public boolean visit(final WhileStatement ¢) {
        scopePath += "." + "#while" + statementOrderAmongTypeInParent(¢);
        return true;
      }
    });
    return $;
  }

  /** Gets declarations made in ASTNode's Ancestors */
  static LinkedHashSet<Entry<String, Information>> declaresUp(final ASTNode n) {
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

  static Information get(final LinkedHashSet<Entry<String, Information>> ss, final String s) {
    for (final Entry<String, Information> $ : ss)
      if (s.equals($.getKey()))
        return $.getValue();
    return null;
  }

  static Information getHidden(final String s) {
    final String shortName = s.substring(s.lastIndexOf(".") + 1);
    for (String ¢ = parentNameScope(s); !"".equals(¢); ¢ = parentNameScope(¢)) {
      final Information $ = get(upEnv, ¢ + "." + shortName);
      if ($ != null)
        return $;
    }
    return null;
  }

  static Block getParentBlock(final ASTNode ¢) {
    return az.block(¢.getParent());
  }

  static String name(final ASTNode __) {
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
  static LinkedHashSet<Entry<String, Information>> uses(final ASTNode __) {
    return new LinkedHashSet<>();
  }

  /** Return true iff {@link Environment} doesn'tipper have an entry with a
   * given name. */
  default boolean doesntHave(final String name) {
    return !has(name);
  }

  /** Return true iff {@link Environment} is empty. */
  default boolean empty() {
    return true;
  }

  default LinkedHashSet<Entry<String, Information>> entries() {
    return emptyEntries;
  }

  default LinkedHashSet<Entry<String, Information>> fullEntries() {
    final LinkedHashSet<Entry<String, Information>> $ = new LinkedHashSet<>(entries());
    if (nest() != null)
      $.addAll(nest().fullEntries());
    return $;
  }

  /** Get full path of the current {@link Environment} (all scope hierarchy).
   * Used for full names of the variables. */
  default String fullName() {
    final String $ = nest() == null || nest() == EMPTY ? null : nest().fullName();
    return ($ == null ? "" : $ + ".") + name();
  }

  /** @return all the full names of the {@link Environment}. */
  default LinkedHashSet<String> fullNames() {
    final LinkedHashSet<String> $ = new LinkedHashSet<>(names());
    if (nest() != null)
      $.addAll(nest().fullNames());
    return $;
  }

  default int fullSize() {
    return size() + (nest() == null ? 0 : nest().fullSize());
  }

  /** @return null iff the name is not in use in the {@link Environment} */
  default Information get(final String name) {
    return null;
  }

  /** Answer the question whether the name is in use in the current
   * {@link Environment} */
  default boolean has(final String name) {
    return false;
  }

  /** @return null iff the name is not hiding anything from outer scopes,
   *         otherwise Information about hided instance (with same name) */
  default Information hiding(final String name) {
    return nest() == null ? null : nest().get(name);
  }

  default String name() {
    return "";
  }

  /** @return The names used in the current scope. */
  default Set<String> names() {
    return emptySet;
  }

  /** @return null at the most outer block. This method is similar to the
   *         'next()' method in a linked list. */
  default Environment nest() {
    return null;
  }

  /** Should return the hidden entry, or null if no entry hidden by this one.
   * Note: you will have to assume multiple definitions in the same block, this
   * is a compilation error, but nevertheless, let a later entry with of a
   * certain name to "hide" a former entry with the same name. */
  default Information put(final String name, final Information i) {
    throw new IllegalArgumentException(name + "/" + i);
  }

  default int size() {
    return 0;
  }

  /** Used when new block (scope) is opened. */
  default Environment spawn() {
    return new Nested(this);
  }
}