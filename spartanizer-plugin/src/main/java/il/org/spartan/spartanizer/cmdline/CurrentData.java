package il.org.spartan.spartanizer.cmdline;

import java.io.BufferedWriter;
import java.io.File;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;

/**
 * TODO Matteo Orru': document class 
 * @author  Matteo Orru'
 * @since  2017-08-10 
 */
public class CurrentData {
  public static File file;
  public static String fileName;
  public static String absolutePath;
  public static  String location;
  public static BufferedWriter out;
  public static List<String> locations;
  public static ASTVisitor visitor;
  public static String relativePath;
  public static String locationPath;
  public static String locationName;
  public static String before;
  public static String after;
}