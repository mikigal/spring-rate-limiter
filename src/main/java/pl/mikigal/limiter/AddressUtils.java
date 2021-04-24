package pl.mikigal.limiter;

import javax.servlet.http.HttpServletRequest;

public class AddressUtils {

	private static final String[] ADDRESS_HEADERS = {
			"X-Forwarded-For",
			"Proxy-Client-IP",
			"WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR",
			"HTTP_X_FORWARDED",
			"HTTP_X_CLUSTER_CLIENT_IP",
			"HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR",
			"HTTP_FORWARDED",
			"HTTP_VIA",
			"REMOTE_ADDR"
	};

	public static String getRealAddress(HttpServletRequest request) {
		for (String header : ADDRESS_HEADERS) {
			String headerValue = request.getHeader(header);
			if (headerValue != null && headerValue.length() != 0 && !"unknown".equalsIgnoreCase(headerValue)) {
				return headerValue;
			}
		}

		return request.getRemoteAddr();
	}
}
