package il.org.spartan.spartanizer.java;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Interface to Environment. Holds all the names defined till current PC. In
 * other words the 'names Environment' at every point of the program flow. */
/* TODO Dan Greenstein: Tippers to improve once Environment is complete:
 * AssignmentToPostfixIncrement (Issue 107). Identifier renaming (Issue 121) */
@SuppressWarnings({ "unused" }) public interface Environment {
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
  final LinkedHashSet<Entry<String, Information>> currentEnvironment = new LinkedHashSet<>();

  static Information createInformation(final VariableDeclarationFragment ¢, final type t) {
    return new Information(¢.getParent(), getHidden(fullName(¢.getName())), ¢, t);
  }

  /** @param ¢ JD
   * @return All declarations in given {@link Statement}, without entering the
   *         contained ({@link Blocl}s. If the {@link Statement} is a
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
    for (final VariableDeclarationFragment ¢ : fragments(s))
      $.add(new MapEntry<>(path + "." + ¢.getName(), createInformation(¢, t)));
    return $;
  }

  /** @return set of entries declared in the node, including all hiding. */
  static LinkedHashSet<Entry<String, Information>> declaresDown(final ASTNode n) {
    // Holds the declarations in the subtree and relevant siblings.
    final LinkedHashSet<Entry<String, Information>> $ = new LinkedHashSet<>();
    n.accept(new ASTVisitor() {
      // Holds the current scope full name (Path).
      String scopePath = "";

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
        for (final VariableDeclarationFragment ¢ : fragments(d))
          $.add(new MapEntry<>(fullName(¢.getName()), createInformation(¢, t)));
        return $;
      }

      Entry<String, Information> convertToEntry(final SingleVariableDeclaration ¢) {
        return new MapEntry<>(fullName(¢.getName()), createInformation(¢));
      }

      @SuppressWarnings("hiding") List<Entry<String, Information>> convertToEntry(final VariableDeclarationExpression x) {
        final List<Entry<String, Information>> $ = new ArrayList<>();
        final type t = type.baptize(wizard.condense(x.getType()));
        for (final VariableDeclarationFragment ¢ : fragments(x))
          $.add(new MapEntry<>(fullName(¢.getName()), createInformation(¢, t)));
        return $;
      }

      @SuppressWarnings("hiding") List<Entry<String, Information>> convertToEntry(final VariableDeclarationStatement s) {
        final List<Entry<String, Information>> $ = new ArrayList<>();
        final type t = type.baptize(wizard.condense(s.getType()));
        for (final VariableDeclarationFragment ¢ : fragments(s))
          $.add(new MapEntry<>(fullName(¢.getName()), createInformation(¢, t)));
        return $;
      }

      Information createInformation(final AnnotationTypeMemberDeclaration ¢) {
        return new Information(¢.getParent(), getHidden(fullName(¢.getName())), ¢, type.baptize(wizard.condense(¢.getType())));
      }

      Information createInformation(final SingleVariableDeclaration ¢) {
        return new Information(¢.getParent(), getHidden(fullName(¢.getName())), ¢, type.baptize(wizard.condense(¢.getType())));
      }

      Information createInformation(final VariableDeclarationFragment ¢, final type t) {
        return new Information(¢.getParent(), getHidden(fullName(¢.getName())), ¢, t);
      }

      @SuppressWarnings("hiding") String fullName(final SimpleName $) {
        return scopePath + "." + $;
      }

      Information get(final LinkedHashSet<Entry<String, Information>> ss, final String s) {
        for (final Entry<String, Information> ¢ : ss)
          if (s.equals(¢.getKey()))
            return ¢.getValue();
        return null;
      }

      /** Returns the {@link Information} of the declaration the current
       * declaration is hiding.
       * @param ¢ the fullName of the declaration.
       * @return The hidden node's Information [[SuppressWarningsSpartan]] */
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
       * (Dan) lean towards searching 'declaresUp'. Current implementation only
       * searches declaresDown.
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
       * @return The nodes index, according to order of appearance, among
       *         nodesof the same type. [[SuppressWarningsSpartan]] */
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
    });
    return $;
  }

  static LinkedHashSet<Entry<String, Information>> declaresUp(final ASTNode n) {
    for (Block PB = getParentBlock(n); PB != null; PB = getParentBlock(PB))
      for (final Statement ¢ : statements(PB))
        currentEnvironment.addAll(declarationsOf(¢));
    return currentEnvironment;
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
    for (final Entry<String, Information> ¢ : ss)
      if (s.equals(¢.getKey()))
        return ¢.getValue();
    return null;
  }

  /** [[SuppressWarningsSpartan]] */
  static Information getHidden(final String ¢) {
    final String shortName = ¢.substring(¢.lastIndexOf(".") + 1);
    for (String s = parentNameScope(¢); !"".equals(s); s = parentNameScope(s)) {
      final Information i = get(currentEnvironment, s + "." + shortName);
      if (i != null)
        return i;
    }
    return null;
  }

  static Block getParentBlock(final ASTNode ¢) {
    return az.block(¢.getParent());
  }

  static String name(final ASTNode ¢) {
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
  static LinkedHashSet<Entry<String, Information>> uses(final ASTNode n) {
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
   *         otherwise ?? TODO: Alex */
  default Information hiding(final String name) {
    return nest().get(name);
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

  /** TODO Dan Greenstein: document Mumbo jumbo of stuff we will do later.
   * Document it, but do not maintain it for now, this class is intentionally
   * package level, and intenationally defined local. For now, clients should
   * not be messing with it */
  static class Information {
    public static boolean eq(final Object o1, final Object o2) {
      return o1 == o2 || o1 == null && o2 == null || o2.equals(o1);
    }

    // TODO Dan Greenstein: implement this.
    static boolean eq(final type t1, final type t2) {
      return true;
      // return t1 == null ? t2 == null : t2 != null && t1 == t2;
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
      // Yossi Gil: we wanted to use the prudentType, so we wrote a comparison
      // function to it.
      // Some one changed it and all our tests fell. When the API to "type" will
      // be ready we will
      // TODO Dan Greenstein: it should be ready now.
      return eq(blockScope, ¢.blockScope) && eq(hiding, ¢.hiding) && eq(prudentType, ¢.prudentType) && eq(self, ¢.self);
    }

    @Override public boolean equals(final Object ¢) {
      return ¢ == this || ¢ != null && getClass() == ¢.getClass() && equals((Information) ¢);
    }

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

    /** @return true iff {@link Environment} is empty. */
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
}