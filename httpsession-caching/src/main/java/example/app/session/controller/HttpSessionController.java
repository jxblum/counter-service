package example.app.session.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.geode.internal.concurrent.ConcurrentHashSet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpSessionController {

	public static final Set<String> sessionIds = new ConcurrentHashSet<>();

	public static final String INDEX_TEMPLATE_VIEW_NAME = "index";

	@GetMapping("/ping")
	public String pong() {
		return "<h1>PONG</h1>";
	}

	@GetMapping("/session")
	public Map<String, Object> sessionRequestCounts(HttpSession session) {

		this.sessionIds.add(session.getId());

		Map modelMap = new HashMap();

		modelMap.put("sessionType", session.getClass().getName());
		modelMap.put("sessionCount", this.sessionIds.size());
		modelMap.put("requestCount", getRequestCount(session));

		return modelMap;
	}

	private Object getRequestCount(HttpSession session) {

		Integer requestCount = (Integer) session.getAttribute("requestCount");

		requestCount = requestCount != null ? requestCount : 0;
		requestCount++;

		session.setAttribute("requestCount", requestCount);

		return requestCount;
	}
}
