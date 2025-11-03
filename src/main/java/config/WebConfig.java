package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")  // ✅ REMOVE trailing slash!
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ✅ If needed, you can use filesystem-based path
        registry.addResourceHandler("/static/Romance/**")
                .addResourceLocations("classpath:/static/Romance/");
        registry.addResourceHandler("/Fiction/**")
                .addResourceLocations("classpath:/static/Fiction/");
        registry.addResourceHandler("/Thriller/**")
                .addResourceLocations("classpath:/static/Thriller/");
        registry.addResourceHandler("/Mystery/**")
                .addResourceLocations("classpath:/static/Mystery/");
    }
}











/*
package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Allows CORS for all paths
                .allowedOrigins("http://localhost:3000/")  // Your frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // HTTP methods allowed
                .allowedHeaders("*")  // All headers allowed
                .allowCredentials(true);  // Allow credentials like cookies or authentication headers
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve all static resources (images, PDFs, etc.)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
    */
/*public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/Romance/**")
                .addResourceLocations("file:src/main/resources/static/Romance/");
    }*//*

}
*/
