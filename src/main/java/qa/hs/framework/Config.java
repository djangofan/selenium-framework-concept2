package qa.hs.framework;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Config 
{
    public String url();
    public Browser browser() default Browser.FIREFOX;
    public String hub() default "";
    public String osType() default "WIN8";
    public String browserVersion() default "";
    public String resolution() default "1024x768";
}
