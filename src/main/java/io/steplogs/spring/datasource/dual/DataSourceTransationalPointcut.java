package io.steplogs.spring.datasource.dual;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractGenericPointcutAdvisor;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

public class DataSourceTransationalPointcut extends AbstractGenericPointcutAdvisor {
	
	private static final long serialVersionUID = -4690870229176239159L;
	
	public DataSourceTransationalPointcut(){
		this.setOrder(HIGHEST_PRECEDENCE);
	}

	@Resource
	DataSourceSwitcher dataSourceSwitcher;
	
	private Pointcut pointcut = new DynamicMethodMatcherPointcut() {
		@Override
		public boolean matches(Method method, Class<?> targetClass, Object... args) {
			return method.isAnnotationPresent(Transactional.class) 
					|| targetClass.isAnnotationPresent(Transactional.class);
		}
		@Override
		public boolean matches(Method method, Class<?> targetClass) {
			return method.isAnnotationPresent(Transactional.class) 
					|| targetClass.isAnnotationPresent(Transactional.class);
		}
	};
	
	@Override
	public Pointcut getPointcut() {
		return pointcut;
	}

	private Advice beforeAdvice = new MethodBeforeAdvice(){
		@Override
		public void before(Method method, Object[] args, Object target) throws Throwable {
			dataSourceSwitcher.toWriterDataSource();
		}};
	@Override
	public Advice getAdvice() {
		return beforeAdvice;
	}
}
