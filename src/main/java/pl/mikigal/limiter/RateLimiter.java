package pl.mikigal.limiter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Enable rate limiting
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimiter {

	/**
	 * Maximum amount of requests in selected time
	 */
	int max();

	/**
	 * Time in selected unit
	 */
	int time();

	/**
	 * Unit of given time, seconds by default
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

	/**
	 * HTTP status code returned after exceed allowed rate
	 */
	int errorResponseCode() default 429;
}
