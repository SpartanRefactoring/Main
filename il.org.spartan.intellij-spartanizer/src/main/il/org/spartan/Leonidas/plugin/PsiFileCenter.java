package il.org.spartan.Leonidas.plugin;

import com.intellij.psi.*;


import java.util.HashMap;

/**
 * @author Roey Maor
 * @since 15-06-2016
 */
public class PsiFileCenter {

    public enum CodeType{
        EXPRESSION, METHOD_BOUND, CLASS_BOUND, ENUM_BOUND, FILE_BOUND, GLOBAL, ILLEGAL
    }

    private static HashMap<CodeType,String> wrappingPrefixes;
    static {
        wrappingPrefixes.put(CodeType.CLASS_BOUND,"public class XCLASS{\n");
        wrappingPrefixes.put(CodeType.METHOD_BOUND,wrappingPrefixes.get(CodeType.CLASS_BOUND)+"public void XMETHOD(){\n");
        wrappingPrefixes.put(CodeType.EXPRESSION,wrappingPrefixes.get(CodeType.CLASS_BOUND)+wrappingPrefixes.get(CodeType.METHOD_BOUND)+"XMETHOD(");
        wrappingPrefixes.put(CodeType.ENUM_BOUND,wrappingPrefixes.get(CodeType.CLASS_BOUND)+"public enum XENUM{\n");
        wrappingPrefixes.put(CodeType.GLOBAL,"package XPACKAGE;\n");
        wrappingPrefixes.put(CodeType.FILE_BOUND,"");
    }

    private static HashMap<CodeType,String> wrappingPostfixes;
    static {
        wrappingPostfixes.put(CodeType.CLASS_BOUND,"\n}");
        wrappingPostfixes.put(CodeType.METHOD_BOUND,"\n}"+wrappingPostfixes.get(CodeType.CLASS_BOUND));
        wrappingPostfixes.put(CodeType.EXPRESSION,");"+wrappingPostfixes.get(CodeType.METHOD_BOUND));
        wrappingPostfixes.put(CodeType.ENUM_BOUND,"\n}"+wrappingPostfixes.get(CodeType.CLASS_BOUND));
        wrappingPostfixes.put(CodeType.GLOBAL,"\n"+wrappingPrefixes.get(CodeType.CLASS_BOUND)+wrappingPostfixes.get(CodeType.CLASS_BOUND));
        wrappingPostfixes.put(CodeType.FILE_BOUND,"");
    }

    /*
     * This is a PsiFile, that it's sole purpose is to hold within it some
     * sub-tree that was given by the user.
     */
    public class PsiFileWrapper {

        private CodeType codeType;
        private PsiFile file;

        public PsiFileWrapper(PsiFile file, CodeType type) {
            this.file = file;
            this.codeType = type;
        }

        public PsiFile getFile(){
            return this.file;
        }

        public String extractRelevantSubtreeString(){
            return null;
        }

        private PsiElement extractRelevantSubtree(){
            return null;
        }

    }

    public PsiFileWrapper createFileFromString(){
        return null;
    }
}
