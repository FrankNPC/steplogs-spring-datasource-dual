package io.steplogs.spring.datasource.dual.hikari;


import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import io.steplogs.spring.datasource.dual.DataSourceSwitcher;
import io.steplogs.spring.datasource.dual.DataSourceTransationalPointcut;
import io.steplogs.spring.datasource.dual.RandomDataSourceSwitcher;

@Import({HikariRoutingDataSourceConfiguration.class, HikariConfigForWriter.class, HikariConfigForReader.class})
public class HikariRoutingDataSourceAutoConfiguration {
	
	@Bean("HikariRoutingDataSourceProxyCreator")
//	@ConditionalOnMissingBean(name="HikariRoutingDataSourceProxyCreator")
	DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator() {
			private static final long serialVersionUID = -6678903539018663337L;
			@Override
			protected boolean isEligibleAdvisorBean(String beanName) {
				return beanName.equals("DataSourceTransationalPointcut");
			}
		};
		creator.setProxyTargetClass(true);
		return creator;
	}
	
	@Bean("DataSourceTransationalPointcut")
	@ConditionalOnMissingBean
	DataSourceTransationalPointcut createDataSourceTransationalPointcut() {
		return new DataSourceTransationalPointcut();
	}
	
	@Bean("DataSourceSwitcher")
	@ConditionalOnMissingBean
	DataSourceSwitcher getDataSourceSwitcher() {
		return new RandomDataSourceSwitcher();
	}
}
