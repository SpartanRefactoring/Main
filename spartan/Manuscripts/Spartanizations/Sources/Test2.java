import java.util.*;
import org.eclipse.jdt.core.dom.*;

public class Test2 {
  public static void main(final String args[]) { go(); }
  private static void go() {
    final CompilationUnit u = makeCompilationUnit();
    u.accept(new ASTVisitor() {
      final Set<String> $ = new HashSet<>();
      @Override public boolean visit(final SimpleName ¢) {
        note(¢);
        return true;
      }
      @Override public boolean visit(final VariableDeclarationFragment ¢) {
        collect(¢.getName());
        return false; // do not continue to avoid usage info
      }
      void note(final SimpleName ¢) {
        if ($.contains(¢.getIdentifier()))
          print("Usage ", ¢);
      }
      void collect(final SimpleName ¢) {
        $.add(¢.getIdentifier());
        print("Declaration '", ¢);
      }
      void print(String prefix, final SimpleName ¢) {
        System.out.println(prefix + " of '" + ¢ + "' at line " + u.getLineNumber(¢.getStartPosition()));
      }
    });
  }
  private static CompilationUnit makeCompilationUnit() {
    return (CompilationUnit) makeParser().createAST(null);
  }
  private static ASTParser makeParser() {
    final ASTParser $ = ASTParser.newParser(AST.JLS3);
    $.setSource("public class A { int i = 9;  \n int j; \n ArrayList<Integer> al = new ArrayList<Integer>();j=1000; }".toCharArray()); // parser.setSource("/*abc*/".toCharArray());
    $.setKind(ASTParser.K_COMPILATION_UNIT);
    return $;
  }
}