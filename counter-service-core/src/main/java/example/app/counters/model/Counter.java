package example.app.counters.model;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Region("Counters")
@ToString(of = "name")
@RequiredArgsConstructor(staticName = "of")
public class Counter {

	@Id @Getter
	private final String name;

	private final AtomicLong count = new AtomicLong(0L);

	public long getCount() {
		return this.count.get();
	}

	public long incrementAndGet() {
		return this.count.incrementAndGet();
	}

	public Counter with(long count) {
		this.count.set(Math.max(count, 0L));
		return this;
	}
}
