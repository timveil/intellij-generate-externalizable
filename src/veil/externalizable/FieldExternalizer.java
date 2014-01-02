package veil.externalizable;

import com.intellij.psi.PsiField;

public interface FieldExternalizer {

    String writeField(PsiField field, String objectOutputName);

    String readField(PsiField field, String objectInputName);
}
