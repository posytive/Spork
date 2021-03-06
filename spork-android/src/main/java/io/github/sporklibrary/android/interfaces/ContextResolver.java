package io.github.sporklibrary.android.interfaces;

import android.content.Context;

import io.github.sporklibrary.annotations.Nullable;

/**
 * Resolves a Context for a given object or returns null when it cannot be resolved.
 */
public interface ContextResolver {
	@Nullable Context resolveContext(Object object);
}
