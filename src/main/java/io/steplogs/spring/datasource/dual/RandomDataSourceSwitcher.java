package io.steplogs.spring.datasource.dual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomDataSourceSwitcher implements DataSourceSwitcher {

	private static final ThreadLocal<String> DataSourceKeyContainer = new ThreadLocal<>();

	private static final String READER_KEY = "READER_";
	
	private static final String WRITER_KEY = "WRITER_";

	private List<String> readerSourceKeyList = null;
	
	private List<String> writerSourceKeyList = null;
	
	@Override
	public List<String> createReaderDataSourceKeys(int readerCount){
		List<String> sourceKeys = new ArrayList<>();
		for(int i=0; i<readerCount; i++) {
			sourceKeys.add(READER_KEY+i);
		}
		return readerSourceKeyList = Collections.unmodifiableList(sourceKeys);
	}

	@Override
	public List<String> createWriterDataSourceKeys(int writerCount){
		List<String> sourceKeys = new ArrayList<>();
		for(int i=0; i<writerCount; i++) {
			sourceKeys.add(WRITER_KEY+i);
		}
		return writerSourceKeyList = Collections.unmodifiableList(sourceKeys);
	}

	private static final ThreadLocal<Random> RandomContainer = new ThreadLocal<>();

	@Override
	public String toWriterDataSource() {
		String key = DataSourceKeyContainer.get();
		if (key!=null && key.charAt(0)==WRITER_KEY.charAt(0)) {
			return key;
		}

		if (writerSourceKeyList==null) {
			return null;
		}

		Random random = RandomContainer.get();
		if (random==null) {
			RandomContainer.set(random = new Random());
		}

		key = writerSourceKeyList.get(random.nextInt(writerSourceKeyList.size()));
		DataSourceKeyContainer.set(key);
		return key;
	}

	@Override
	public String toReaderDataSource() {
		String key = DataSourceKeyContainer.get();
		if (key!=null && key.charAt(0)==READER_KEY.charAt(0)) {
			return key;
		}
		
		if (readerSourceKeyList==null) {
			return null;
		}

		Random random = RandomContainer.get();
		if (random==null) {
			RandomContainer.set(random = new Random());
		}

		key = readerSourceKeyList.get(random.nextInt(readerSourceKeyList.size()));
		DataSourceKeyContainer.set(key);
		return key;
	}

	@Override
	public void clearSourceKey() {
		DataSourceKeyContainer.remove();
	}

	@Override
	public String getCurrentDataSourceKey() {
		String key = DataSourceKeyContainer.get();
		if (key==null) {
			if (readerSourceKeyList!=null && !readerSourceKeyList.isEmpty()) {
				key = toReaderDataSource();
			} else {
				key = toWriterDataSource();
			}
			
			if (key!=null) {
				DataSourceKeyContainer.set(key);
			}
		}
		return key;
	}
}