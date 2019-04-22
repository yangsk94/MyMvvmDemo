package com.widget.loading;



/**
 * Created by ysk on 2017/4/9.
 */
public enum LoadingType {

    ROTATE_CIRCLE(RotateCircleBuilder.class),TEXT(TextBuilder.class),;

    private final Class<?> mBuilderClass;

    LoadingType(Class<?> builderClass) {
        this.mBuilderClass = builderClass;
    }

    <T extends BaseLoadingBuilder> T newInstance() {
        try {
            return (T) mBuilderClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
