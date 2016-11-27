package il.org.spartan.spartanizer.utils.tdd;

public final class ParameterInt {
  private int value;
  
  public ParameterInt(){ }
  
  public ParameterInt(int defaultValue){
    value = defaultValue;
  }
  
  public int intValue(){
    return value;
  }
  
  public void set(int v){
    value = v;
  }
  
}
