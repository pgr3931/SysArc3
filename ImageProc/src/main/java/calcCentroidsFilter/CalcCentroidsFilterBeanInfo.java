package calcCentroidsFilter;


import java.beans.MethodDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;

public class CalcCentroidsFilterBeanInfo extends SimpleBeanInfo {
    private static final Class beanClass = CalcCentroidsFilter.class;

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
