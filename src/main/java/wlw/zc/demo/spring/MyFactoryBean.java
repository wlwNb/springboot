package wlw.zc.demo.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import wlw.zc.demo.system.entity.Task;

@Component
public class MyFactoryBean implements FactoryBean<Task>, InitializingBean,ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Override
    public Task getObject() throws Exception {
        System.out.println("=======================getObject==================");
        return new Task();
    }

    @Override
    public Class<?> getObjectType() {
        System.out.println("=======================getObjectType==================");
        return Task.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        applicationContext.getBean(Task.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
