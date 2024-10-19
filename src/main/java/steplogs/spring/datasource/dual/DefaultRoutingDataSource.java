package steplogs.spring.datasource.dual;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import jakarta.annotation.Resource;

public class DefaultRoutingDataSource extends AbstractRoutingDataSource {
	
	@Resource
	DataSourceSwitcher dataSourceSwitcher;
	
	@Override
	protected Object determineCurrentLookupKey() {
		return dataSourceSwitcher.getCurrentDataSourceKey();
	}
	
}