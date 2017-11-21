package tests;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

import metatester.MetaTester;

@RunWith(MetaTester.class)
@SuppressWarnings("static-method")
public class SimpleTest {

	public int f() {
		return 6;
	}

	@Test
	public void test1() {
		assert 3 < 2;
		assertTrue(3 < 2);
		assert 1 == 1;
		assert 0 == 1;
		assert 2 == 2;
		assert f() < 2;
		assertEquals(f(), 2);
		assertThat(f(), is(lessThan(2)));

	}

}
