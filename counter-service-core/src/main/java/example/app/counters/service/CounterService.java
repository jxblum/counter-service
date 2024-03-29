package example.app.counters.service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import example.app.counters.model.Counter;

@Service
public class CounterService {

	@Value("${example.app.counters.pause-enabled:false}")
	private boolean pauseEnabled;

	private final ConcurrentMap<String, Counter> counters = new ConcurrentHashMap<>();

	private final Random secondsGenerator = new Random(System.currentTimeMillis());

	public long getCount(String counterName) {

		Counter counter = this.counters.get(counterName);

		if (counter == null) {
			counter = Counter.of(counterName);
			Counter existingCounter = this.counters.putIfAbsent(counterName, counter);
			counter = existingCounter != null ? existingCounter : counter;
		}

		return pause(counter.incrementAndGet());
	}

	public long getCurrentCount(String counterName) {

		return Optional.ofNullable(this.counters.get(counterName))
			.map(this::pause)
			.map(Counter::getCount)
			.orElseGet(() -> this.getCount(counterName));
	}

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
