package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import il.org.spartan.Leonidas.PsiTypeHelper;;
import il.org.spartan.Leonidas.plugin.PsiFileCenter;
import il.org.spartan.Leonidas.plugin.Toolbox;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author @roey maor
 * @since 27-05-2017.
 */
/*
This class should be used by all tipper tests in order to test the tippers by the examples they provide.
It is correct to demand the creator of the tipper to provide the examples for testing, and it's he's
responsibility to make sure he has included examples that provide adequate coverage for testing.
examples can be in one of two forms:

1) String -> String : The tipper should be applied on the former, and output a PSI Element tree
                      that conforms to the latter
2) String -> null : The tipper should not affect the former.

 */

public class TipperTest{

    private static final String before = "package test;\n public class Foo\n{\n public void Bar(){\n";
    private static final String after = "\n }\n}";
    Tipper tipper = null;
    LeonidasTipperDefinition leonidasTipper = null;
    Boolean leonidasMode; //whether we are testing a leonidas or non-leonidas tipper
    PsiTypeHelper junitTest; //The junit class instance that is being tested (the one that creates a TipperTest object)
    private Boolean setup = false;
    private Boolean printsToScreen;

    TipperTest(Tipper t, PsiTypeHelper test, Boolean prints){
        this.tipper = t;
        this.leonidasMode = false;
        this.junitTest = test;
        this.printsToScreen = prints;
    }

    TipperTest(LeonidasTipperDefinition l,PsiTypeHelper test,Boolean prints){
        this.leonidasTipper = l;
        this.leonidasMode = true;
        this.junitTest = test;
        this.printsToScreen = prints;
    }



    private Map<String,String> getExamples(){
        if(leonidasMode){
            return leonidasTipper.getExamples();
        }
        return tipper.getExamples();
    }

    private String getTipperName(){
        if(leonidasMode){
            return leonidasTipper.getClass().getSimpleName();
        }
        return tipper.name();
    }

    private void setUp(){
        if(setup) return;
        setup = true;
    }

    private void log(String s){
        if(!printsToScreen) {return;}
        System.out.println(s);
    }

    public void check(){
        if(!setup) {
            try {
                this.setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String,String> examples = getExamples();
        Toolbox toolbox = Toolbox.getInstance();
        toolbox.testing = true;
        //System.out.println(toolbox.getAllTippers().stream().map(t -> t.name()).collect(Collectors.toList()));
        for (Map.Entry<String,String> entry : examples.entrySet()) {
            String key = entry.getKey();
            if(key == null){
                log("An example with a null key was inserted in tipper "+getTipperName()+". aborting test.");
                assert(false);
            }
            String value = entry.getValue();
            PsiFileCenter pfc = new PsiFileCenter();
            PsiFileCenter.PsiFileWrapper filewkey = pfc.createFileFromString(key);
            if(filewkey.getCodeType() == PsiFileCenter.CodeType.ILLEGAL){
                log("The following key in the examples of the tipper "+getTipperName()+" contains illegal java code. aborting test: \n"+key);
                assert(false);
            }
            PsiFileCenter.PsiFileWrapper filewvalue = pfc.createFileFromString(value);
            if( value != null && filewkey.getCodeType() == PsiFileCenter.CodeType.ILLEGAL){
                log("The following value in the examples of the tipper "+getTipperName()+" contains illegal java code. aborting test: \n"+value);
                assert(false);
            }
            log("before: \n"+filewkey.extractCanonicalSubtreeString()+"\n");
            log(filewkey.getCodeType()+"");
            Boolean tipperAffected = toolbox.executeSingleTipper(filewkey.getFile(),getTipperName());
            if(!tipperAffected){
                if(value != null) {
                    log("Tipper "+getTipperName()+" should have affected the example\n: "+filewkey.extractCanonicalSubtreeString()+"\n but it didn't. aborting test.");
                    assert(false);
                }
            }
            else{
                if(value == null){
                    log("Tipper "+getTipperName()+" should not have affected the example:\n "+key+"\n but it did. aborting test.");
                    assert(false);
                }
                else{
                    String keyAfterChange = filewkey.extractCanonicalSubtreeString();
                    if(!keyAfterChange.equals(filewvalue.extractCanonicalSubtreeString())){
                        log("Tipper "+getTipperName()+" didn't affect the example:\n "+key+" \nas expected. expected:\n"+filewvalue.extractCanonicalSubtreeString()+"\n but got: \n"+keyAfterChange);
                        assert(false);
                    }
                }
            }

            log("after: \n"+filewkey.extractCanonicalSubtreeString()+"\n");

        }

        toolbox.testing = false;
    }
    private boolean byExample(String input, String output){
        if(!setup) {
            try {
                this.setUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(leonidasMode){
            return leonidasTipperByExample(input,output);
        }
        else{
            return regularTipperByExample(input,output);
        }
    }

    private boolean leonidasTipperByExample(String input,String output){
        return false;
    }

    private boolean regularTipperByExample(String input, String output){
        return false;
    }

    //wrap in class
    //wrap in method
    //inside a method call - expression
}
