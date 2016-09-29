package io.github.sporklibrary;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.github.sporklibrary.annotations.Nullable;
import io.github.sporklibrary.binders.FieldBinder;
import io.github.sporklibrary.binders.MethodBinder;
import io.github.sporklibrary.binders.TypeBinder;
import io.github.sporklibrary.internal.BinderImpl;
import io.github.sporklibrary.internal.BinderManager;
import io.github.sporklibrary.internal.BinderManagerImpl;
import io.github.sporklibrary.internal.caching.BinderCache;
import io.github.sporklibrary.internal.caching.BinderCacheImpl;
import io.github.sporklibrary.internal.inject.InjectFieldBinder;

/**
 * Main class to access Spork functionality.
 */
public class Spork {
	private final BinderRegistry binderRegistry;
	private final Binder binder;

	private static @Nullable Spork sharedInstance;

	public Spork() {
		// create all instances
		BinderManager binderManager = new BinderManagerImpl();
		final BinderCache binderCache = new BinderCacheImpl(binderManager);

		binderRegistry = binderManager;
		binder = new BinderImpl(binderCache);

		// ensure the cache is updated when a new type is registered
		binderManager.addRegistrationListener(new BinderManager.RegistrationListener() {
			@Override
			public void onRegisterFieldBinder(FieldBinder<?> fieldBinder) {
				binderCache.cache(fieldBinder);
			}

			@Override
			public void onRegisterMethodBinder(MethodBinder<?> methodBinder) {
				binderCache.cache(methodBinder);
			}

			@Override
			public void onRegisterTypeBinder(TypeBinder<?> typeBinder) {
				binderCache.cache(typeBinder);
			}
		});

		// registration must happen after cache is created and listening for registrations
		binderManager.register(new InjectFieldBinder());

		// Try auto-binding Spork for Android through reflection
		try {
			Class<?> sporkAndroidClass = Class.forName("io.github.sporklibrary.android.SporkAndroid");
			Method initializeMethod = sporkAndroidClass.getDeclaredMethod("initialize", BinderRegistry.class);
			initializeMethod.invoke(null, binderRegistry);
		} catch (ClassNotFoundException e) {
			// no-op
		} catch (NoSuchMethodException e) {
			System.out.println("Spork: Spork for Android found, but initialize method is not present");
		} catch (InvocationTargetException e) {
			System.out.println("Spork: Spork for Android found, but initialization failed because of InvocationTargetException: " + e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println("Spork: Spork for Android found, but initialization failed because of IllegalAccessException: " + e.getMessage());
		}
	}

	/**
	 * @return the interface for binding
	 */
	public Binder getBinder() {
		return binder;
	}

	public BinderRegistry getBinderRegistry() {
		return binderRegistry;
	}

	// region static methods

	/**
	 * @return a shared instance of Spork (creates one if one hasn't been created yet)
	 */
	public static Spork sharedInstance() {
		if (sharedInstance == null) {
			sharedInstance = new Spork();
		}

		return sharedInstance;
	}

	/**
	 * A shortcut to Spork.sharedInstance().getBinder().bind(object, modules)
	 * @param object the object to bind
	 * @param modules an optional array of non-null module instances
	 */
	public static void bind(Object object, @Nullable Object... modules) {
		sharedInstance().getBinder().bind(object, modules);
	}

	// endregion
}
