package com.github.dmitriiveng.mindScriptCompiler.compilers.mindScriptCode;

import com.github.dmitriiveng.mindScriptCompiler.compilers.methods.MethodData;
import com.github.dmitriiveng.mindScriptCompiler.compilers.methods.MethodsReader;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.CodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.blockControls.*;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.inputOutput.Draw;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.inputOutput.Print;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.inputOutput.Read;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.inputOutput.Write;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.loops.ComplexForCycle;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.loops.IfCycle;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.loops.WhileCycle;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.methods.Method;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.methods.ReturnValueFromMethod;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.Lookup;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.PackColor;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.otherLogics.Stop;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.otherLogics.Wait;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.unitControls.UBind;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.unitControls.UControl;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.unitControls.ULocate;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.unitControls.URadar;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.blockControls.*;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathematicalExpressionReader;

import java.io.IOException;
import java.util.*;

public interface MindScriptCompiler {
    public static UncompiledCode convertCodeIntoUncompiledCode(String code) throws IOException {
        ArrayList<CodePart> codeParts = new ArrayList<>();
        ArrayList<Method> methods =  new ArrayList<>();

        List<LineOfCode> lines = splitCodeIntoLines(code);
        //проверка правильности отступов
        int spacing = 0;//отступ определяется по первому найденному отступу
        for (int i = 0; i < lines.size(); i++){
            LineOfCode line = lines.get(i);
            if(i > 0) {
                if(spacing == 0 && line.spacing > 0){
                    spacing = line.spacing;
                }
                if(spacing > 0){
                    LineOfCode previousLine = lines.get(i - 1);
                    int spaceDelta = line.spacing - previousLine.spacing;

                    //exceptions
                    if (spaceDelta > 0 && !previousLine.hasInsideCode){
                        throw new IOException("error, line: " + previousLine.value + " does not require inside code");
                    }
                    if (line.spacing >= spacing && line.type == LineOfCode.lineType.method){
                        throw new IOException("error, method: " + line.value + " cannot be declared inside loop, construction or other method");
                    }
                    if (spaceDelta > 0 && spacing != spaceDelta){
                        throw new IOException("error, any new spacing should contain " + spacing + " spaces, found " + spaceDelta);
                    }
                    if (spaceDelta < 0 && spaceDelta % spacing != 0){
                        throw new IOException("error, spaceDelta < 0 && spaceDelta % spacing != 0");
                    }
                }
            }
            else if (line.spacing > 0){
                throw new IOException("first line cannot have spacing");
            }
        }
        //перевод в CodePart
        int i = 0;
        while(i < lines.size()){
            LineOfCode line = lines.get(i);
            switch (line.type){
                case operation -> {
                    codeParts.add(MathematicalExpressionReader.readExpression(line.value, MindScriptData.mathData));
                    i++;
                }
                case builtInMethodCall -> {
                    codeParts.add(getCallBuiltInMethodCodePart(line));
                    i++;
                }
                case method -> {
                    List<LineOfCode> insideCode = new ArrayList<>();
                    i++;
                    while (i < lines.size() && lines.get(i).spacing > line.spacing){
                        insideCode.add(lines.get(i));
                        i++;
                    }
                    methods.add(getMethodCodePart(line, insideCode));
                }
                case cycleOrConstruction -> {
                    List<LineOfCode> insideCode = new ArrayList<>();
                    i++;
                    while (i < lines.size() && lines.get(i).spacing > line.spacing){
                        insideCode.add(lines.get(i));
                        i++;
                    }
                    codeParts.add(getCycleOrConstructionCodePart(line, insideCode));
                }
            }
        }
        return new UncompiledCode(methods, codeParts, MindScriptData.mathData);
    }
    private static CodePart getCallBuiltInMethodCodePart(LineOfCode methodLine) throws IOException {
        MethodData methodData = MethodsReader.getMethodDataFromString(methodLine.value);
        switch (MindScriptData.builtInMethodsMap.get(methodLine.firstWord)){
            //input output
            case read -> {
                if(methodData.args.size() != 3){
                    throw new IOException("error, method " + methodData.name + " gets 3 args, provided " + methodData.args.size());
                }
                return new Read(methodData.args.get(0), methodData.args.get(1), methodData.args.get(2), MindScriptData.mathData);
            }
            case write -> {
                if(methodData.args.size() != 3){
                    throw new IOException("error, method " + methodData.name + " gets 3 args, provided " + methodData.args.size());
                }
                return new Write(methodData.args.get(0), methodData.args.get(1), methodData.args.get(2), MindScriptData.mathData);
            }
            case draw -> {
                if(methodData.args.size() < 2 || methodData.args.size() > 7 || methodData.args.size() == 3){
                    throw new IOException("error, method " + methodData.name + " gets 2, 4, 5, 6, 7 args, provided " + methodData.args.size());
                }
                switch (methodData.args.size()){
                    case 2 -> {
                        return new Draw(
                                MindScriptData.getDrawType(methodData.args.get(0)),
                                methodData.args.get(1),
                                MindScriptData.mathData
                        );
                    }
                    case 4 -> {
                        return new Draw(
                                MindScriptData.getDrawType(methodData.args.get(0)),
                                methodData.args.get(1),
                                methodData.args.get(2),
                                methodData.args.get(3),
                                MindScriptData.mathData
                        );
                    }
                    case 5 -> {
                        return new Draw(
                                MindScriptData.getDrawType(methodData.args.get(0)),
                                methodData.args.get(1),
                                methodData.args.get(2),
                                methodData.args.get(3),
                                methodData.args.get(4),
                                MindScriptData.mathData
                        );
                    }
                    case 6 -> {
                        return new Draw(
                                MindScriptData.getDrawType(methodData.args.get(0)),
                                methodData.args.get(1),
                                methodData.args.get(2),
                                methodData.args.get(3),
                                methodData.args.get(4),
                                methodData.args.get(5),
                                MindScriptData.mathData
                        );
                    }
                    case 7 -> {
                        return new Draw(
                                MindScriptData.getDrawType(methodData.args.get(0)),
                                methodData.args.get(1),
                                methodData.args.get(2),
                                methodData.args.get(3),
                                methodData.args.get(4),
                                methodData.args.get(5),
                                methodData.args.get(6),
                                MindScriptData.mathData
                        );
                    }
                }
            }
            case print -> {
                if(methodData.args.size() != 1){
                    throw new IOException("error, method " + methodData.name + " gets 1 args, provided " + methodData.args.size());
                }
                return new Print(methodData.args.get(0), MindScriptData.mathData);
            }
            //block control
            case drawflush -> {
                if(methodData.args.size() != 1){
                    throw new IOException("error, method " + methodData.name + " gets 1 args, provided " + methodData.args.size());
                }
                return new DrawFlush(methodData.args.get(0));
            }
            case printflush -> {
                if(methodData.args.size() != 1){
                    throw new IOException("error, method " + methodData.name + " gets 1 args, provided " + methodData.args.size());
                }
                return new PrintFlush(methodData.args.get(0));
            }
            case getlink -> {
                if(methodData.args.size() != 2){
                    throw new IOException("error, method " + methodData.name + " gets 2 args, provided " + methodData.args.size());
                }
                return new GetLink(methodData.args.get(0), methodData.args.get(1), MindScriptData.mathData);
            }
            case control -> {
                if(methodData.args.size() < 3 || methodData.args.size() > 5){
                    throw new IOException("error, method " + methodData.name + " gets 3, 4, 5 args, provided " + methodData.args.size());
                }
                switch (methodData.args.size()) {
                    case 3 -> {
                        return new Control(
                                MindScriptData.getControlType(methodData.args.get(0)),
                                methodData.args.get(1),
                                methodData.args.get(2),
                                MindScriptData.mathData
                        );
                    }
                    case 4 -> {
                        return new Control(
                                MindScriptData.getControlType(methodData.args.get(0)),
                                methodData.args.get(1),
                                methodData.args.get(2),
                                methodData.args.get(3),
                                MindScriptData.mathData
                        );
                    }
                    case 5 -> {
                        return new Control(
                                MindScriptData.getControlType(methodData.args.get(0)),
                                methodData.args.get(1),
                                methodData.args.get(2),
                                methodData.args.get(3),
                                methodData.args.get(4),
                                MindScriptData.mathData
                        );
                    }
                }
            }
            case radar -> {
                if(methodData.args.size() < 5 || methodData.args.size() > 7){
                    throw new IOException("error, method " + methodData.name + " gets 5, 6, 7 args, provided " + methodData.args.size());
                }
                switch (methodData.args.size()) {
                    case 5 -> {
                        return new Radar(
                                MindScriptData.getRadarFilterType(methodData.args.get(0)),
                                MindScriptData.getRadarSortType(methodData.args.get(1)),
                                methodData.args.get(2),
                                methodData.args.get(3),
                                methodData.args.get(4),
                                MindScriptData.mathData
                        );
                    }
                    case 6 -> {
                        return new Radar(
                                MindScriptData.getRadarFilterType(methodData.args.get(0)),
                                MindScriptData.getRadarFilterType(methodData.args.get(1)),
                                MindScriptData.getRadarSortType(methodData.args.get(2)),
                                methodData.args.get(3),
                                methodData.args.get(4),
                                methodData.args.get(5),
                                MindScriptData.mathData
                        );
                    }
                    case 7 -> {
                        return new Radar(
                                MindScriptData.getRadarFilterType(methodData.args.get(0)),
                                MindScriptData.getRadarFilterType(methodData.args.get(1)),
                                MindScriptData.getRadarFilterType(methodData.args.get(2)),
                                MindScriptData.getRadarSortType(methodData.args.get(3)),
                                methodData.args.get(4),
                                methodData.args.get(5),
                                methodData.args.get(6),
                                MindScriptData.mathData
                        );
                    }
                }
            }
            case sensor -> {
                if(methodData.args.size() != 3){
                    throw new IOException("error, method " + methodData.name + " gets 3 args, provided " + methodData.args.size());
                }
                return new Sensor(
                        methodData.args.get(0),
                        methodData.args.get(1),
                        methodData.args.get(2)
                );
            }
            //operations
            case lookup -> {
                if(methodData.args.size() != 3){
                    throw new IOException("error, method " + methodData.name + " gets 3 args, provided " + methodData.args.size());
                }
                return new Lookup(
                        MindScriptData.lookupTypeTypeMap.get(methodData.args.get(0)),
                        methodData.args.get(1),
                        methodData.args.get(2),
                        MindScriptData.mathData
                );
            }
            case packcolor -> {
                if(methodData.args.size() != 5){
                    throw new IOException("error, method " + methodData.name + " gets 5 args, provided " + methodData.args.size());
                }
                return new PackColor(
                        methodData.args.get(0),
                        methodData.args.get(1),
                        methodData.args.get(2),
                        methodData.args.get(3),
                        methodData.args.get(4),
                        MindScriptData.mathData
                );
            }
            //other logic
            case wait -> {
                if(methodData.args.size() != 1){
                    throw new IOException("error, method " + methodData.name + " gets 1 arg, provided " + methodData.args.size());
                }
                return new Wait(
                        methodData.args.get(0),
                        MindScriptData.mathData
                );
            }
            case stop -> {
                if(methodData.args.size() != 0){
                    throw new IOException("error, method " + methodData.name + " gets 0 args, provided " + methodData.args.size());
                }
                return new Stop();
            }
            // unit controls
            case ubind -> {
                if(methodData.args.size() != 1){
                    throw new IOException("error, method " + methodData.name + " gets 1 args, provided " + methodData.args.size());
                }
                return new UBind(
                        methodData.args.get(0)
                );
            }
            case ucontrol -> {
                if(methodData.args.size() < 1 || methodData.args.size() > 6){
                    throw new IOException("error, method " + methodData.name + " gets 1, 2, 3, 4, 5, 6 args, provided " + methodData.args.size());
                }
                switch (methodData.args.size()) {
                    case 1 -> {
                        return new UControl(
                                MindScriptData.getUnitControlType(methodData.args.get(0))
                        );
                    }
                    case 2 -> {
                        return new UControl(
                                MindScriptData.getUnitControlType(methodData.args.get(0)),
                                methodData.args.get(1),
                                MindScriptData.mathData
                        );
                    }
                    case 3 -> {
                        return new UControl(
                                MindScriptData.getUnitControlType(methodData.args.get(0)),
                                methodData.args.get(1),
                                methodData.args.get(2),
                                MindScriptData.mathData
                        );
                    }
                    case 4 -> {
                        return new UControl(
                                MindScriptData.getUnitControlType(methodData.args.get(0)),
                                methodData.args.get(1),
                                methodData.args.get(2),
                                methodData.args.get(3),
                                MindScriptData.mathData
                        );
                    }
                    case 5 -> {
                        return new UControl(
                                MindScriptData.getUnitControlType(methodData.args.get(0)),
                                methodData.args.get(1),
                                methodData.args.get(2),
                                methodData.args.get(3),
                                methodData.args.get(4),
                                MindScriptData.mathData
                        );
                    }
                    case 6 -> {
                        return new UControl(
                                MindScriptData.getUnitControlType(methodData.args.get(0)),
                                methodData.args.get(1),
                                methodData.args.get(2),
                                methodData.args.get(3),
                                methodData.args.get(4),
                                methodData.args.get(5),
                                MindScriptData.mathData
                        );
                    }
                }
            }
            case ulocate -> {
                if(methodData.args.size() != 5 && methodData.args.size() != 7){
                    throw new IOException("error, method " + methodData.name + " gets 5, 7 args, provided " + methodData.args.size());
                }
                switch (methodData.args.size()) {
                    case 5 -> {
                        return new ULocate(
                                MindScriptData.getObjectToFindType(methodData.args.get(0)),
                                methodData.args.get(1),
                                methodData.args.get(2),
                                methodData.args.get(3),
                                methodData.args.get(4),
                                MindScriptData.mathData
                        );
                    }
                    case 7 -> {
                        return new ULocate(
                                MindScriptData.getObjectToFindType(methodData.args.get(0)),
                                MindScriptData.getGroupType(methodData.args.get(1)),
                                methodData.args.get(2),
                                methodData.args.get(3),
                                methodData.args.get(4),
                                methodData.args.get(5),
                                methodData.args.get(6),
                                MindScriptData.mathData
                        );
                    }
                }
            }
            case uradar -> {
                if(methodData.args.size() < 4 || methodData.args.size() > 6){
                    throw new IOException("error, method " + methodData.name + " gets 4, 5, 6 args, provided " + methodData.args.size());
                }
                switch (methodData.args.size()) {
                    case 4 -> {
                        return new URadar(
                                MindScriptData.getURadarFilterType(methodData.args.get(0)),
                                MindScriptData.getURadarSortType(methodData.args.get(1)),
                                methodData.args.get(2),
                                methodData.args.get(3),
                                MindScriptData.mathData
                        );
                    }
                    case 5 -> {
                        return new URadar(
                                MindScriptData.getURadarFilterType(methodData.args.get(0)),
                                MindScriptData.getURadarFilterType(methodData.args.get(1)),
                                MindScriptData.getURadarSortType(methodData.args.get(2)),
                                methodData.args.get(3),
                                methodData.args.get(4),
                                MindScriptData.mathData
                        );
                    }
                    case 6 -> {
                        return new URadar(
                                MindScriptData.getURadarFilterType(methodData.args.get(0)),
                                MindScriptData.getURadarFilterType(methodData.args.get(1)),
                                MindScriptData.getURadarFilterType(methodData.args.get(2)),
                                MindScriptData.getURadarSortType(methodData.args.get(3)),
                                methodData.args.get(4),
                                methodData.args.get(5),
                                MindScriptData.mathData
                        );
                    }
                }
            }
        }
        throw new IOException(methodLine.value + " is not BuiltInMethod");
    }
    private static Method getMethodCodePart(LineOfCode lineOfCOde, List<LineOfCode> insideCodeList) throws IOException {
        //method name
        List<String> methodStrings = MethodsReader.findMethodsInExpression(lineOfCOde.value, MindScriptData.mathData);
        if(methodStrings.size() != 1){
            throw new IOException("error, '" + lineOfCOde.value + "' is invalid method declare");
        }
        String methodString = methodStrings.get(0);
        MethodData methodData = MethodsReader.getMethodDataFromString(methodString);
        assert methodData != null;
        //inside code
        List<CodePart> insideCodeCodeParts = new ArrayList<>();
        int i = 0;
        while(i < insideCodeList.size()){
            LineOfCode line = insideCodeList.get(i);
            switch (line.type){
                case operation -> {
                    insideCodeCodeParts.add(MathematicalExpressionReader.readExpression(line.value, MindScriptData.mathData));
                    i++;
                }
                case builtInMethodCall -> {
                    insideCodeCodeParts.add(getCallBuiltInMethodCodePart(line));
                    i++;
                }
                case cycleOrConstruction -> {
                    List<LineOfCode> insideCode = new ArrayList<>();
                    i++;
                    while (i < insideCodeList.size() && insideCodeList.get(i).spacing > line.spacing){
                        insideCode.add(insideCodeList.get(i));
                        i++;
                    }
                    insideCodeCodeParts.add(getCycleOrConstructionCodePart(line, methodData.name, insideCode));
                }
                case returnValueFromMethod -> {
                    insideCodeCodeParts.add(new ReturnValueFromMethod(line.value.replace(MindScriptData.methodReturnValueKeyWord, ""), methodData.name, MindScriptData.mathData));
                    i++;
                }
            }
        }
        return new Method(methodData.name, methodData.args, insideCodeCodeParts);
    }
    private static CodePart getCycleOrConstructionCodePart(LineOfCode lineOfCOde, String methodName, List<LineOfCode> insideCodeList) throws IOException {
        //inside code
        List<CodePart> insideCodeCodeParts = new ArrayList<>();
        int i = 0;
        while(i < insideCodeList.size()){
            LineOfCode line = insideCodeList.get(i);
            switch (line.type){
                case operation -> {
                    insideCodeCodeParts.add(MathematicalExpressionReader.readExpression(line.value, MindScriptData.mathData));
                    i++;
                }
                case builtInMethodCall -> {
                    insideCodeCodeParts.add(getCallBuiltInMethodCodePart(line));
                    i++;
                }
                case cycleOrConstruction -> {
                    List<LineOfCode> insideCode = new ArrayList<>();
                    i++;
                    while (i < insideCodeList.size() && insideCodeList.get(i).spacing > line.spacing){
                        insideCode.add(insideCodeList.get(i));
                        i++;
                    }
                    insideCodeCodeParts.add(getCycleOrConstructionCodePart(line, insideCode));
                }
                case returnValueFromMethod -> {
                    insideCodeCodeParts.add(new ReturnValueFromMethod(line.value.replace(MindScriptData.methodReturnValueKeyWord, ""), methodName, MindScriptData.mathData));
                    i++;
                }
            }
        }
        //CycleOrConstruction name
        List<String> methodStrings = MethodsReader.findMethodsInExpression(lineOfCOde.value, MindScriptData.mathData);
        if(methodStrings.size() != 1){
            throw new IOException("error, '" + lineOfCOde.value + "' is invalid method declare");
        }
        String methodString = methodStrings.get(0);
        MethodData methodData = MethodsReader.getMethodDataFromString(methodString);
        assert methodData != null;
        if(!Objects.equals(lineOfCOde.firstWord, methodData.name)){System.out.println("error, line.firstWord: " + lineOfCOde.firstWord + " != CycleOrConstruction name: " + methodData.name);}
        switch (methodData.name){
            case MindScriptData.ifConstructionName -> {
                if(methodData.args.size() != 1){
                    throw new IOException("error, " + MindScriptData.ifConstructionName + " gets only 1 argument, provided " + methodData.args.size());
                }
                return new IfCycle(methodData.args.get(0), insideCodeCodeParts, MindScriptData.mathData);
            }
            case MindScriptData.forCycleName -> {
                if(methodData.args.size() != 3){
                    throw new IOException("error, " + MindScriptData.forCycleName + " gets 3 argument, provided " + methodData.args.size());
                }
                return new ComplexForCycle(methodData.args.get(0), methodData.args.get(1), methodData.args.get(2), insideCodeCodeParts, MindScriptData.mathData);
            }
            case MindScriptData.whileCycleName -> {
                if(methodData.args.size() != 1){
                    throw new IOException("error, " + MindScriptData.whileCycleName + " get only 1 argument, provided " + methodData.args.size());
                }
                return new WhileCycle(methodData.args.get(0), insideCodeCodeParts, MindScriptData.mathData);
            }
        }
        return new Method(methodData.name, methodData.args, insideCodeCodeParts);
    }//for inside method case
    private static CodePart getCycleOrConstructionCodePart(LineOfCode lineOfCOde, List<LineOfCode> insideCodeList) throws IOException {
        //inside code
        List<CodePart> insideCodeCodeParts = new ArrayList<>();
        int i = 0;
        while(i < insideCodeList.size()){
            LineOfCode line = insideCodeList.get(i);
            switch (line.type){
                case operation -> {
                    insideCodeCodeParts.add(MathematicalExpressionReader.readExpression(line.value, MindScriptData.mathData));
                    i++;
                }
                case builtInMethodCall -> {
                    insideCodeCodeParts.add(getCallBuiltInMethodCodePart(line));
                    i++;
                }
                case cycleOrConstruction -> {
                    List<LineOfCode> insideCode = new ArrayList<>();
                    i++;
                    while (i < insideCodeList.size() && insideCodeList.get(i).spacing > line.spacing){
                        insideCode.add(insideCodeList.get(i));
                        i++;
                    }
                    insideCodeCodeParts.add(getCycleOrConstructionCodePart(line, insideCode));
                }
            }
        }
        //CycleOrConstruction name
        List<String> methodStrings = MethodsReader.findMethodsInExpression(lineOfCOde.value, MindScriptData.mathData);
        if(methodStrings.size() != 1){
            throw new IOException("error, '" + lineOfCOde.value + "' is invalid method declare");
        }
        String methodString = methodStrings.get(0);
        MethodData methodData = MethodsReader.getMethodDataFromString(methodString);
        assert methodData != null;
        if(!Objects.equals(lineOfCOde.firstWord, methodData.name)){
            throw new IOException("error, line.firstWord: " + lineOfCOde.firstWord + " != CycleOrConstruction name: " + methodData.name);
        }
        switch (methodData.name){
            case MindScriptData.ifConstructionName -> {
                if(methodData.args.size() != 1){
                    throw new IOException("error, " + MindScriptData.ifConstructionName + " gets only 1 argument, provided " + methodData.args.size());
                }
                return new IfCycle(methodData.args.get(0), insideCodeCodeParts, MindScriptData.mathData);
            }
            case MindScriptData.forCycleName -> {
                if(methodData.args.size() != 3){
                    throw new IOException("error, " + MindScriptData.forCycleName + " gets 3 arguments, provided " + methodData.args.size());
                }
                return new ComplexForCycle(methodData.args.get(0), methodData.args.get(1), methodData.args.get(2), insideCodeCodeParts, MindScriptData.mathData);
            }
            case MindScriptData.whileCycleName -> {
                if(methodData.args.size() != 1){
                    throw new IOException("error, " + MindScriptData.whileCycleName + " get only 1 argument, provided " + methodData.args.size());
                }
                return new WhileCycle(methodData.args.get(0), insideCodeCodeParts, MindScriptData.mathData);
            }
        }
        return new Method(methodData.name, methodData.args, insideCodeCodeParts);
    }

    private static List<LineOfCode> splitCodeIntoLines(String code){
        String[] lines = code.split("\n");
        List<LineOfCode> linesOfCode = new ArrayList<>();
        for(String line: lines){
            LineOfCode newLine = new LineOfCode(line);
            if(!newLine.isEmpty){
                linesOfCode.add(newLine);
            }
        }
        return linesOfCode;
    }
}
