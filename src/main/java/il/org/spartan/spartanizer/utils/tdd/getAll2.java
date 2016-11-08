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
  /** @author Shimon Azulay
   * @author Idan Atias
   * @since 16-11-3 */
  /** Given a TypeDeclaration argument, this function returns a list of it's
   * public fields names.
   * @param d - the TypeDeclaration argument */
  public static List<String> publicFields(TypeDeclaration d) {
    LinkedList<String> $ = new LinkedList<>();
    if (d == null)
      return $;
    for (FieldDeclaration fd : d.getFields())
      for (Object mod : fd.modifiers())
        if ("public".equals((mod + ""))) {
          String[] field_splitted_to_words = (fd + "").trim().split(" ");
          int field_name_idx = field_splitted_to_words.length - 1;
          if (field_name_idx < 0)
            continue;
          $.add(field_splitted_to_words[field_name_idx].replace(";", "").trim());
        }
    return $;
  }
  
  /** @author Sapir Bismot
   * @author Yaniv Levinsky
   * @since 16-11-8 */
  /** Given a MethodDeclaration argument, this function returns a list of it's
   * all String variable declarations.
   * @param d - the MethodDeclaration argument */
  public static List<VariableDeclaration> stringVariables(MethodDeclaration m) {
    return null;
  }
}
