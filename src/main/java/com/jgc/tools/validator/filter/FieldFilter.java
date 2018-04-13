package com.jgc.tools.validator.filter;

import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

public class FieldFilter extends SimplePropertyPreFilter implements NameFilter {

    public FieldFilter(String... properties) {
        super(null, properties);
    }

    public FieldFilter(Class<?> clazz, String... properties) {
        super(clazz, properties);
    }

    @Override
    public String process(Object object, String name, Object value) {
        return name == null ? null : FieldsUtils.translate(name);
    }
}
