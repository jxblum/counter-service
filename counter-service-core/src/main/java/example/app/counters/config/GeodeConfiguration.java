package example.app.counters.config;

import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.util.CacheWriterAdapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.RegionConfigurer;
import org.springframework.geode.cache.support.CacheLoaderSupport;

import example.app.counters.model.Counter;
import example.app.counters.service.CounterService;

@Configuration
public class GeodeConfiguration {

	@Bean
	RegionConfigurer cacheLoaderServiceAdapter(CounterService counterService) {

		return new RegionConfigurer() {

			@Override
			@SuppressWarnings("unchecked")
			public void configure(String beanName, ClientRegionFactoryBean<?, ?> bean) {

				if ("Counters".equals(beanName)) {
					bean.setCacheLoader((CacheLoaderSupport) helper -> {

						String counterName = String.valueOf(helper.getKey());

						return Counter.of(counterName).with(counterService.getCount(counterName));
					});
				}
			}
		};
	}

	@Bean
	RegionConfigurer cacheWriterServiceAdapter(CounterService counterService) {

		return new RegionConfigurer() {

			@Override
			@SuppressWarnings("unchecked")
			public void configure(String beanName, ClientRegionFactoryBean<?, ?> bean) {

				bean.setCacheWriter(new CacheWriterAdapter() {

					@Override
					public void beforeCreate(EntryEvent event) throws CacheWriterException {
						useCounter((Counter) event.getNewValue());
					}

					@Override
					public void beforeDestroy(EntryEvent event) throws CacheWriterException {
						counterService.reset(event.getKey().toString());
					}

					@Override
					public void beforeUpdate(EntryEvent event) throws CacheWriterException {
						useCounter((Counter) event.getNewValue());
					}

					private void useCounter(Counter counter) {
						counterService.use(counter);
					}
				});
			}
		};
	}
}
