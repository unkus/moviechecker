package moviechecker.datasource.di;

import java.util.Collection;

public interface DataRecordProvider {

	Collection<DataRecord> retrieveData() throws Exception;

}
