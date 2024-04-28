package steplogs.spring.datasource.dual;

import javax.sql.DataSource;

public interface DataSourceResolver {
	
	DataSource apply();

}
