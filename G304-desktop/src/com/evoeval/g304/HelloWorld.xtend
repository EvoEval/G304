package com.evoeval.g304 
class HelloWorld {
	
	def static void main(String[] args) {
		val testVar = new TestClass("Hello","World");
		val testVar2 = new TestClass("Hello","World");
		val t = if(testVar==testVar2) " true" else " false"
		println(testVar + t)
	}


}

@Data class TestClass {
	@Property private String test1
	@Property private String test2	
	
	override toString() {
		return test1 + " " + test2
	}
	
}