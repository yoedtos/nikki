package net.yoedtos.nikki.it;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Suite.class)
@SuiteClasses({CommandLine.class})
public class SuiteIT extends BaseIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(SuiteIT.class);

    @BeforeClass
    public static void create() {
        LOGGER.debug("Create {}", SuiteIT.class.getSimpleName());
        if (configFile.exists()) {
            LOGGER.debug("Deleted: {}", configFile);
            configFile.delete();
        }
    }

    @AfterClass
    public static void destroy() {
        LOGGER.debug("Destroy {}", SuiteIT.class.getSimpleName());
        if (dbFile.exists()) {
            LOGGER.debug("Deleted: {}", dbFile);
            dbFile.delete();
        }
        if (configFile.exists()) {
            LOGGER.debug("Deleted: {}", configFile);
            configFile.delete();
        }
    }
}
