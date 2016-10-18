package il.org.spartan.plugin.revision;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.*;

/**
 * Selection useful to deal with projects using the command line
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLineSelection extends AbstractSelection {
  
  private CompilationUnit compilationUnit;
  private List<WrappedCompilationUnit> compilationUnits;

  public CommandLineSelection(final List<WrappedCompilationUnit> compilationUnits, final String name){
    this.compilationUnits = compilationUnits != null ? compilationUnits : new ArrayList<>();
    this.name = name;
  }
  
  public List<CompilationUnit> getCompilationUnits() {
    List<CompilationUnit> $ = new ArrayList<>();
    for (WrappedCompilationUnit ¢: compilationUnits)
      $.add(¢.compilationUnit);
    return $;
  }
  
  public List<WrappedCompilationUnit> get() {
    return compilationUnits;
  }
  
  public static class Util{

    /**
     * @return
     */
    public static Object getAllCompilationUnits() {
      return getSelection();
    }

    /**
     * @return
     */
    private static Object getSelection() {
      return null;
    }
    
    /**
     * @return
     */
    public static AbstractSelection get() {
      List<WrappedCompilationUnit> cuList = new ArrayList<>();
      for (final File ¢ : new FilesGenerator(".java").from(".")) {
        WrappedCompilationUnit cu = WrappedCompilationUnit.of((CompilationUnit) makeAST.COMPILATION_UNIT.from(¢));
        cuList.add(cu);
      }
      return new CommandLineSelection(cuList,"default");
    }
    
    public static AbstractSelection getFromPath(String path) {
      List<WrappedCompilationUnit> cuList = new ArrayList<>();
      for (final File ¢ : new FilesGenerator(".java").from(path)) {
        WrappedCompilationUnit cu = WrappedCompilationUnit.of((CompilationUnit) makeAST.COMPILATION_UNIT.from(¢));
        cuList.add(cu);
      }
      return new CommandLineSelection(cuList,"selection");
    }
 
  }
  
  /**
   * @param inputPath
   * @return
   */
  public void createSelectionFromProjectDir(String inputPath) {
    List<WrappedCompilationUnit> cuList = new ArrayList<>();
    for (final File ¢ : new FilesGenerator(".java").from(inputPath)) {
      System.out.println("Free memory (bytes): " + Unit.BYTES.format(Runtime.getRuntime().freeMemory()));
      WrappedCompilationUnit cu = WrappedCompilationUnit.of((CompilationUnit) makeAST.COMPILATION_UNIT.from(¢));
      cuList.add(cu);
    }
    compilationUnits = cuList;
  }

  public CommandLineSelection buildAll() {
    for (WrappedCompilationUnit ¢ : compilationUnits)
      ¢.build();
    return this;
  }
  
//  public CU build() {
//    if (compilationUnit == null)
//      compilationUnit = makeAST.COMPILATION_UNIT.from("");
//    return this;
//  }
  
}
