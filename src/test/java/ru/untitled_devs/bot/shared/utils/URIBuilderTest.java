package ru.untitled_devs.bot.shared.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class URIBuilderTest {
	@Test
	void builderGotUrlWithoutParams() {
		URIBuilder builder = new URIBuilder("https://test.com/");
		assertEquals("https://test.com/", builder.getBaseUrl());
		assertEquals(0, builder.getParams().size());

		assertEquals("https://test.com/", builder.toUri().toString());
	}

	@Test
	void builderGotUrlWithParams() {
		URIBuilder builder = new URIBuilder("https://test.com/?param1=value1&param2=value 2");

		assertEquals("https://test.com/", builder.getBaseUrl());
		assertEquals(2, builder.getParams().size());
		assertEquals("value1", builder.getParam("param1"));
		assertEquals("value 2", builder.getParam("param2"));

		assertEquals("https://test.com/?param1=value1&param2=value+2", builder.toUri().toString());
	}

	@Test
	void builderGotUrlWithInvalidParams() {
		URIBuilder builder = new URIBuilder("https://test.com/?param1=&param2=value2");

		assertEquals("https://test.com/", builder.getBaseUrl());
		assertEquals(2, builder.getParams().size());
		assertEquals("", builder.getParam("param1"));
		assertEquals("value2", builder.getParam("param2"));

		assertEquals("https://test.com/?param1=&param2=value2", builder.toUri().toString());
	}

	@Test
	void addParamGotValidParam() {
		URIBuilder builder = new URIBuilder("https://test.com/");
		builder.addParam("param", "value");

		assertEquals("value", builder.getParam("param"));
		assertEquals(1, builder.getParams().size());
		assertEquals("https://test.com/?param=value", builder.toUri().toString());
	}
}
