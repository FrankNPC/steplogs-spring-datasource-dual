package steplogs.spring.datasource.dual.hikari;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.annotation.Resource;
import steplogs.spring.datasource.dual.DataSourceResolver;
import steplogs.spring.datasource.dual.DataSourceSwitcher;
import steplogs.spring.datasource.dual.DefaultRoutingDataSource;

@Configuration
public class HikariRoutingDataSourceConfiguration {

	@Resource
	HikariConfigForReader hikariConfigForReader;
	
	@Resource
	HikariConfigForWriter hikariConfigForWriter;

	@Resource
	DataSourceSwitcher dataSourceSwitcher;
	
	private DataSourceResolver getDataSourceResolver(HikariConfig hikariConfig) {
		return new DataSourceResolver() {
			private DataSource dataSource;
			@Override
			public DataSource apply() {
				if (dataSource==null) {
					dataSource = new HikariDataSource(hikariConfig);
				}
				return dataSource;
			}
		};
	}
	
	@Bean("DefaultRoutingDataSource")
	public DefaultRoutingDataSource getRoutingDataSources() {
		DefaultRoutingDataSource routingDataSource = new DefaultRoutingDataSource();

		DataSource defaultDataSource = null;
		Map<Object, Object> dataSources = new HashMap<>();
		if (hikariConfigForReader.getInstanceCount()>0) {
			defaultDataSource = new HikariLazyDataSource(getDataSourceResolver(hikariConfigForReader.getHikariConfig(0)));
			
			List<String> readerDataSourceKeys = dataSourceSwitcher.createReaderDataSourceKeys(hikariConfigForReader.getInstanceCount());
			for(int i=0; i<readerDataSourceKeys.size(); i++) {
				dataSources.put(readerDataSourceKeys.get(i), 
					new HikariLazyDataSource(getDataSourceResolver(hikariConfigForReader.getHikariConfig(i))));
			}
		}
		
		if (hikariConfigForWriter.getInstanceCount()>0) {
			defaultDataSource = new HikariLazyDataSource(getDataSourceResolver(hikariConfigForWriter.getHikariConfig(0)));

			List<String> writerDataSourceKeys = dataSourceSwitcher.createWriterDataSourceKeys(hikariConfigForWriter.getInstanceCount());
			for(int i=0; i<writerDataSourceKeys.size(); i++) {
				dataSources.put(writerDataSourceKeys.get(i), 
					new HikariLazyDataSource(getDataSourceResolver(hikariConfigForWriter.getHikariConfig(i))));
			}
		}
		if (!dataSources.isEmpty()) {
			routingDataSource.setTargetDataSources(dataSources);
		}
		if (defaultDataSource!=null) {
			routingDataSource.setDefaultTargetDataSource(defaultDataSource);
		}
		return routingDataSource;
	}
	
//	@Bean
//	public SqlSessionFactory sqlSessionFactory(DataSource routingDataSource) throws Exception {
//		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//		sqlSessionFactoryBean.setDataSource(routingDataSource);
//		return sqlSessionFactoryBean.getObject();
//	}
}
