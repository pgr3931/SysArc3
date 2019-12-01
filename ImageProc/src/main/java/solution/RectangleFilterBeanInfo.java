package solution;

import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;

public class RectangleFilterBeanInfo extends SimpleBeanInfo {
    private static final Class beanClass = RectangleFilter.class;

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor prop1 = new PropertyDescriptor("showRectangle", beanClass);
            PropertyDescriptor prop2 = new PropertyDescriptor("x", beanClass);
            PropertyDescriptor prop3 = new PropertyDescriptor("y", beanClass);
            PropertyDescriptor prop4 = new PropertyDescriptor("width", beanClass);
            PropertyDescriptor prop5 = new PropertyDescriptor("height", beanClass);

            prop1.setBound(true);
            prop2.setBound(true);
            prop3.setBound(true);
            prop4.setBound(true);
            prop5.setBound(true);

            return new PropertyDescriptor[]{prop1, prop2, prop3, prop4, prop5};
        } catch (IntrospectionException ex) {
            throw new Error(ex.toString());
        }
    }

    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        try {
            Method method = beanClass.getMethod("doProcess");
            MethodDescriptor methodDescriptor = new MethodDescriptor(method);
            return new MethodDescriptor[]{methodDescriptor};
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
}
