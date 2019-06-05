package example.app.counters.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.app.counters.model.Counter;
import example.app.counters.service.CounterService;
import lombok.RequiredArgsConstructor;

/**
 * The CountersController class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class CountersController {

	private static final String HEADER_ONE = "<h1>%s</h1>";

	private final CounterService counterService;

	@GetMapping("/ping")
	public String ping() {
		return String.format(HEADER_ONE, "PONG");
	}

	@GetMapping("counters/{name}")
	public String count(@PathVariable("name") String counterName) {
		return String.format(HEADER_ONE, this.counterService.getCount(counterName));
	}

	@GetMapping("counters/{name}/current")
	public String currentCount(@PathVariable("name") String counterName) {
		return String.format(HEADER_ONE, this.counterService.getCurrentCount(counterName));
	}

	@GetMapping("counters/{name}/reset")
	public String reset(@PathVariable("name") String counterName) {
		this.counterService.reset(counterName);
		return String.format(HEADER_ONE, "0");
	}

	@GetMapping("counters/{name}/use")
	public String use(@PathVariable("name") String counterName,
			@RequestParam(name = "count", required = false, defaultValue = "0") long count) {

		return String.format(HEADER_ONE, this.counterService.use(Counter.of(counterName).with(count)).getCount());
	}
}
