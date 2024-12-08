package com.jainyJobPortal.Config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration          //This configuration class will map requests for /photos to serve 
                         //files from a directory on our file system
public class MvcConfig implements WebMvcConfigurer {
              
  private static final String  UPLOAD_DIR = "photos";

@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
	
	exposeDirectory(UPLOAD_DIR,registry);

}

private void exposeDirectory(String uploadDir, ResourceHandlerRegistry registry) {
	// TODO Auto-generated method stub
	Path path = Paths.get(uploadDir); //Converts the uploadDir string to a path Maps requests starting with "/photos" to a file system location
	                                //file:<absolute path to photos directory> ** will match all sub directories
	registry.addResourceHandler("/" + uploadDir + "/**").addResourceLocations("file:"+path.toAbsolutePath()+"/");
}
  
}
