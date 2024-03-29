package example.app.counters.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.app.counters.model.Counter;
import example.app.counters.service.CounterService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CounterController {

	private static final String HEADER_ONE = "<h1>%s - %s ms</h1>";

	private final CounterService counterService;

	@GetMapping("/ping")
	public String ping() {
		return String.format(HEADER_ONE, "PONG", "NA");
	}

	@GetMapping("counters/{name}")
	public String count(@PathVariable("name") String counterName) {

		long t0 = System.currentTimeMillis();
		long count = this.counterService.getCount(counterName);

		return String.format(HEADER_ONE, count, System.currentTimeMillis() - t0);
	}

	@GetMapping("counters/{name}/current")
	public String currentCount(@PathVariable("name") String counterName) {

		long t0 = System.currentTimeMillis();
		long currentCount = this.counterService.getCurrentCount(counterName);

		return String.format(HEADER_ONE, currentCount, System.currentTimeMillis() - t0);
	}

	@GetMapping("counters/{name}/reset")
	public String reset(@PathVariable("name") String counterName) {

		this.counterService.reset(counterName);

		return String.format(HEADER_ONE, "0", "NA");
	}

	@GetMapping("counters/{name}/use")
	public String use(@PathVariable("name") String counterName,
			@RequestParam(name = "count", required = false, defaultValue = "0") long count) {

		Counter counter = this.counterService.use(Counter.of(counterName).with(count));

		return String.format(HEADER_ONE, counter.getCount(), "NA");
	}
}
