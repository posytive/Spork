package io.github.sporklibrary.android.annotations;

import io.github.sporklibrary.android.utils.ResourceId;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bind a View instance.
 * Can be used with classes derived from:
 * <ul>
 * <li>{@link android.view.View}</li>
 * <li>{@link android.app.Activity}</li>
 * <li>{@link android.app.Fragment}</li>
 * <li>{@link android.support.v4.app.Fragment}</li>
 * <li>{@link android.support.v7.widget.RecyclerView.ViewHolder}</li>
 * </ul>
 * The value specified is the View id. When not specified, the name of the field will be used to resolve the id's name.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindView {
    /**
     * The value specified is the View id. When not specified, the name of the field will be used to resolve the id's name.
     * For example: "@BindView View my_view;" would bind to R.id.my_view
     *
     * @return View resource id as defined in R.id.*
     */
    int value() default ResourceId.sDefaultValue;
}
