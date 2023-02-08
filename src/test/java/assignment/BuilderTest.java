package assignment;

import org.junit.Assert;
import org.junit.Test;

public class BuilderTest {
    // clone existing main branch
    @Test
    public void testBuilder() {
        Builder builder = new Builder("main", "123456789");
        Assert.assertTrue(builder.cloneRepo());
        Assert.assertTrue(builder.buildRepo());

    }

    // clone non existing branch
    @Test
    public void testBuilder2() {
        Builder builder = new Builder("non-existing-branch", "1234567899");
        Assert.assertFalse(builder.cloneRepo());
        Assert.assertFalse(builder.buildRepo());
    }
}
