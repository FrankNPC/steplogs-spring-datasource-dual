package io.steplogs.spring.datasource.dual.hikari;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import com.zaxxer.hikari.HikariConfig;

import io.steplogs.spring.datasource.dual.DataSourceConfiguration;
import jakarta.annotation.Resource;

public class HikariConfigForWriter implements DataSourceConfiguration<HikariConfig> {

	@Value("${spring.datasource.writer.instanceCount:#{1}}")
	private int writerInstanceCount;
	
	@Resource(name="WriterHikariConfig")
	@Lazy
	HikariConfig hikariConfig;

	@Bean("WriterHikariConfig")
	@ConfigurationProperties("spring.datasource.writer")
	HikariConfig getWriterHikariConfig() {
		return new HikariConfig();
	}

	@Override
	public HikariConfig getHikariConfig(int i) {
		HikariConfig newHikariConfig = new HikariConfig();
		hikariConfig.copyStateTo(newHikariConfig);
		newHikariConfig.setJdbcUrl(hikariConfig.getJdbcUrl().replace("{}", String.valueOf(i)));
		return newHikariConfig;
	}

	@Override
	public int getInstanceCount() {
		return writerInstanceCount;
	}

}