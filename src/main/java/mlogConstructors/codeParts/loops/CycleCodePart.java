package mlogConstructors.codeParts.loops;

import mlogConstructors.codeParts.CodePart;
import mlogConstructors.codeParts.ComplexCodePart;
import mlogConstructors.mathEngine.MathData;

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
