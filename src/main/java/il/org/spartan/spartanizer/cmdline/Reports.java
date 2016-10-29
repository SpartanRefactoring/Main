package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.tide.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.AbstractCommandLineSpartanizer.*;

public class Reports {
  protected String folder = "/tmp/";
  protected String afterFileName;
  protected String beforeFileName;
  protected String inputPath;
  protected String spectrumFileName;
//  protected <HashMap<Object, List<HashMap<String,Object>>>> metrics = null;
  protected static HashMap<String, CSVStatistics> reports = new HashMap<>();
  protected static HashMap<String, PrintWriter> files = new HashMap<>();
  
  private static class Util {
    
    private static NamedFunction functions[]; 
    
    public static NamedFunction[] functions(final String id){
      return functions = as.array(m("length" + id, (¢) -> (¢ + "").length()), m("essence" + id, (¢) -> Essence.of(¢ + "").length()),
          m("tokens" + id, (¢) -> metrics.tokens(¢ + "")), m("nodes" + id, (¢) -> count.nodes(¢)), m("body" + id, (¢) -> metrics.bodySize(¢)),
          m("methodDeclaration" + id, (¢) -> az.methodDeclaration(¢) == null ? -1
              : extract.statements(az.methodDeclaration(¢).getBody()).size()),
          m("tide" + id, (¢) -> clean(¢ + "").length()));//
//          m("Δ Nodes", (¢1,¢2) -> (count.nodes(¢1)-count.nodes(¢2))),//
//          m("Δ Body", (¢1,¢2) -> (metrics.bodySize(¢1)-metrics.bodySize(¢2))),//
//          m("Δ Tokens", (¢1,¢2) -> (metrics.tokens(¢1 + "")-metrics.tokens(¢2 + ""))),//
//          m("Δ Length", (¢1,¢2) -> ((¢1 + "").length()-(¢2 + "").length())),//
//          m("Δ Tide2", (¢1,¢2) -> (clean(¢1 + "").length()-clean(¢2 + "").length())),//
//          m("Δ Essence", (¢1,¢2) -> (Essence.of(¢1 + "").length()-Essence.of(¢2 + "").length()))
//          );
//          m("Δ Statements", (¢1,¢2) -> (az.methodDeclaration(¢) == null ? -1
//              : extract.statements(az.methodDeclaration(¢).getBody()).size()))
    }

    static NamedFunction m(final String name, final ToInt<ASTNode> f) {
      return new NamedFunction(name,f);
    }
    
//    static NamedFunction m(final String name, final BiToInt<ASTNode,ASTNode> biToI) {
//      return new NamedBiFunctionInt(name,biToI);
//    }
    
//    static NamedFunction m(final String name, final BiToDouble<ASTNode,ASTNode> biToD) {
//      return new NamedBiFunctionDouble(name,biToD);
//    }
   
  }
  
//.put("Δ Nodes", nm1.nodes() - nm2.nodes())//
//.put("δ Nodes", system.d(nm1.nodes(), nm2.nodes()))//
//.put("δ Nodes %", system.p(nm1.nodes(), nm2.nodes()))//
//.put("Δ Body", nm1.body() - nm2.body())//
//.put("δ Body", system.d(nm1.body(), nm2.body()))//
//.put("% Body", system.p(nm1.body(), nm2.body()))//
//.put("Δ Tokens", nm1.tokens() - nm2.tokens())//
//.put("δ Tokens", system.d(nm1.tokens(), nm2.tokens()))//
//.put("% Tokens", system.p(nm1.tokens(), nm2.tokens()))//
//.put("Δ Length", nm1.length() - nm2.length())//
//.put("δ Length", system.d(nm1.length(), nm2.length()))//
//.put("% Length", system.p(nm1.length(), nm2.length()))//
//.put("Δ Tide2", nm1.tide() - nm2.tide())//
//.put("δ Tide2", system.d(nm1.tide(), nm2.tide()))//
//.put("δ Tide2", system.p(nm1.tide(), nm2.tide()))//
//.put("Δ Essence", nm1.essence() - nm2.essence())//
//.put("δ Essence", system.d(nm1.essence(), nm2.essence()))//
//.put("% Essence", system.p(nm1.essence(), nm2.essence()))//
//.put("Δ Statement", nm1.statements() - nm2.statements())//
//.put("δ Statement", system.d(nm1.statements(), nm2.statements()))//
//.put("% Statement", system.p(nm1.statements(), nm2.statements()));//
  
//  private static final NamedFunction functions[] = as.array(m("length", (¢) -> (¢ + "").length()), m("essence", (¢) -> Essence.of(¢ + "").length()),
//      m("tokens", (¢) -> metrics.tokens(¢ + "")), m("nodes", (¢) -> count.nodes(¢)), m("body", (¢) -> metrics.bodySize(¢)),
//      m("methodDeclaration", (¢) -> az.methodDeclaration(¢) == null ? -1 : extract.statements(az.methodDeclaration(¢).getBody()).size()),
//      m("tide", (¢) -> clean(¢ + "").length()));
  
  // running report
  public static void writeMetrics(final CSVStatistics report, final ASTNode n, final String id) {
    for (NamedFunction ¢ : Reports.Util.functions(id))
      report.put(¢.name(), ¢.function().run(n));
  }
  
  public static void writeDiff(final CSVStatistics report, final ASTNode n1, final ASTNode n2, final String id) {
    int a;
    for (NamedFunction ¢ : Reports.Util.functions(id)){
      a = ¢.function().run(n1) - ¢.function().run(n2);
      report.put(¢.name(), a);
    }
   }
  
  public static void writePerc(final CSVStatistics report, final ASTNode n1, final ASTNode n2, final String id) {
    double a;
    for (NamedFunction ¢ : Reports.Util.functions(id)){
      a = system.d(¢.function().run(n1), ¢.function().run(n2));
      report.put(¢.name() + " %", a);
    }
   }
  
  @FunctionalInterface public interface ToInt<R> {
    int run(R r);
  }
  
//  @FunctionalInterface public interface BiToInt<R,S> {
//    int run(R r, S s);
//  }
//  
//  @FunctionalInterface public interface BiToDouble<R,S> {
//    double run(R r, S s);
//  }
  
//  static class NamedFunctionInt extends NamedFunction{
//    
//    NamedFunctionInt(final String name, final ToInt<ASTNode> f) {
//      super(name);
//      this.f = f;
//    }
//    
//    ToInt<ASTNode> f;
//    
//    public ToInt<ASTNode> function(){
//      return this.f;
//    }
//    
////    static NamedFunction m(final String name, final ToInt<ASTNode> f) {
////      return new NamedFunctionInt(name,f);
////    }
//  }
  
//  static class NamedBiFunctionInt extends NamedFunction{
//    NamedBiFunctionInt(final String name, final BiToInt<ASTNode,ASTNode> biToI) {
//      super(name);
//      this.biToI = biToI;
//    }
//    
//    BiToInt<ASTNode, ASTNode> biToI;
//    
//    public BiToInt<ASTNode,ASTNode> function(){
//      return this.biToI;
//    }
    
//    static NamedFunction m(final String name, final BiToInt<ASTNode,ASTNode> biToI) {
//      return new NamedBiFunctionInt(name,biToI);
//    }
//  }
  
//  static class NamedBiFunctionDouble extends NamedFunction{
//    NamedBiFunctionDouble(String name, BiToDouble<ASTNode, ASTNode> biToD) {
//      super(name);
//      this.biToD = biToD;
//    }
//    
//    BiToDouble<ASTNode, ASTNode> biToD;
//    
//    public BiToDouble<ASTNode,ASTNode> function(){
//      return this.biToD;
//    }
//    
////    static NamedFunction m(final String name, final BiToDouble<ASTNode,ASTNode> biToD) {
////      return new NamedBiFunctionDouble(name,biToD);
////    }
//  }


//  static NamedFunction m(final String name, final ToInt<ASTNode> f) {
//    return new NamedFunction(name);
//  }

  static class NamedFunction {
    final String name;
    final ToInt f;
    
    NamedFunction(final String name, ToInt f) {
      this.name = name;
      this.f = f;
    }
   
    public String name(){
      return this.name;
    }
    
    public ToInt<ASTNode> function(){
      return this.f;
    }
  }

  @SuppressWarnings("resource") public static void initializeFile(final String fileName, final String id) throws IOException {
    files.put(id, new PrintWriter(new FileWriter(fileName)));
  }

  public static void intializeReport(final String reportFileName, final String id) {
    // reportFileName = "/tmp/report.CSV";
    try {
      reports.put(id, new CSVStatistics(reportFileName, id));
    } catch (final IOException x) {
      x.printStackTrace();
    }
  }

  private static CSVStatistics report(final String key) {
    return reports.get(key);
  }

  private static PrintWriter files(final String key) {
    return files.get(key);
  }

  public static void reportMetrics(final ASTNodeMetrics nm, final String id, final String key) {
    report(key)//
        .put("Nodes" + id, nm.nodes())//
        .put("Body" + id, nm.body())//
        .put("Length" + id, nm.length())//
        .put("Tokens" + id, nm.tokens())//
        .put("Tide" + id, nm.tide())//
        .put("Essence" + id, nm.essence())//
        .put("Statements" + id, nm.statements());//
  }

  /** @param nm1
   * @param nm2 */
  public static void reportDifferences(final ASTNodeMetrics nm1, final ASTNodeMetrics nm2, final String key) {
    report(key) //
        .put("Δ Nodes", nm1.nodes() - nm2.nodes())//
        .put("δ Nodes", system.d(nm1.nodes(), nm2.nodes()))//
        .put("δ Nodes %", system.p(nm1.nodes(), nm2.nodes()))//
        .put("Δ Body", nm1.body() - nm2.body())//
        .put("δ Body", system.d(nm1.body(), nm2.body()))//
        .put("% Body", system.p(nm1.body(), nm2.body()))//
        .put("Δ Tokens", nm1.tokens() - nm2.tokens())//
        .put("δ Tokens", system.d(nm1.tokens(), nm2.tokens()))//
        .put("% Tokens", system.p(nm1.tokens(), nm2.tokens()))//
        .put("Δ Length", nm1.length() - nm2.length())//
        .put("δ Length", system.d(nm1.length(), nm2.length()))//
        .put("% Length", system.p(nm1.length(), nm2.length()))//
        .put("Δ Tide2", nm1.tide() - nm2.tide())//
        .put("δ Tide2", system.d(nm1.tide(), nm2.tide()))//
        .put("δ Tide2", system.p(nm1.tide(), nm2.tide()))//
        .put("Δ Essence", nm1.essence() - nm2.essence())//
        .put("δ Essence", system.d(nm1.essence(), nm2.essence()))//
        .put("% Essence", system.p(nm1.essence(), nm2.essence()))//
        .put("Δ Statement", nm1.statements() - nm2.statements())//
        .put("δ Statement", system.d(nm1.statements(), nm2.statements()))//
        .put("% Statement", system.p(nm1.statements(), nm2.statements()));//
  }

  /** @param nm */
  public static void reportRatio(final ASTNodeMetrics nm, final String id, final String key) {
    report(key) //
        // .put("Words)", wordCount).put("R(T/L)", system.ratio(length, tide))
        // //
        .put("R(E/L)" + id, system.ratio(nm.length(), nm.essence())) //
        .put("R(E/T)" + id, system.ratio(nm.tide(), nm.essence())) //
        .put("R(B/S)" + id, system.ratio(nm.nodes(), nm.body())); //
  }

  public static void close(final String key) {
    report(key).close();
  }

  public static void summaryFileName(final String key) {
    report(key).summaryFileName();
  }

  public static void nl(final String key) {
    report(key).nl();
  }

  public static void printFile(final String input, final String key) {
    assert input != null;
    files(key).print(input);
  }

  public static void closeFile(final String key) {
    files(key).flush();
    files(key).close();
  }

  public static HashMap<String, CSVStatistics> reports() {
    return reports;
  }
}
