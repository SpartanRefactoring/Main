package il.org.spartan.spartanizer.utils.tdd;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;

import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;

/** @author Ori Marcovitch
 * @author Moshe ELiasof
 * @author Netanel Felcher
 * @author Doron Meshulam
 * @author Tomer Dragucki
 * @author Shimon Azulay
 * @author Idan Atias
 * @since Nov 8, 2016 */
public enum getAll2 {
  ;
  // For you to implement! Let's TDD and get it on!
  /** takes a single parameter, which is a CompilationUnit returns a list of
   * methods in cu.
   * @param u CompilationUnit
   * @author Moshe Eliasof
   * @author Netanel Felcher */
  public static List<MethodDeclaration> methods(CompilationUnit u) {
    if (u == null)
      return null;
    List<MethodDeclaration> $ = new ArrayList<>();
    u.accept(new ASTVisitor() {
      @Override public boolean visit(MethodDeclaration ¢) {
        $.add(¢);
        return super.visit(¢);
      }
    });
    return $;
  }
  /** Takes Block b and returns list of names in it
   * @param b
   * @return List<Name> which is all names in b
   * @author Doron Meshulam
   * @author Tomer Dragucki */
  public static List<Name> names(Block b) {
    if (b == null)
      return null;
    List<Name> $ = new ArrayList<>();
    b.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode an) {
        if (iz.name(an))
          $.add(az.name(an));
      }
    });
    return $;
  }

  /** @author Shimon Azulay & Idan Atias
   * @since 16-11-3 */
  public static List<String> publicFields(TypeDeclaration type) {
    LinkedList<String> list = new LinkedList<>();
    if (type == null)
      return list;
    for (FieldDeclaration fd : type.getFields()) {
      for (Object mod : fd.modifiers()) {
        if (mod.toString().equals("public")) {
          String[] field_splitted_to_words = fd.toString().trim().split(" ");
          int field_name_idx = field_splitted_to_words.length - 1;
          if (field_name_idx < 0)
            continue;
          String field_name = field_splitted_to_words[field_name_idx].replace(";", "").trim();
          list.add(field_name);
        }
      }
    }
    return list;
  }
}
