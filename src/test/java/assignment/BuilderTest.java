package assignment;

import org.junit.Assert;
import org.junit.Test;

public class BuilderTest {
    // clone existing main branch
    @Test
    public void testClone() {
        Builder builder = new Builder("main", "123456789");
        Assert.assertTrue(builder.cloneRepo());
    }

    // clone non existing branch
    @Test
    public void testClone2() {
        Builder builder = new Builder("non-existing-branch", "1234567899");
        Assert.assertFalse(builder.cloneRepo());

    }
}
