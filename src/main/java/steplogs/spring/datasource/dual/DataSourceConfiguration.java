package steplogs.spring.datasource.dual;

public interface DataSourceConfiguration<T> {
	
	public T getHikariConfig(int i);
	
	public int getInstanceCount();
	
}
