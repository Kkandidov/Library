package org.astashonok.library.util.pool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class Pools {
    public static ConnectionPool newPool(Class<? extends ConnectionPool> pool) {
        for (Method method : pool.getMethods()){
            if(method.getName().equals("getInstance")){
                try {
                    return (ConnectionPool) method.invoke(pool);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
