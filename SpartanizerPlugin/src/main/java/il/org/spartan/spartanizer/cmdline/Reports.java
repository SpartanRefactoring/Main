package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.*;
import java.util.function.*;

import il.org.spartan.*;

public class Reports {
  protected String folder = "/tmp/";
  protected String afterFileName;
  protected String beforeFileName;
  protected String inputPath;
  protected static String reportFileName;
  protected String spectrumFileName;
  private PrintWriter befores;
  private PrintWriter afters;
  private int done;
   
  private static HashMap<String, CSVStatistics> reports = new HashMap<>();
  private static HashMap<String, PrintWriter> files = new HashMap<>();
  
//  @SuppressWarnings("unused") static List<HashMap<String, CSVStatistics>> reports = new ArrayList<HashMap<String, CSVStatistics>>();
//  
//  public void setReport(final CSVStatistics r, final String name){
//    HashMap map = new HashMap<String, CSVStatistics>();
//    map.put(name, r);
//    reports.add(map);
//  }
  
  public static void initializeFile(final String fileName, final String id){
    try (PrintWriter w = new PrintWriter(new FileWriter(fileName));) {
      files.put(id,w);
    } catch (final IOException x) {
      x.printStackTrace();
    }
  }

//  public static void intialize() {
//    reportFileName = "/tmp/report.CSV";
//    try {
//      reports.put("metrics", new CSVStatistics(reportFileName, "metrics"));
//    } catch (final IOException x) {
//      x.printStackTrace();
//    }
//  }
  
  public static void intializeReport(final String reportFileName, final String id) {
//    reportFileName = "/tmp/report.CSV";
    try {
      reports.put(id, new CSVStatistics(reportFileName, id));
    } catch (final IOException x) {
      x.printStackTrace();
    }
  }

  private static CSVStatistics report(final String key) {
    return reports.get(key);
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
  
//  /** Setup PrintWriters
//   * @author matteo */
//  protected void setUpPrintWriters() {
//    try (PrintWriter b = new PrintWriter(new FileWriter(beforeFileName)); //
//        PrintWriter a = new PrintWriter(new FileWriter(afterFileName))) {
//      befores = b;
//      afters = a;
//    } catch (final IOException x) {
//      x.printStackTrace();
//      System.err.println(done + " items processed; processing of " + inputPath + " failed for some I/O reason");
//    }
//  }

  public static void printFile(final Object input, final String key) {
    
    System.out.println(input);
    files.get(key).print(input);
  }
  
  public static void closeFile(final String key) {
    files.get(key).close();
  }
  
//  public static void close(final Consumer<HashMap> c, final String key){
//    c -> (c.get(key)).close();    
//  }
}
