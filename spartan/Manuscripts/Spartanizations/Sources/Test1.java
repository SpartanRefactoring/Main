import java.util.*;
import org.eclipse.jdt.core.dom.*;

public class Test1 {
  public static void main(final String __[]) {
    final ASTParser p = ASTParser.newParser(AST.JLS3);
    p.setSource((
    	      "public class A {\n" +
    	      " int i = 9;\n"+ //
    	      " int j;\n" + //
    	      " ArrayList<Integer> al = new ArrayList<Integer>();\n" + //
    	      " j=1000;\n" + // sic (syntax error in the original)
    	      "}\n").toCharArray());   
    p.setKind(ASTParser.K_COMPILATION_UNIT);
    final CompilationUnit u = (CompilationUnit) p.createAST(null);
    u.accept(new ASTVisitor() {
      Set names = new HashSet();
      @Override public boolean visit(final SimpleName ¢) {
        if (names.contains(¢.getIdentifier()))
          System.out.println("Usage of '" + ¢ + //
          "' at line " + u.getLineNumber(¢.getStartPosition()));
        return true;
      }
      @Override public boolean visit(final VariableDeclarationFragment f) {
        final SimpleName ¢ = f.getName();
        names.add(¢.getIdentifier());
        System.out.println("Declaration of '" + ¢ + //
        "' at line" + u.getLineNumber(¢.getStartPosition()));
        return false; // do not continue to avoid usage info
      }
    });
  }
}