package spork.injection.test;

import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Singleton;

import spork.Spork;
import spork.injection.Provides;

import static org.junit.Assert.assertEquals;

public class SingletonTests {
	private static class Parent {
		@Inject
		int counter = -1;
	}

	public class Module {
		private int counter = 1;

		@Provides
		@Singleton
		public int counter() {
			return counter++;
		}
	}

	@Test
	public void test() {
		Parent parent = new Parent();
		Module module = new Module();

		// ensure wrong defaults
		parent.counter = -1;

		Spork.bind(parent, module);
		assertEquals(1, parent.counter);
		Spork.bind(parent, module);
		assertEquals(1, parent.counter);
	}
}