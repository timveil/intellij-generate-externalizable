package veil.externalizable;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;

public class GenerateExternalizableAction extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        generateExternalizable(getPsiClassFromContext(e));
    }

    private PsiClass getPsiClassFromContext(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return null;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAt = psiFile.findElementAt(offset);
        return PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
    }

    private void generateExternalizable(final PsiClass psiClass) {
        new WriteCommandAction.Simple(psiClass.getProject(), psiClass.getContainingFile()) {

            @Override
            protected void run() throws Throwable {
                PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
                final JavaCodeStyleManager javaCodeStyleManager = JavaCodeStyleManager.getInstance(psiClass.getProject());

                final String readExternalAsString = generateReadExternal(psiClass);
                PsiMethod readExternalMethod = elementFactory.createMethodFromText(readExternalAsString, psiClass);
                javaCodeStyleManager.shortenClassReferences(psiClass.add(readExternalMethod));

                final String writeExternalAsString = generateWriteExternal(psiClass);
                PsiMethod writeExternalMethod = elementFactory.createMethodFromText(writeExternalAsString, psiClass);
                javaCodeStyleManager.shortenClassReferences(psiClass.add(writeExternalMethod));
            }

        }.execute();
    }

    private String generateReadExternal(PsiClass psiClass) {
        StringBuilder builder = new StringBuilder("@Override\npublic void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {\n");

        for (PsiField field : psiClass.getFields()) {
            builder.append(field.getName());
            builder.append(" = ");
            builder.append(ExternalizerFactory.getExternalizer(field).readField(field, "in"));
            builder.append(";\n");
        }

        builder.append("}");
        return builder.toString();
    }

    private String generateWriteExternal(PsiClass psiClass) {
        StringBuilder builder = new StringBuilder("@Override\npublic void writeExternal(ObjectOutput out) throws IOException {\n");

        for (PsiField field : psiClass.getFields()) {
            builder.append(ExternalizerFactory.getExternalizer(field).writeField(field, "out"));
            builder.append(";\n");
        }

        builder.append("}");
        return builder.toString();
    }


}
