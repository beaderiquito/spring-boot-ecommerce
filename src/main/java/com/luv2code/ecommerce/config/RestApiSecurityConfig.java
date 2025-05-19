package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.Product;
import com.luv2code.ecommerce.entity.ProductCategory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestApiSecurityConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public RestApiSecurityConfig(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    private static final HttpMethod[] UNSUPPORTED_HTTP_METHODS = {
            HttpMethod.POST,
            HttpMethod.PUT,
            HttpMethod.DELETE
    };

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        disableHttpMethodsForDomainType(config, Product.class);
        disableHttpMethodsForDomainType(config, ProductCategory.class);

        // Expose the ids of the entities
        config.exposeIdsFor(Product.class);
        config.exposeIdsFor(ProductCategory.class);
    }

    private void disableHttpMethodsForDomainType(RepositoryRestConfiguration config, Class<?> domainType) {
        config.getExposureConfiguration()
                .forDomainType(domainType)
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(UNSUPPORTED_HTTP_METHODS))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(UNSUPPORTED_HTTP_METHODS));
    }
}