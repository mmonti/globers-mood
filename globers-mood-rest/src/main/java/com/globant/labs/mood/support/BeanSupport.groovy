package com.globant.labs.mood.support

/**
 * Created by mmonti on 8/22/14.
 */
class BeanSupport {

    /**
     * Returns a view of the bean provided as parameter that only includes the properties specified.
     * @param bean
     * @param properties
     * @return
     */
    def static extract(final Object bean, final String ... properties) {
        def map = [:]
        for (String property  : properties) {
            map[property] = bean[property];
        }
        return map
    }

}
