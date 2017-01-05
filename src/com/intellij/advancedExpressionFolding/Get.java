package com.intellij.advancedExpressionFolding;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Get extends Expression implements GetExpression {
    private final Expression object;
    private final Expression key;

    public Get(TextRange textRange, Expression object, Expression key) {
        super(textRange);
        this.object = object;
        this.key = key;
    }

    @Override
    public String format() {
        return object.format() + "[" + key + "]";
    }

    @Override
    public boolean supportsFoldRegions(Document document, boolean quick) {
        return getTextRange() != null && object.getTextRange() != null && key.getTextRange() != null;
    }

    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement element, @NotNull Document document) {
        FoldingGroup group = FoldingGroup.newGroup(Get.class.getName());
        return new FoldingDescriptor[] {
                new FoldingDescriptor(element.getNode(),
                        TextRange.create(object.getTextRange().getEndOffset(),
                                key.getTextRange().getStartOffset()), group) {
                    @Nullable
                    @Override
                    public String getPlaceholderText() {
                        return "[";
                    }
                },
                new FoldingDescriptor(element.getNode(),
                        TextRange.create(key.getTextRange().getEndOffset(),
                                getTextRange().getEndOffset()), group) {
                    @Nullable
                    @Override
                    public String getPlaceholderText() {
                        return "]";
                    }
                }
        };
    }
}
