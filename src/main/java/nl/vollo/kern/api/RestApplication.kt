package nl.vollo.kern.api;

import io.swagger.jaxrs.config.BeanConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
class RestApplication() : Application() {
    init {
        val beanConfig = BeanConfig()
        beanConfig.version = "1.0.0"
        beanConfig.schemes = arrayOf("http")
        beanConfig.host = "localhost:8080"
        beanConfig.basePath = "/vollo-kern/api"
        beanConfig.resourcePackage = "io.swagger.resources,nl.vollo.kern.api"
        beanConfig.scan = true
        beanConfig.title = "Vollo-kern"
        beanConfig.prettyPrint = true
    }
}
