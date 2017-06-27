package il.org.spartan.Leonidas.plugin;

import com.intellij.psi.PsiFile;
import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Amir Sagiv
 * @since 03-05-2017
 */
public class ToolboxTest extends PsiTypeHelper {

    private Toolbox tb = new Toolbox();


    public void testGetAllTippers() throws Exception {
        tb.initComponent();
        List<Tipper> list = tb.getAllTippers();
        List<String> names = new ArrayList<>();
        list.forEach(tipper -> names.add(tipper.name()));

        assert names.contains("SafeReference");
        assert names.contains("Unless");
        assert names.contains("Delegator");
        assert names.contains("RemoveCurlyBracesFromIfStatement");
        assert names.contains("IfEmptyThen");
        assert names.contains("LispLastElement");
        assert names.contains("DefaultsTo");
        assert names.contains("RemoveCurlyBracesFromWhileStatement");
        assert names.contains("LambdaExpressionRemoveRedundantCurlyBraces");
        assert names.contains("IfDoubleNot");

        tb.disposeComponent();
    }

    public void testGetCurrentTippers() throws Exception {
        tb.initComponent();
        List<String> currNames = tb.getAllTippers().stream().map(Tipper::name).collect(Collectors.toList());

        assert !currNames.contains("myOpt");
        assert currNames.contains("SafeReference");
        assert currNames.contains("Unless");
        assert currNames.contains("Delegator");
        assert currNames.contains("RemoveCurlyBracesFromIfStatement");
        assert currNames.contains("IfEmptyThen");
        assert currNames.contains("LispLastElement");
        assert currNames.contains("DefaultsTo");
        assert currNames.contains("RemoveCurlyBracesFromWhileStatement");
        assert currNames.contains("LambdaExpressionRemoveRedundantCurlyBraces");
        assert currNames.contains("IfDoubleNot");

        tb.disposeComponent();
    }

    public void testUpdateTipperList() throws Exception {
        tb.initComponent();
        List<Tipper> list = tb.getAllTippers();
        List<String> names = new ArrayList<>();
        list.forEach(tipper -> names.add(tipper.name()));

        names.remove("SafeReference");
        names.remove("Unless");
        tb.updateTipperList(names);
        list = tb.getCurrentTippers();
        List<String> currNames = new ArrayList<>();
        list.forEach(tipper -> currNames.add(tipper.name()));


        assert !currNames.contains("SafeReference");
        assert !currNames.contains("Unless");
        assert currNames.contains("Delegator");
        assert currNames.contains("RemoveCurlyBracesFromIfStatement");
        assert currNames.contains("IfEmptyThen");

        names.remove("SafeReference");
        names.add("Unless");
        tb.updateTipperList(names);
        list = tb.getCurrentTippers();
        List<String> finalNames = new ArrayList<>();
        list.forEach(tipper -> finalNames.add(tipper.name()));

        assert !finalNames.contains("SafeReference");
        assert finalNames.contains("Unless");
        assert finalNames.contains("Delegator");
        assert finalNames.contains("RemoveCurlyBracesFromIfStatement");
        assert finalNames.contains("IfEmptyThen");

        tb.disposeComponent();
    }

    public void testIsElementOfOperableType() throws Exception {
        tb.initComponent();
        assert tb.isElementOfOperableType(createTestStatementFromString("if(true == false){}"));
        assert tb.isElementOfOperableType(createTestStatementFromString("while(true){}"));
        tb.disposeComponent();
    }

    public void testCanTip() throws Exception {
        tb.initComponent();
        assert tb.canTip(createTestIfStatement("true", "x++;"));
        assert tb.canTip(createTestWhileStatementFromString("while(true){x++;}"));

        tb.disposeComponent();
    }

    public void testGetTipper() throws Exception {
        tb.initComponent();
        assertEquals(tb.getTipper(createTestStatementFromString("if(true){x++;}")).name(), "RemoveCurlyBracesFromIfStatement");
        assertEquals(tb.getTipper(createTestStatementFromString("while(true){x++;}")).name(), "RemoveCurlyBracesFromWhileStatement");
        assertEquals(tb.getTipper(createTestStatementFromString("x += 1;")).name(), "ReplaceIdPlusOneWithIdPlusPlus");

        tb.disposeComponent();
    }

    public void testCheckExcluded() throws Exception {
        tb.initComponent();
        PsiFile f = createTestFileFromString("class A{}");
        tb.excludeFile(f);
        assert tb.checkExcluded(f);
        tb.includeFile(f);
        assert !tb.checkExcluded(f);

        tb.disposeComponent();
    }


}