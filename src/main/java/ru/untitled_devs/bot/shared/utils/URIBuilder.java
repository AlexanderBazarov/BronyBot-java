package ru.untitled_devs.bot.shared.utils;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class URIBuilder {
	private final String baseUrl;
	private final Map<String, String> params = new HashMap<>();

	public URIBuilder(String uri) {
		String[] parts = uri.split("\\?", 2);
		baseUrl = parts[0];
		if (parts.length >= 2 && !parts[1].isEmpty()) {
			params.putAll(parseParams(parts[1]));
		}
	}

	public URI toUri() {
		if (params.isEmpty()) {
			return URI.create(baseUrl);
		}

		String query = params.entrySet().stream()
			.map((e) -> urlEncode(e.getKey()) + "=" + urlEncode(e.getValue()))
			.collect(Collectors.joining("&"));

		String separator = baseUrl.contains("?") ? "&" : "?";
		return URI.create(baseUrl + separator + query);
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void addParam(String param, String value) {
		params.put(urlDecode(param), urlDecode(value));
	}

	public String getParam(String param) {
		return params.get(param);
	}

	public boolean hasParam(String param) {
		return params.containsKey(param);
	}

	public void clearParams() {
		params.clear();
	}

	public Map<String, String> getParams() {
		return Collections.unmodifiableMap(params);
	}

	private HashMap<String, String> parseParams(String queryString) {
		return Arrays.stream(queryString.split("&"))
			.map((pair) -> pair.split("=", 2))
			.collect(Collectors.toMap(
				(arr) -> urlDecode(arr[0]),
				(arr) -> arr.length > 1 ? urlDecode(arr[1]) : "",
				(first, second) -> second,
				HashMap::new
			));
	}

	private String urlEncode(String s) {
		return URLEncoder.encode(s, StandardCharsets.UTF_8);
	}
	
	private String urlDecode(String s) {
		 return URLDecoder.decode(s, StandardCharsets.UTF_8);
	}

}
