package io.steplogs.spring.datasource.dual.hikari;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import io.steplogs.spring.datasource.dual.DataSourceResolver;

public class HikariLazyDataSource implements DataSource {
	
	private DataSourceResolver defaultResolver;
	
	public HikariLazyDataSource(DataSourceResolver resolver) {
		this.defaultResolver = resolver;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return defaultResolver.apply().getConnection();
	}
	
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return defaultResolver.apply().getParentLogger();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return defaultResolver.apply().unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return defaultResolver.apply().isWrapperFor(iface);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return defaultResolver.apply().getConnection(username, password);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return defaultResolver.apply().getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		defaultResolver.apply().setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		defaultResolver.apply().setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return defaultResolver.apply().getLoginTimeout();
	}
}
