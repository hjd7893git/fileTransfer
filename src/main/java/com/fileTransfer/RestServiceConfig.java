package com.fileTransfer;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Jersey服务构建类
 * Created by nano on 18-4-25.
 */
@ApplicationPath("/")
public class RestServiceConfig extends ResourceConfig {
    public RestServiceConfig() {
        packages("com.fileTransfer.custom.service");
        register(MultiPartFeature.class);
        register(JacksonFeature.class);
    }
}
