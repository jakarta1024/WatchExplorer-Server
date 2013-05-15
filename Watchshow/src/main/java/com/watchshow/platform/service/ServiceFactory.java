package com.watchshow.platform.service;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ServiceFactory {
	public final static String MOBILE_SERVICE_CONTEXT = "MobileServiceContext";
	public final static String PLATFORM_SERVICE_CONTEXT = "PlatformServiceContext";
	public final static String STORE_SERVICE_CONTEXT = "StoreServiceContext";
	private static Map<String, Class<? extends AbstractServiceContext>> serviceClassDictionary;

	static {
		serviceClassDictionary = new HashMap<String, Class<? extends AbstractServiceContext>>();
		serviceClassDictionary.put(MOBILE_SERVICE_CONTEXT,MobileServiceContext.class);
		serviceClassDictionary.put(PLATFORM_SERVICE_CONTEXT,PlatformServiceContext.class);
		serviceClassDictionary.put(STORE_SERVICE_CONTEXT,StoreServiceContext.class);
	}

	public static AbstractServiceContext getServiceContext(Integer contextIdentifier,
			Object... initargs) throws Exception {
		Class<? extends AbstractServiceContext> serviceClass = serviceClassDictionary
				.get(contextIdentifier);

		Class<?>[] params = new Class[] { String.class, String.class, String.class };
		Constructor<? extends AbstractServiceContext> constructor = serviceClass
				.getDeclaredConstructor(params);
		AbstractServiceContext service = null;
		if (constructor != null) {
			service = constructor.newInstance(initargs);
		} else {
			constructor = serviceClass.getDeclaredConstructor();
			if (constructor == null) {
				throw new Exception(
						"Can not find constructors for specified Service Context ["
								+ serviceClass.getSimpleName() + "].");
			}
			service = constructor.newInstance();
		}
		return service;
	}

	public static AbstractServiceContext getServiceContext(Integer contextIdentifier,
			String arg1, String arg2, String arg3) throws Exception {
		Class<? extends AbstractServiceContext> serviceClass = serviceClassDictionary
				.get(contextIdentifier);

		Class<?>[] params = new Class[] { String.class, String.class,
				String.class };
		Constructor<? extends AbstractServiceContext> constructor = serviceClass
				.getDeclaredConstructor(params);
		AbstractServiceContext service = null;
		if (constructor != null) {
			service = constructor.newInstance(arg1, arg2, arg3);
		} else {
			constructor = serviceClass.getDeclaredConstructor();
			if (constructor == null) {
				throw new Exception(
						"Can not find constructors for specified Service Context ["
								+ serviceClass.getSimpleName() + "].");
			}
			service = constructor.newInstance();
		}
		return service;
	}
}
