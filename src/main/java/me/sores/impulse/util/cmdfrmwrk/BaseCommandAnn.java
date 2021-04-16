package me.sores.impulse.util.cmdfrmwrk;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by LavaisWatery on 10/25/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface BaseCommandAnn {
    String name();

    String[] aliases();

    String permission() default "";

    String usage();

    CommandUsageBy usageBy();

    int minArgs() default 0;

    int maxArgs() default -1;
}
