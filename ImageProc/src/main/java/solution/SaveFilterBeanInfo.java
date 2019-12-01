package solution;

import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;

public class SaveFilterBeanInfo extends SimpleBeanInfo {
    private static final Class beanClass = SaveFilter.class;

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor prop1 = new PropertyDescriptor("outputPath", beanClass);

            prop1.setBound(true);

            return new PropertyDescriptor[]{prop1};
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
