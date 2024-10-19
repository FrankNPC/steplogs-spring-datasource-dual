package steplogs.spring.datasource.dual.hikari;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import com.zaxxer.hikari.HikariConfig;

import jakarta.annotation.Resource;
import steplogs.spring.datasource.dual.DataSourceConfiguration;

public class HikariConfigForReader implements DataSourceConfiguration<HikariConfig> {

	@Value("${spring.datasource.reader.instanceCount:#{1}}")
	private int readerInstanceCount;
	
	@Resource(name="ReaderHikariConfig")
	@Lazy
	HikariConfig hikariConfig;

	@Bean("ReaderHikariConfig")
	@ConfigurationProperties("spring.datasource.reader")
	HikariConfig getReaderHikariConfig() {
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
		return readerInstanceCount;
	}

}
