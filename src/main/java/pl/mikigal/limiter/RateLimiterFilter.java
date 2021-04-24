package pl.mikigal.limiter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Order(0)
public class RateLimiterFilter extends HttpFilter {

	private final static Map<String, AtomicInteger> requests = new HashMap<>();
	private final static Map<String, Long> resetCounters = new HashMap<>();

	private final RequestMappingHandlerMapping handlerMapping;

	public RateLimiterFilter(RequestMappingHandlerMapping handlerMapping) {
		this.handlerMapping = handlerMapping;
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
		try {
			HandlerMethod handlerMethod = (HandlerMethod) this.handlerMapping.getHandler(request).getHandler();
			RateLimiter rateLimiter = handlerMethod.getMethodAnnotation(RateLimiter.class);
			if (rateLimiter == null) {
				super.doFilter(request, response, chain);
				return;
			}

			String address = AddressUtils.getRealAddress(request);
			long reset = resetCounters.getOrDefault(address, Long.MIN_VALUE);
			if (System.currentTimeMillis() > reset) {
				requests.put(address, new AtomicInteger());

				long millis = TimeUnit.MILLISECONDS.convert(rateLimiter.time(), rateLimiter.timeUnit());
				resetCounters.put(address, System.currentTimeMillis() + millis);
			}

			AtomicInteger counter = requests.getOrDefault(address, new AtomicInteger());
			if (counter.incrementAndGet() > rateLimiter.max()) {
				response.sendError(rateLimiter.errorResponseCode());
				return;
			}

			requests.put(address, counter);

			super.doFilter(request, response, chain);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
