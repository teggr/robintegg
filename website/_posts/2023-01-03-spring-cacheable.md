---
layout: post
title: "Watch out for the Spring @Cacheable default key"
date: "2023-01-03"
description: "Understanding how Spring @Cacheable annotation uses method parameters as cache keys and avoiding unexpected cache behavior."
image: /images/ben-kolde-bs2Ba7t69mM-unsplash.jpg
tags:
  - java
  - spring
---
Spring provides a useful [caching abstraction](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache) that can be applied to your Spring beans using annotations.

You can use the `@Cacheable` annotation to transparently mark methods or classes whose results are cacheable. If you are caching static data for example, this can reduce the number of calls to the database.

What you need to be aware of is that the Spring `@Cacheable` annotation uses the [method parameters as the cache key by default](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/cache/annotation/Cacheable.html). This can display surprising behaviours at runtime that you might not run into when testing. One scenario where this behaviour is unwanted would be when two different repository methods share the same signature.

```java
@Cacheable("staticdata")
public interface StaticDataRepository extends JpaRepository<StaticData,Long> {

    List<StaticData> findAllByName(String name);

    List<StaticData> findAllByLabel(String label);

}
```

In this repository implementation we have two different queries that we would expect to return different results.

With the following test we can show in the logs that the cache is being used.

```java
@DataJpaTest
@ContextConfiguration
class StaticDataRepositoryTest {

    @EnableCaching
    @TestConfiguration
    public static class StaticDataRepositoryTestConfiguration {

        @Bean
        CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("staticdata");
        }

    }

    private static Logger LOGGER = LoggerFactory.getLogger(StaticDataRepositoryTest.class);

    @Autowired
    StaticDataRepository staticData;

    @BeforeEach
    void setup() {
        staticData.save(new StaticData(1L, "one", "two"));
        staticData.save(new StaticData(2L, "two", "two"));
    }

    @Test
    void findAllByName() {

        LOGGER.info("FIRST READ");
        List<StaticData> val1 = staticData.findAllByName("two");
        LOGGER.info("SECOND READ");
        List<StaticData> val2 = staticData.findAllByName("two");
        LOGGER.info("VERIFY");
        Assertions.assertThat(val1).hasSize(1).flatMap(StaticData::getId).containsExactly(2L);
        Assertions.assertThat(val2).hasSize(1).flatMap(StaticData::getId).containsExactly(2L);

    }
}
```

Running the `findAllByName` twice in this tests we see the following output in the logs. The first read executes the SQL but the second does not because it is retrieved from the cache.

```text
2023-01-03T14:41:12.572Z  INFO 28212 --- [           main] c.r.c.c.StaticDataRepositoryTest         : FIRST READ
2023-01-03T14:41:12.643Z DEBUG 28212 --- [           main] org.hibernate.SQL                        : select s1_0.id,s1_0.label,s1_0.name from static_data s1_0 where s1_0.name=?
Hibernate: select s1_0.id,s1_0.label,s1_0.name from static_data s1_0 where s1_0.name=?
2023-01-03T14:41:12.648Z  INFO 28212 --- [           main] c.r.c.c.StaticDataRepositoryTest         : SECOND READ
2023-01-03T14:41:12.649Z  INFO 28212 --- [           main] c.r.c.c.StaticDataRepositoryTest         : VERIFY
```

To see the conflict of the default key, we will now run the same test but mix the calls to use `findAllByLabel` first which would return a different set of results.

```java
    @Test
    void findAllByName() {

        LOGGER.info("FIRST READ");
        List<StaticData> val1 = staticData.findAllByLabel("two");
        LOGGER.info("SECOND READ");
        List<StaticData> val2 = staticData.findAllByName("two");
        LOGGER.info("VERIFY");
        Assertions.assertThat(val1).hasSize(2).flatMap(StaticData::getId).containsExactly(1L,2L);
        Assertions.assertThat(val2).hasSize(1).flatMap(StaticData::getId).containsExactly(2L);

    }
```

Here we see the error where the first read has read in the expected 2 items, but the second read has failed because it is using the value cached from the previous read because they share the same arguments in this case: "two".

```text
2023-01-03T14:44:01.353Z  INFO 7528 --- [           main] c.r.c.c.StaticDataRepositoryTest         : FIRST READ
2023-01-03T14:44:01.417Z DEBUG 7528 --- [           main] org.hibernate.SQL                        : select s1_0.id,s1_0.label,s1_0.name from static_data s1_0 where s1_0.label=?
Hibernate: select s1_0.id,s1_0.label,s1_0.name from static_data s1_0 where s1_0.label=?
2023-01-03T14:44:01.420Z  INFO 7528 --- [           main] c.r.c.c.StaticDataRepositoryTest         : SECOND READ
2023-01-03T14:44:01.421Z  INFO 7528 --- [           main] c.r.c.c.StaticDataRepositoryTest         : VERIFY

java.lang.AssertionError: 
Expected size: 1 but was: 2 in:
[com.robintegg.cache.cachedemo.StaticData@1,
    com.robintegg.cache.cachedemo.StaticData@2]

```

So revisiting the [javadocs](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/cache/annotation/Cacheable.html) for the `@Cacheable` annotation we have identified that we should be wary of using the default caching settings.

```
Each time an advised method is invoked, caching behavior will be applied, checking whether the method has been already invoked for the given arguments. A sensible default simply uses the method parameters to compute the key, but a SpEL expression can be provided via the key() attribute, or a custom KeyGenerator implementation can replace the default one (see keyGenerator()).
```

So in order to alleviate this issue and reduce contention, it would be wise to use the suggested `key` argument for the annotation and use the suggested SpEL values according to the possible contention in your arguments or method names

```java
@Cacheable(cacheNames = "staticdata", key = "#root.targetClass + ' ' +  #root.methodName + ' ' + #root.args")
public interface StaticDataRepository extends JpaRepository<StaticData,Long> {

    List<StaticData> findAllByName(String name);

    List<StaticData> findAllByLabel(String label);

}
```

Now that we've used a fuller qualified key of `#root.targetClass + ' ' +  #root.methodName + ' ' + #root.args` then we see both method being executed.

```text
2023-01-03T14:51:10.705Z  INFO 16976 --- [           main] c.r.c.c.StaticDataRepositoryTest         : FIRST READ
2023-01-03T14:51:10.802Z DEBUG 16976 --- [           main] org.hibernate.SQL                        : select s1_0.id,s1_0.label,s1_0.name from static_data s1_0 where s1_0.label=?
Hibernate: select s1_0.id,s1_0.label,s1_0.name from static_data s1_0 where s1_0.label=?
2023-01-03T14:51:10.806Z  INFO 16976 --- [           main] c.r.c.c.StaticDataRepositoryTest         : SECOND READ
2023-01-03T14:51:10.810Z DEBUG 16976 --- [           main] org.hibernate.SQL                        : select s1_0.id,s1_0.label,s1_0.name from static_data s1_0 where s1_0.name=?
Hibernate: select s1_0.id,s1_0.label,s1_0.name from static_data s1_0 where s1_0.name=?
2023-01-03T14:51:10.810Z  INFO 16976 --- [           main] c.r.c.c.StaticDataRepositoryTest         : VERIFY
```
