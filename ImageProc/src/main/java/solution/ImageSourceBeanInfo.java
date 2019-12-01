package solution;

import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;

public class ImageSourceBeanInfo extends SimpleBeanInfo {
    private static final Class beanClass = ImageSource.class;

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor prop1 = new PropertyDescriptor("filePath", beanClass);

            prop1.setBound(true);

            return new PropertyDescriptor[]{prop1};
        } catch (IntrospectionException ex) {
            throw new Error(ex.toString());
        }
    }

}
