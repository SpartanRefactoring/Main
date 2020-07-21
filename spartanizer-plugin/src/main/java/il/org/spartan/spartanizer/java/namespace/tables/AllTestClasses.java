package il.org.spartan.spartanizer.java.namespace.tables;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** Filter projects which does not contain any Junit test (Dor Msc.)
 * @author Dor Ma'ayan
 * @since 2019-01-10 */
public class AllTestClasses extends TestTables {
  @SuppressWarnings({ "boxing", "unused" }) public static void main(final String[] args) throws Exception, UnsupportedEncodingException {
    final HashMap<String, List<String>> map = new HashMap<>();
    map.put("TestClassName", new ArrayList<>());
    map.put("TestClass", new ArrayList<>());
    final String initialPath = "/Users/Dor/Desktop/TestingTables/TestClassesDataBase/";
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            done(CurrentData.location);
          }
        });
      }

      void reset() {
        map.put("TestClassName", new ArrayList<>());
        map.put("TestClass", new ArrayList<>());
      }
      protected void done(final String path) {
        summarize(path);
        reset();
      }
      public void summarize(final String path) {
        initializeWriter();
        if (!map.get("TestClassName").isEmpty()) {
          for (int i = 0; i < map.get("TestClassName").size(); i++) {
            String className = map.get("TestClassName").get(i);
            String classCode = map.get("TestClass").get(i);
            table.col("Project", path).col("TestClassName", className) //
                // .col("TestClass", map.get("TestClass").get(i)) //
                .nl();
            try {
              File file = new File(initialPath + path + "/" + className + ".txt");
              file.getParentFile().mkdirs();
              FileWriter writer = new FileWriter(file, false);
              PrintWriter output = new PrintWriter(writer);
              output.print(classCode);
              output.close();
            } catch (Exception x) {
              // TODO Auto-generated catch block
              x.printStackTrace();
            }
          }
        }
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        System.out.println(¢.getTypeRoot().toString());
        ¢.accept(new ASTVisitor() {
          @Override public boolean visit(final TypeDeclaration x) {
            if (isJunit4(¢) || isJunit5(¢)) {
              List<String> a = map.get("TestClassName");
              if (¢.getJavaElement() != null && ¢.getJavaElement().getPath() != null)
                System.out.println("Hi");
              // System.out.println(¢.getJavaElement().getPath().toString());
              a.add(extract.name(x));
              map.put("TestClassName", a);
              List<String> b = map.get("TestClass");
              b.add(x.toString());
              map.put("TestClass", b);
            }
            return true;
          }
        });
        return super.visit(¢);
      }
    });
    table.close();
    System.err.println(table.description());
  }
}
