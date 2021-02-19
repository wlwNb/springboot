package wlw.zc.demo.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import wlw.zc.demo.system.entity.Task;

@Component
public class MyFactoryBean implements FactoryBean<Task> {
    @Override
    public Task getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return Task.class;
    }
}
