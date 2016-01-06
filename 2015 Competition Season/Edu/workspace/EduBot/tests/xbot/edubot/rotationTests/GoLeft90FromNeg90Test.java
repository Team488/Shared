package xbot.edubot.rotationTests;

import org.junit.Test;

public class GoLeft90FromNeg90Test extends BaseOrientationEngineTest {
	
	@Test
	public void goLeft90FromNeg90()
	{
		run90ClassTest(-90, 0);
	}
}
