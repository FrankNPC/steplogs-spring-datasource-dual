1: read/write separation requires database support. Or you can setup instanceCount to 0 to disable it
2: It's based on Hikari and it;s configuration.
3: Once the method is marked as @Transactional, DataSourceTransationalPointcut will setup the write datasource
4: If you want to specify an HTTP request as writer for consistency, you may need to do that in an Interceptor.