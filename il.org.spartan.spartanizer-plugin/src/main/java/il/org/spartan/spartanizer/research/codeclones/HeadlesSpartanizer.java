package il.org.spartan.spartanizer.research.codeclones;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.omg.CORBA.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.utils.*;

/** __ this class you can spartanize a directory easily. Or you can extends this
 * class and configure it to fit to your own needs.
 * @author oran1248
 * @author Matteo Orru'
 * @since 2017-04-11 */
public class HeadlesSpartanizer extends GrandVisitor {
  
  static HeadlesSpartanizer hs;
  
  public static void main(final String[] args){
    hs = new HeadlesSpartanizer(args);
    System.err.println("hs.current.location:\t" + hs.current.location);
    hs.go(hs.current.locations.get(0));
  }
  
  public HeadlesSpartanizer(final String[] args){
    super(args);
  }
  
  public HeadlesSpartanizer() {
  }

  public final TextualTraversals traversals = new TextualTraversals();

  protected void setUp() {
    /**/
  }
  protected void tearDown() {
    /**/
  }
  @SuppressWarnings("static-method") protected boolean spartanize(@SuppressWarnings("unused") final File __) {
    return true;
  }
  @SuppressWarnings("static-method") protected ASTVisitor astVisitor() {
    return new ASTVisitor() {/**/};
  }
  protected String perform(final String fileContent) {
    return fixedPoint(fileContent);
  }
  
  JavaProductionFilesVisitor v;
  
  public final void go(final String dirPath) {
    setUp();
    (new GrandVisitor(new String[] {dirPath}) {
      @Override public void visitFile(final File f) {
        current.fileName = f.getName();
        try {
          current.relativePath = Paths.get(f.getCanonicalPath()).subpath(Paths.get(inputFolder).getNameCount(), Paths.get(f.getCanonicalPath()).getNameCount()) + "";
          System.out.println(current.relativePath);
        } catch (IOException ¢) {
          ¢.printStackTrace();
        }
        //System.err.println(f.getAbsolutePath());
        if (!spartanize(f))
          return;
        String beforeChange;
        try {
          beforeChange = FileUtils.read(f);
          analyze(beforeChange, perform(beforeChange));
        } catch (final IOException ¢) {
          note.io(¢);
        }
      }
      
      protected void analyze(@SuppressWarnings("unused") final String before, final String after) {
        try {
          System.err.println(current.location);
          current.location = "/tmp/";
          System.err.println(current.relativePath);
          System.err.println(outputFolder + File.separator + current.fileName);
          System.err.println("parent: " + Paths.get(current.relativePath).getParent());
          new File(outputFolder + File.separator + Paths.get(current.relativePath).getParent()).mkdirs();
          FileUtils.writeToFile(outputFolder + File.separator + current.relativePath , after);
        } catch (final FileNotFoundException ¢) {
          note.io(¢);
        }
      }
      
      @SuppressWarnings("unused")
      protected void mkDirs(final File root, final List<String> dirs, final int depth) {
        if (depth != 0)
          for (String s : dirs) {
            File subdir = new File(root, s);
            subdir.mkdir();
            mkDirs(subdir, dirs, depth - 1);
          }
      }
    }).visitAll(astVisitor());
    tearDown();
  }
  public final String fixedPoint(final String from) {
    return traversals.fixed(from);
  }
}
