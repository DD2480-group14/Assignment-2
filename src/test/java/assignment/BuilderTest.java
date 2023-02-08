package assignment;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import java.nio.file.StandardOpenOption;

public class BuilderTest {
    @Before
    public void createJson() {
        try {
            Files.write(Paths.get("builds.json"),
                        (new JSONArray()).toString().getBytes(),
                        StandardOpenOption.CREATE);
        } catch (Exception e) {}
    }

    // clone existing commit
    @Test
    public void testClone() {
        try {
            Builder.build("559a247f76d0fbc436c51193a439130bb232851c");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    // clone non existing commit
    @Test
    public void testClone2() {
        try {
            Builder.build("1234567899");
            Assert.fail();
        } catch (Exception e) {}
    }
}
