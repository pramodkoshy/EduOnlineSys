package education.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import education.util.JHipsterProperties;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Factory;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.hibernate.cache.jcache.ConfigSettings;

import javax.cache.CacheManager;
import javax.inject.Singleton;

@Factory
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Singleton
    public CacheManager cacheManager(ApplicationContext applicationContext) {
        CacheManager cacheManager = new EhcacheCachingProvider().getCacheManager(
            null, applicationContext.getClassLoader(), new Properties());
        customizeCacheManager(cacheManager);
        return cacheManager;
    }

    private void customizeCacheManager(CacheManager cm) {
        createCache(cm, education.repository.UserRepository.USERS_BY_LOGIN_CACHE);
        createCache(cm, education.repository.UserRepository.USERS_BY_EMAIL_CACHE);
        createCache(cm, education.domain.User.class.getName());
        createCache(cm, education.domain.Authority.class.getName());
        createCache(cm, education.domain.User.class.getName() + ".authorities");
        createCache(cm, education.domain.Teacher.class.getName());
        createCache(cm, education.domain.Student.class.getName());
        createCache(cm, education.domain.Student.class.getName() + ".courses");
        createCache(cm, education.domain.Course.class.getName());
        createCache(cm, education.domain.Course.class.getName() + ".courseModules");
        createCache(cm, education.domain.Course.class.getName() + ".students");
        createCache(cm, education.domain.Exam.class.getName());
        createCache(cm, education.domain.Exam.class.getName() + ".questions");
        createCache(cm, education.domain.Question.class.getName());
        createCache(cm, education.domain.Question.class.getName() + ".questionItems");
        createCache(cm, education.domain.QuestionItem.class.getName());
        createCache(cm, education.domain.AnswerItem.class.getName());
        createCache(cm, education.domain.CourseModule.class.getName());
        createCache(cm, education.domain.CourseModule.class.getName() + ".courseUnits");
        createCache(cm, education.domain.CourseUnit.class.getName());
        createCache(cm, education.domain.CourseUnit.class.getName() + ".courseLearningObjects");
        createCache(cm, education.domain.CourseLearningObjects.class.getName());
        createCache(cm, education.domain.CourseEnrolment.class.getName());
        createCache(cm, education.domain.ExamRegistration.class.getName());
        // jhipster-needle-ehcache-add-entry
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
