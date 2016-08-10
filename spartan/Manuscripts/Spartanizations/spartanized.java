import java.util.*;
import org.eclipse.jdt.core.dom.*;

public class Test {
  public static void main(String args[]) { go(); }
  private static void go() {
    CompilationUnit u = makeCompilationUnit();
    u.accept(new ASTVisitor() {
      Set $ = new HashSet();
      public boolean visit(SimpleName ¢) {
        note(¢);
        return true;
      }
      public boolean visit(VariableDeclarationFragment ¢) {
        collect(¢.getName());
        return false; 
      }
      void note(SimpleName ¢) {
        if ($.contains(¢.getIdentifier()))
          System.out.println("Usage of '" + ¢ + "' at line " + u.getLineNumber(¢.getStartPosition()));
      }
      void collect(SimpleName ¢) {
        $.add(¢.getIdentifier());
        System.out.println("Declaration of '" + ¢ + "' at line" + u.getLineNumber(¢.getStartPosition()));
      }
    });
  }
  static CompilationUnit makeCompilationUnit() {
    return (CompilationUnit) makeParser().createAST(null);
  }
  static ASTParser makeParser() {
    ASTParser $ = ASTParser.newParser(AST.JLS3);
    $.setSource("public class A { int i = 9;  \n int j; \n ArrayList<Integer> al = new ArrayList<Integer>();j=1000; }".toCharArray()); 
    $.setKind(ASTParser.K_COMPILATION_UNIT);
    return $;
  }
}
