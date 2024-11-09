package steplogs.spring.datasource.dual.hikari;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import steplogs.spring.datasource.dual.DataSourceResolver;

public class HikariDataSourceResolver implements DataSourceResolver {
	
	private HikariConfig hikariConfig;
	
	private volatile DataSource dataSource;
	
	public HikariDataSourceResolver(HikariConfig hikariConfig) {
		this.hikariConfig = hikariConfig;
	}
	
	@Override
	public DataSource apply() {
		if (dataSource==null) {
			dataSource = new HikariDataSource(hikariConfig);
		}
		return dataSource;
	}

}
