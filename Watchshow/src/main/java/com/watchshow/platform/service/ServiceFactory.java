package com.watchshow.platform.service;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.watchshow.platform.domain.BaseDomainObject;

public class ServiceFactory {
	public final static String MobileServiceContextIdentifier = "MobileServiceContext";
	public final static String PlatformServiceContextIdentifier = "PlatformServiceContext";
	public final static String StoreServiceContextIdentifier = "StoreServiceContext";
	private static Map<String, Class<? extends AbstractServiceContext>> serviceClassDictionary;

	static {
		serviceClassDictionary = new HashMap<String, Class<? extends AbstractServiceContext>>();
		serviceClassDictionary.put(MobileServiceContextIdentifier,MobileServiceContext.class);
		serviceClassDictionary.put(PlatformServiceContextIdentifier,PlatformServiceContext.class);
		serviceClassDictionary.put(StoreServiceContextIdentifier,StoreServiceContext.class);
	}

	public static AbstractServiceContext getServiceContext(BaseDomainObject user, final String contextIdentifier,
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

	public static AbstractServiceContext getServiceContext(BaseDomainObject user, final String contextIdentifier,
			String arg1, String arg2, String arg3) throws Exception {
		Class<? extends AbstractServiceContext> serviceClass = serviceClassDictionary
				.get(contextIdentifier);

		Class<?>[] params = new Class[] {BaseDomainObject.class, String.class, String.class,
				String.class };
		Constructor<? extends AbstractServiceContext> constructor = serviceClass
				.getDeclaredConstructor(params);
		AbstractServiceContext service = null;
		if (constructor != null) {
			service = constructor.newInstance(user, arg1, arg2, arg3);
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
