package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.*;

import il.org.spartan.*;
import il.org.spartan.csv.*;

public class Reports {
  
  protected String folder = "/tmp/";
  protected String afterFileName;
  protected String beforeFileName;
  protected String inputPath;
  protected static String reportFileName;
  protected String spectrumFileName;

  private static HashMap<String, CSVStatistics> map = new HashMap<String, CSVStatistics>(); 
  
  public static void intitialize(){
    reportFileName = "/tmp/report.CSV";
    try {
      map.put("metrics", new CSVStatistics(reportFileName, "metrics"));
    } catch (IOException x) {
      x.printStackTrace();
    }
  }
  
  private static CSVStatistics report(String key) {
    return map.get(key);
  }
  
  @SuppressWarnings("static-access") 
  public static void reportMetrics(final ASTNodeMetrics nm, final String id, final String key){
    report(key)//
    .put("Nodes" + id, nm.nodes())//
    .put("Body" + id, nm.body())//
    .put("Length" + id, nm.length())//
    .put("Tokens" + id, nm.tokens())//
    .put("Tide" + id, nm.tide())//
    .put("Essence" + id, nm.essence())//
    .put("Statements" + id, nm.statements());//
  }
  
  /**
   * 
   * @param nm1
   * @param nm2
   */
  
  @SuppressWarnings("static-access") 
  public static void reportDifferences(final ASTNodeMetrics nm1, 
      final ASTNodeMetrics nm2, final String key){
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
  
  /**
   * 
   * @param nm
   */
  
  @SuppressWarnings("static-access") public static void reportRatio(final ASTNodeMetrics nm, final String id, final String key){
    report(key) //
//    .put("Words)", wordCount).put("R(T/L)", system.ratio(length, tide)) //
    .put("R(E/L)" + id, system.ratio(nm.length(), nm.essence())) //
    .put("R(E/T)" + id, system.ratio(nm.tide(), nm.essence())) //
    .put("R(B/S)" + id, system.ratio(nm.nodes(), nm.body())); //
  }

  public static void initialize() {
    // TODO Auto-generated method stub
    
  }

  public static void close(String key) {
    report(key).close();
    }

  public static void summaryFileName(String key) {
    report(key).summaryFileName();
  }

  public static void nl(String key) {
    report(key).nl();
  }

  
}
