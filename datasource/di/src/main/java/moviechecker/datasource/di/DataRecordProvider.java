package moviechecker.datasource.di;

import java.util.List;

public interface DataRecordProvider {

	List<DataRecord> retrieveData() throws Exception;

}
