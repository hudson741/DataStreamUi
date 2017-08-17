package com.yss.spark.log;

/**
 * Created by xingjun.Li on 2017/8/17.
 */
import java.lang.annotation.*;


@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BussinessLog {

    String value() default "";

}
