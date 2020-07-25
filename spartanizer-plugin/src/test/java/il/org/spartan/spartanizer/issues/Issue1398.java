package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.junit.Test;

import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.tippers.LocalInitializedInlineIntoNext;

/** Test case for bug in {@link LocalInitializedInlineIntoNext} (occured from
 * {@link step#type} )
 * @author Yuval Simon
 * @since 2017-05-27 */
@SuppressWarnings("static-method")
public class Issue1398 {
  @Test public void t11() {
    trimmingOf("final double vs[]={5,20,40,80,100}; Assert.assertEquals(40.0625,correctedSd(vs),1E-4);")
        .gives("Assert.assertEquals(40.0625,correctedSd((new double[]{5,20,40,80,100})),1E-4);");
  }
  @Test public void t12() {
    trimmingOf("final double vs[][] = {{3,4},{5,6}}; Assert.assertEquals(40.0625,correctedSd(vs),1E-4);")
        .gives("Assert.assertEquals(40.0625,correctedSd((new double[][]{{3,4},{5,6}})),1E-4);");
  }
  @Test public void t13() {
    trimmingOf("final double vs[][] = {{3,4},{5,6}}; Assert.assertEquals(40.0625,correctedSd(vs),1E-4);")
        .gives("Assert.assertEquals(40.0625,correctedSd((new double[][]{{3,4},{5,6}})),1E-4);");
  }
  @Test public void t21() {
    trimmingOf("NetworkConnections<N, E> connectionsV = nodeConnections.get(nodeV) == null ? addNodeInternal(nodeV) : nodeConnections.get(nodeV);"
        + "connectionsV.addInEdge(edge, nodeU, isSelfLoop);")
            .gives("(nodeConnections.get(nodeV) == null ? addNodeInternal(nodeV) : nodeConnections.get(nodeV)).addInEdge(edge, nodeU, isSelfLoop);");
  }
  @Test public void t31() {
    trimmingOf("int hiCount = hiChars != null ? hiChars.length : 1;int loCount = loChars == null ? 1 : loChars.length;"
        + "char[] output = new char[hiCount + loCount];").using(new LocalInitializedInlineIntoNext())
            .gives("int hiCount = hiChars != null ? hiChars.length : 1;char[] output = new char[hiCount + (loChars == null ? 1 : loChars.length)];");
  }
}
