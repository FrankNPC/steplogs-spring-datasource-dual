package io.steplogs.spring.datasource.dual.hikari;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.steplogs.spring.datasource.dual.DataSourceSwitcher;
import io.steplogs.spring.datasource.dual.DefaultRoutingDataSource;
import jakarta.annotation.Resource;

@Configuration
public class HikariRoutingDataSourceConfiguration {

	@Resource
	HikariConfigForReader hikariConfigForReader;
	
	@Resource
	HikariConfigForWriter hikariConfigForWriter;

	@Resource
	DataSourceSwitcher dataSourceSwitcher;
	
	@Bean("DefaultRoutingDataSource")
	public DefaultRoutingDataSource getRoutingDataSources() {
		DefaultRoutingDataSource routingDataSource = new DefaultRoutingDataSource();

		DataSource defaultDataSource = null;
		Map<Object, Object> dataSources = new HashMap<>();
		if (hikariConfigForReader.getInstanceCount()>0) {
			defaultDataSource = new HikariLazyDataSource(new HikariDataSourceResolver(hikariConfigForReader.getHikariConfig(0)));
			
			List<String> readerDataSourceKeys = dataSourceSwitcher.createReaderDataSourceKeys(hikariConfigForReader.getInstanceCount());
			for(int i=0; i<readerDataSourceKeys.size(); i++) {
				dataSources.put(readerDataSourceKeys.get(i), 
					new HikariLazyDataSource(new HikariDataSourceResolver(hikariConfigForReader.getHikariConfig(i))));
			}
		}
		
		if (hikariConfigForWriter.getInstanceCount()>0) {
			defaultDataSource = new HikariLazyDataSource(new HikariDataSourceResolver(hikariConfigForWriter.getHikariConfig(0)));

			List<String> writerDataSourceKeys = dataSourceSwitcher.createWriterDataSourceKeys(hikariConfigForWriter.getInstanceCount());
			for(int i=0; i<writerDataSourceKeys.size(); i++) {
				dataSources.put(writerDataSourceKeys.get(i), 
					new HikariLazyDataSource(new HikariDataSourceResolver(hikariConfigForWriter.getHikariConfig(i))));
			}
		}
		if (!dataSources.isEmpty()) {
			routingDataSource.setTargetDataSources(Collections.unmodifiableMap(dataSources));
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
