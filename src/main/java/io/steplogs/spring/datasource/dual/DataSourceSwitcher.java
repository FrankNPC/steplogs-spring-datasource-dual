package io.steplogs.spring.datasource.dual;

import java.util.List;

public interface DataSourceSwitcher {

	public List<String> createReaderDataSourceKeys(int readerCount);

	public List<String> createWriterDataSourceKeys(int writerCount);

	public String toWriterDataSource();

	public String toReaderDataSource();

	public void clearSourceKey();
	
	public String getCurrentDataSourceKey();
	
}