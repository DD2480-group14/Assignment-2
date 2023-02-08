package assignment;

import org.junit.Test;
import org.junit.Assert;

public class BuilderTest {
    // clone non existing commit
    @Test
    public void testClone2() {
        try {
            Builder.build("1234567899");
            Assert.fail();
        } catch (Exception e) {}
    }
}
