package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.loops;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.CodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.ComplexCodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathData;

import java.util.ArrayList;
import java.util.List;

public abstract class CycleCodePart extends ComplexCodePart {
    protected List<CodePart> insideCode = new ArrayList<CodePart>();
    protected MathData mathData = new MathData();
    protected CycleCodePart(List<CodePart> insideCode, MathData mathData)
    {
        this.insideCode = insideCode;
        this.mathData = mathData;
    }
}
