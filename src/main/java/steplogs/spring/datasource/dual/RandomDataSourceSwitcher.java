package steplogs.spring.datasource.dual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomDataSourceSwitcher implements DataSourceSwitcher {

	private static final ThreadLocal<String> DataSourceKeyContainer = new ThreadLocal<>();

	private static final String DATA_SOURCE_KEY_READER = "READER_DATA_SOURCE_";
	
	private static final String DATA_SOURCE_KEY_WRITER = "WRITER_DATA_SOURCE_";

	private List<String> readerSourceKeyList = null;
	
	private List<String> writerSourceKeyList = null;
	
	@Override
	public List<String> createReaderDataSourceKeys(int readerCount){
		List<String> sourceKeys = new ArrayList<>();
		for(int i=0; i<readerCount; i++) {
			sourceKeys.add(DATA_SOURCE_KEY_READER+i);
		}
		return readerSourceKeyList = Collections.unmodifiableList(sourceKeys);
	}

	@Override
	public List<String> createWriterDataSourceKeys(int writerCount){
		List<String> sourceKeys = new ArrayList<>();
		for(int i=0; i<writerCount; i++) {
			sourceKeys.add(DATA_SOURCE_KEY_WRITER+i);
		}
		return writerSourceKeyList = Collections.unmodifiableList(sourceKeys);
	}

	private static final ThreadLocal<Random> RandomContainer = new ThreadLocal<>();

	@Override
	public String toWriterDataSource() {
		if (writerSourceKeyList==null) {
			return null;
		}

		String key = DataSourceKeyContainer.get();
		if (key!=null && key.startsWith(DATA_SOURCE_KEY_WRITER)) {
			return key;
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
		if (readerSourceKeyList==null) {
			return null;
		}

		Random random = RandomContainer.get();
		if (random==null) {
			RandomContainer.set(random = new Random());
		}

		String key = readerSourceKeyList.get(random.nextInt(readerSourceKeyList.size()));
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
				key = readerSourceKeyList.get(0);
			}else if (writerSourceKeyList!=null && !writerSourceKeyList.isEmpty()) {
				key = writerSourceKeyList.get(0);
			}
			
			if (key!=null) {
				DataSourceKeyContainer.set(key);
			}
		}else if (key.startsWith(DATA_SOURCE_KEY_READER)){
			return toReaderDataSource();
		}
		return key;
	}
}