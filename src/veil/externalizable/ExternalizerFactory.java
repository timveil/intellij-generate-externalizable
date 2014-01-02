package veil.externalizable;

import com.intellij.psi.PsiField;

import java.util.HashMap;
import java.util.Map;

public class ExternalizerFactory {


    private static final Map<String, FieldExternalizer> map = new HashMap<String, FieldExternalizer>();

    static {
        map.put("byte", new PrimitiveExternalizer("Byte"));
        map.put("double", new PrimitiveExternalizer("Double"));
        map.put("float", new PrimitiveExternalizer("Float"));
        map.put("int", new PrimitiveExternalizer("Int"));
        map.put("long", new PrimitiveExternalizer("Long"));
        map.put("boolean", new PrimitiveExternalizer("Boolean"));

        map.put("java.lang.Byte", new ObjectExternalizer("java.lang.Byte"));
        map.put("java.lang.Double", new ObjectExternalizer("java.lang.Double"));
        map.put("java.lang.Float", new ObjectExternalizer("java.lang.Float"));
        map.put("java.lang.Integer", new ObjectExternalizer("java.lang.Integer"));
        map.put("java.lang.Long", new ObjectExternalizer("java.lang.Long"));
        map.put("java.lang.Boolean", new ObjectExternalizer("java.lang.Boolean"));
        map.put("java.lang.String", new ObjectExternalizer("java.lang.String"));
    }


    public static FieldExternalizer getExternalizer(PsiField field) {

        FieldExternalizer fieldExternalizer = map.get(field.getType().getCanonicalText());

        if (fieldExternalizer == null) {
            fieldExternalizer = new ObjectExternalizer(null);
        }

        return fieldExternalizer;
    }


}
