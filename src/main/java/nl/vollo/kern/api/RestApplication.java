package nl.vollo.kern.api;

import io.swagger.jaxrs.config.BeanConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestApplication extends Application {
    public RestApplication() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/vollo-kern/api");
        beanConfig.setResourcePackage("io.swagger.resources,nl.vollo.kern.api");
        beanConfig.setScan(true);
        beanConfig.setTitle("Vollo-kern");
        beanConfig.setPrettyPrint(true);
    }
}