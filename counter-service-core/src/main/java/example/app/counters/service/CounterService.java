package example.app.counters.service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import example.app.counters.model.Counter;

@Service
public class CounterService {

	@Value("${example.app.counters.pause-enabled:false}")
	private boolean pauseEnabled;

	private final ConcurrentMap<String, Counter> counters = new ConcurrentHashMap<>();

	private final Random secondsGenerator = new Random(System.currentTimeMillis());

	@CachePut("Counters")
	public long getCount(String counterName) {

		Counter counter = this.counters.get(counterName);

		if (counter == null) {
			counter = Counter.of(counterName);
			Counter existingCounter = this.counters.putIfAbsent(counterName, counter);
			counter = existingCounter != null ? existingCounter : counter;
		}

		return pause(counter.incrementAndGet());
	}

	@Cacheable("Counters")
	public long getCurrentCount(String counterName) {
		return getCount(counterName);
	}

	@CacheEvict("Counters")
	public void reset(String counterName) {
		this.counters.remove(counterName);
	}

	public Counter use(Counter counter) {

		this.counters.put(counter.getName(), counter);

		return counter;
	}

	private <T> T pause(T obj) {

		if (this.pauseEnabled) {
			try {

				long milliseconds = TimeUnit.SECONDS
					.toMillis(2 + this.secondsGenerator.nextInt(3));

				Thread.sleep(milliseconds);
			}
			catch (InterruptedException ignore) {
				Thread.currentThread().interrupt();
			}
		}

		return obj;
	}
}
