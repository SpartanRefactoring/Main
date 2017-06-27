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

This class relies on PsiFileCenter
 */

public class TipperTest{


    Tipper tipper = null;
    LeonidasTipperDefinition leonidasTipper = null;
    Boolean leonidasMode; //whether we are testing a leonidas or non-leonidas tipper
    PsiTypeHelper junitTest; //The junit class instance that is being tested (the one that creates a TipperTest object)
    private Boolean setup = false;
    private Boolean printsToScreen;
    private Boolean crashed;
    public Boolean quietCrash;

    TipperTest(Tipper t, PsiTypeHelper test, Boolean prints, Boolean quietCrash){
        this.tipper = t;
        this.leonidasMode = false;
        this.junitTest = test;
        this.printsToScreen = prints;
        this.quietCrash = quietCrash;
    }

    TipperTest(LeonidasTipperDefinition l,PsiTypeHelper test,Boolean prints,Boolean quietCrash){
        this.leonidasTipper = l;
        this.leonidasMode = true;
        this.junitTest = test;
        this.printsToScreen = prints;
        this.quietCrash = quietCrash;
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

    private void crashTest(){
        crashed = true;
        if(!quietCrash){
            assert false;
        }
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
        log("\n*************************Now testing "+getTipperName()+":*************************\n");
        if(examples.entrySet().size() == 0){
            log("No examples exist for tipper "+getTipperName());
        }
        for (Map.Entry<String,String> entry : examples.entrySet()) {
            crashed= false;
            String key = entry.getKey();
            if(key == null){
                log("\nError! An example with a null key was inserted in tipper "+getTipperName()+".\n");
                crashTest();
                continue;
            }
            String value = entry.getValue();
            PsiFileCenter pfc = new PsiFileCenter();
            PsiFileCenter.PsiFileWrapper filewkey = pfc.createFileFromString(key);
            log("Testing example:\n"+key+"\n");
            if(filewkey.getCodeType() == PsiFileCenter.CodeType.ILLEGAL){
                log("\nError! The following key in the examples of the tipper "+getTipperName()+" contains illegal java code: \n"+key+"\n");
                crashTest();
                continue;
            }

            PsiFileCenter.PsiFileWrapper filewvalue = pfc.createFileFromString(value);
            if( value != null && filewkey.getCodeType() == PsiFileCenter.CodeType.ILLEGAL){
                log("\nError! The following value in the examples of the tipper "+getTipperName()+" contains illegal java code:\n"+value);
                crashTest();
                continue;
            }
            //log("before: \n"+filewkey.extractCanonicalSubtreeString()+"\n");
            //reassigning with fixed strings:
            filewkey = pfc.createFileFromString(filewkey.extractCanonicalSubtreeString());
            if(value != null){
                filewvalue = pfc.createFileFromString(filewvalue.extractCanonicalSubtreeString());
            }
            int tipperAffected;
            try {
                tipperAffected = toolbox.executeSingleTipper(filewkey.getFile(), getTipperName());
            } catch(Exception e){
                log("An exception was thrown. \n");
                if(printsToScreen){
                    e.printStackTrace();
                    log("\n");
                }
                crashTest();
                continue;
            }
            if(tipperAffected == -1){
                log("Could not find tipper named "+getTipperName()+" in the toolbox.\n");
                crashTest();
                continue;
            }
            if(tipperAffected==0){
                if(value != null) {
                    log("\nError! Tipper "+getTipperName()+" should have affected the example:\n"+key+"\nbut it didn't.\n");
                    crashTest();
                    continue;
                }
            }
            else{
                if(value == null){
                    log("\nError! Tipper "+getTipperName()+" should not have affected the example:\n"+key+"\nbut it did.\n");
                    crashTest();
                    continue;
                }
                else{
                    String keyAfterChange = filewkey.extractCanonicalSubtreeString();
                    if(!keyAfterChange.equals(filewvalue.extractCanonicalSubtreeString())){
                        log("\nError! Tipper "+getTipperName()+" didn't affect the example:\n"+key+"\nas expected. expected:\n"+filewvalue.extractCanonicalSubtreeString()+"\nbut got: \n"+keyAfterChange+"\n");
                        crashTest();
                        continue;
                    }
                }
            }

            //log("after: \n"+filewkey.extractCanonicalSubtreeString()+"\n");
            if(!crashed){log(".............OK.............\n");}
        }

        toolbox.testing = false;

    }
}
