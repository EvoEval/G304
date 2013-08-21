package com.evoeval.g304;

import com.evoeval.g304.TestClass;
import com.google.common.base.Objects;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class HelloWorld {
  public static void main(final String[] args) {
    TestClass _testClass = new TestClass("Hello", "World");
    final TestClass testVar = _testClass;
    TestClass _testClass_1 = new TestClass("Hello", "World");
    final TestClass testVar2 = _testClass_1;
    String _xifexpression = null;
    boolean _equals = Objects.equal(testVar, testVar2);
    if (_equals) {
      _xifexpression = " true";
    } else {
      _xifexpression = " false";
    }
    final String t = _xifexpression;
    String _plus = (testVar + t);
    InputOutput.<String>println(_plus);
  }
}
