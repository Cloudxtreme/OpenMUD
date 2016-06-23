package pl.com.clockworkgnome.openmud.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static Object runGetter(String fieldName, Object o)
    {
        // MZ: Find the correct method
        for (Method method : o.getClass().getMethods())
        {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (fieldName.length() + 3)))
            {
                if (method.getName().toLowerCase().endsWith(fieldName.toLowerCase()))
                {
                    // MZ: Method found, run it
                    try
                    {
                        return method.invoke(o);
                    }
                    catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
//                        Logger.fatal("Could not determine method: " + method.getName());
                    }
                    catch (InvocationTargetException e)
                    {
                        e.printStackTrace();
//                        Logger.fatal("Could not determine method: " + method.getName());
                    }

                }
            }
        }


        return null;
    }
}
