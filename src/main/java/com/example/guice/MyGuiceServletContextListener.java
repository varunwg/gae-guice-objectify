package com.example.guice;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyFilter;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class MyGuiceServletContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule() {
					@Override
					protected void configureServlets() {

						bind(ObjectifyFactory.class).in(Singleton.class);
						bind(ObjectifyFilter.class).in(Singleton.class);		

						Map<String, String> apiParams = Maps.newHashMap();
						apiParams.put("com.sun.jersey.config.property.packages", "com.ea.pets.remoteconfig.rest");
						apiParams.put(ServletContainer.FEATURE_FILTER_FORWARD_ON_404, "true");
						apiParams.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
						apiParams.put(ServletContainer.JSP_TEMPLATES_BASE_PATH, "/WEB-INF/jsp");

						filter("/*").through(ObjectifyFilter.class);
						filter("/rest/*").through(GuiceContainer.class, apiParams);
					}
				});
	}
}
