package il.org.spartan;

/**
 * @author Ori Marcovitch
 * @since 2016 */
public class MethodDeclarationMockclass {
  
  private int field;
  
  void f(int a){}
  
  @SuppressWarnings("static-method") public void fooSet(int input){
    int temp = 3 + input;
//    this.field = input;
  }
  
  @SuppressWarnings("static-method") public String fooGet(){
    return "";
  }
  
}
