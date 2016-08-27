public class Test0 { // comments and £\kk{import}£ directives omitted for brevity
  public static void main(String args[]) {
    ASTParser parser = ASTParser.newParser(AST.JLS3);
    parser.setSource((
      "public class A {\n" +
      " int i = 9;\n"+ //
      " int j;\n" + //
      " ArrayList<Integer> al = new ArrayList<Integer>();\n" + //
      " j=1000;\n" + // sic! syntax error in the original
      "}\n").toCharArray());
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    CompilationUnit cu = (CompilationUnit) parser.createAST(null);
    cu.accept(new ASTVisitor() {
      Set names = new HashSet();
      public boolean visit(VariableDeclarationFragment node) {
        SimpleName name = node.getName();
        this.names.add(name.getIdentifier());
        System.out.println(
          "Declaration of '" + name + "' at line" + //
          cu.getLineNumber(name.getStartPosition()));
        return false;
      }
      public boolean visit(SimpleName node) {
        if (this.names.contains(node.getIdentifier()))
          System.out.println("Usage of '" + node + "' at line " + cu.getLineNumber(node.getStartPosition()));
        return true;
      }
    });
  }
}
