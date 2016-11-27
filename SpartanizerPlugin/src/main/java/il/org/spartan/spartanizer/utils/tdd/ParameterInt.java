package il.org.spartan.spartanizer.utils.tdd;

public final class ParameterInt {
  private int value;
  private boolean hasValue;
  private boolean hasDefault;
  
  public ParameterInt(){ }
  
  public ParameterInt(int defaultValue){
    value = defaultValue;
    hasDefault = true;
  }
  
  public boolean hasValue(){
    return hasValue;
  }
  
  public int intValue() {
    if (!hasValue && !hasDefault)
      throw new IllegalArgumentException();
    return value;
  }
  
  public void set(int v){
    if (hasValue)
      throw new IllegalArgumentException();
    value = v;
    hasValue = true;
  }
  
  public boolean hasDefault(){
    return hasDefault;
  }
  
}
