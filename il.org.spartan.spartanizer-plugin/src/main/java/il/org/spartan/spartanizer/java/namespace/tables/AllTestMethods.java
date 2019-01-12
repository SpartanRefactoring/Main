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
public class AllTestMethods extends TestTables {
  @SuppressWarnings({ "boxing", "unused" }) public static void main(final String[] args) throws Exception, UnsupportedEncodingException {
    final HashMap<String, List<String>> map = new HashMap<>();
    map.put("TestClassName", new ArrayList<>());
    map.put("TestClass", new ArrayList<>());
    final HashMap<String, List<String>> mapMethods = new HashMap<>();
    final HashMap<String, List<String>> mapMethodsCode = new HashMap<>();
    final String initialPath = "/Users/Dor/Desktop/TestingTables/MethodsDataBase/";
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
        mapMethods.clear();
        mapMethodsCode.clear();
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
            if (mapMethods.containsKey(className) && !mapMethods.get(className).isEmpty()) {
              for (int j = 0; j < mapMethods.get(className).size(); j++) {
                String methodName = mapMethods.get(className).get(j);
                String methodCode = mapMethodsCode.get(className).get(j);
                table.col("Project", path).col("TestClassName", className) //
                    // .col("MethodName", methodName).col("MethodCode", methodCode)//
                    .nl();
                try {
                  File file = new File(initialPath + path + "/" + className + "/" + methodName + ".txt");
                  file.getParentFile().mkdirs();
                  FileWriter writer = new FileWriter(file, false);
                  PrintWriter output = new PrintWriter(writer);
                  output.print(methodCode);
                  output.close();
                } catch (Exception x) {
                  // TODO Auto-generated catch block
                  x.printStackTrace();
                }
              }
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
        ¢.accept(new ASTVisitor() {
          @Override public boolean visit(final TypeDeclaration x) {
            if (isJunit4(¢) || isJunit5(¢)) {
              List<String> a = map.get("TestClassName");
              a.add(extract.name(x));
              map.put("TestClassName", a);
              List<String> b = map.get("TestClass");
              b.add(x.toString());
              map.put("TestClass", b);
            }
            x.accept(new ASTVisitor(true) {
              @Override public boolean visit(final MethodDeclaration m) {
                List<String> annotations = extract.annotations(m).stream().map(a -> a.getTypeName().getFullyQualifiedName())
                    .collect(Collectors.toList());
                if (annotations.contains("Test")
                    || (iz.typeDeclaration(m.getParent()) && az.typeDeclaration(m.getParent()).getSuperclassType() != null
                        && az.typeDeclaration(m.getParent()).getSuperclassType().toString().equals("TestCase"))) {
                  if (!mapMethods.containsKey(extract.name(x))) {
                    mapMethods.put(extract.name(x), new ArrayList<>());
                    mapMethodsCode.put(extract.name(x), new ArrayList<>());
                  }
                  mapMethods.get(extract.name(x)).add(extract.name(m));
                  mapMethodsCode.get(extract.name(x)).add(m.toString());
                }
                return true;
              }
            });
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
