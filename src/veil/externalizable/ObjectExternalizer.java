package veil.externalizable;

import com.intellij.psi.PsiField;

public class ObjectExternalizer implements FieldExternalizer {

    private String typeName;

    public ObjectExternalizer(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String writeField(PsiField field, String objectOutputName) {
        return objectOutputName + ".writeObject(" + field.getName() + ")";
    }

    @Override
    public String readField(PsiField field, String objectInputName) {
        StringBuilder builder = new StringBuilder();

        if (typeName != null) {
            builder.append("(");
            builder.append(typeName);
            builder.append(")");
        }   else {
            builder.append("(");
            builder.append(field.getType().getCanonicalText());
            builder.append(")");
        }

        builder.append(" ");
        builder.append(objectInputName);
        builder.append(".readObject()");

        return builder.toString();
    }
}
