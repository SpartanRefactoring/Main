package il.org.spartan.Leonidas.plugin.GUI;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import il.org.spartan.Leonidas.plugin.GUI.PlaygroundController.Playground;

/**
 * @author Anna Belozovsky
 * @since 06/05/2017
 */
public class PlaygroundTest extends LightPlatformCodeInsightFixtureTestCase {

    private Playground playground;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        playground = new Playground();
    }

    private void preparePlayground(String input) {
        playground.doClose();
        playground.setInput(input);
        playground.doSpartanization();
    }

    @Override
    protected void tearDown() throws Exception {
        playground = null;
        super.tearDown();
    }

    public void testLeonidasTipper1() {
        preparePlayground("if(x==0){\n\tx=1;\n}");
        assert "if(x==0)x=1;".equals(playground.getOutput().replaceAll("\\s+", ""));
    }

    public void testLeonidasTipper2() {
        preparePlayground("while(x==0){x=1;}");
        assert "while(x==0)x=1;".equals(playground.getOutput().replaceAll("\\s+", ""));
    }

    public void testLeonidasTipper3() {
        preparePlayground("!(!(x))");
        assert "x".equals(playground.getOutput());
    }

    public void testNanoTipper1() {
        preparePlayground("l.get(l.size()-1)");
        assert "last(l)".equals(playground.getOutput());
    }

    public void testNanoTipper2() {
        preparePlayground("X == null ? Y : X");
        assert "defaults(X).to(Y)".equals(playground.getOutput());
    }

    public void testNanoTipper3() {
        preparePlayground("i->{return i;}");
        assert "i-> i".equals(playground.getOutput());
    }

    public void testClear() {
        preparePlayground("x");
        assert "x".equals(playground.getOutput());
        playground.doClear();
        assert "".equals(playground.getOutput());
        assert "".equals(playground.getInput());
    }

    public void testClose() {
        assert playground.isDisplayable();
        playground.doClose();
        assert !playground.isDisplayable();
    }
}
