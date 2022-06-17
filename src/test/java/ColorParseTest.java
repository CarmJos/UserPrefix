import cc.carm.lib.easyplugin.utils.ColorParser;
import org.junit.Test;

public class ColorParseTest {


    @Test
    public void onTest() {
        String testString = "&f爱的人永远不爱我，为何付出得到的只有&(#aaaaaa)背叛。";

        System.out.println(ColorParser.parseHexColor(testString));
    }

}
