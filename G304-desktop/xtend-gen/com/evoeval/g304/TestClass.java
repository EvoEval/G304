package com.evoeval.g304;

import org.eclipse.xtend.lib.Data;

@Data
@SuppressWarnings("all")
public class TestClass {
  private final String _test1;
  
  public String getTest1() {
    return this._test1;
  }
  
  private final String _test2;
  
  public String getTest2() {
    return this._test2;
  }
  
  public String toString() {
    String _test1 = this.getTest1();
    String _plus = (_test1 + " ");
    String _test2 = this.getTest2();
    return (_plus + _test2);
  }
  
  public TestClass(final String test1, final String test2) {
    super();
    this._test1 = test1;
    this._test2 = test2;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_test1== null) ? 0 : _test1.hashCode());
    result = prime * result + ((_test2== null) ? 0 : _test2.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TestClass other = (TestClass) obj;
    if (_test1 == null) {
      if (other._test1 != null)
        return false;
    } else if (!_test1.equals(other._test1))
      return false;
    if (_test2 == null) {
      if (other._test2 != null)
        return false;
    } else if (!_test2.equals(other._test2))
      return false;
    return true;
  }
}
