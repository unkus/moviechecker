package moviechecker.datasource.provider;

import moviechecker.database.di.DataRecord;

import java.util.List;

public interface MovieProvider {

	List<DataRecord> retrieveData() throws Exception;

}
