package veil.externalizable;

import com.intellij.psi.PsiField;

public class PrimitiveExternalizer implements FieldExternalizer {

    private String typeName;

    public PrimitiveExternalizer(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String writeField(PsiField field, String objectOutputName) {
        return objectOutputName + ".write" + typeName + "(" + field.getName() + ")";
    }

    @Override
    public String readField(PsiField field, String objectInputName) {
        return objectInputName + ".read" + typeName + "()";
    }
}
