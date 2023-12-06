package net.yoedtos.nikki.external.view;

import javafx.scene.Node;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.framework.junit.ApplicationTest;

public abstract class UIBaseTest extends ApplicationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UIBaseTest.class);

    @BeforeClass
    public static void initSetup() {
        if(System.getProperty("headless") != null) {
            LOGGER.info("Running in headless mode");
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
    }

    protected Node getNode(String nodeId, Class clazz) {
        return  lookup(nodeId).queryAs(clazz);
    }
}
