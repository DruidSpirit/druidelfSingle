package druidelf.config;

import druidelf.sqlcomment.CommentIntegrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;
import java.util.Collections;
import java.util.Map;

@Configuration
public class HibernateConfig implements HibernatePropertiesCustomizer {

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.use_sql_comments", true);
        hibernateProperties.put("hibernate.integrator_provider",
                (IntegratorProvider) () -> Collections.singletonList(CommentIntegrator.INSTANCE));
    }
}
