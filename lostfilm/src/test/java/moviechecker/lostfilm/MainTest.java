package moviechecker.lostfilm;

import moviechecker.datasource.di.DataRecord;
import moviechecker.datasource.di.DataRecordProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestApplication.class)
public class MainTest {

    @Autowired
    private DataRecordProvider provider;

    @Test
    public void retrieveDataTest() throws Exception {
        List<DataRecord> records = provider.retrieveData();

        assertNotNull(records, "Received collection can not be null");
        assertNotEquals(0, records.size(), "date must be received");
    }
}
