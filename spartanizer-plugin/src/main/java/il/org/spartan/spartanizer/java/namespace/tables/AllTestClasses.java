package il.org.spartan.spartanizer.java.namespace.tables;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.cmdline.CurrentData;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.cmdline.Tapper;
import il.org.spartan.tables.Table;

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
        if (!map.get("TestClassName").isEmpty())
			for (int i = 0; i < map.get("TestClassName").size(); i++) {
				String className = map.get("TestClassName").get(i);
				String classCode = map.get("TestClass").get(i);
				table.col("Project", path).col("TestClassName", className).nl();
				File file = new File(initialPath + path + "/" + className + ".txt");
				file.getParentFile().mkdirs();
				try (FileWriter writer = new FileWriter(file, false);
						PrintWriter output = new PrintWriter(writer)) {
					output.print(classCode);
				} catch (Exception x) {
					x.printStackTrace();
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
