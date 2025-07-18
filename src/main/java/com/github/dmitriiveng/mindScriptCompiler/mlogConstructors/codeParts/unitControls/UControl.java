package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.unitControls;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.ComplexCodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathData;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathematicalExpressionReader;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.ComplexOperation;

import java.io.IOException;

public class UControl extends ComplexCodePart {

    public enum UnitControlType {
        idle,
        stop,
        move,
        approach,
        pathfind,
        autoPathfind,
        boost,
        target,
        targetp,
        itemDrop,
        itemTake,
        payDrop,
        payTake,
        payEnter,
        mine,
        flag,
        build,
        getBlock,
        within,
        unbind
    }

    UnitControlType unitControlType;
    ComplexOperation x, y;
    ComplexOperation amount, someValue, boolVar;
    String unitVar, blockVar, itemVar, config;
    String returnVarName, returnVarName2, returnVarName3;

    public UControl(UnitControlType unitControlType) {
        this.unitControlType = unitControlType;
        linesCount = 1;
    }//unbind, idle, stop, autoPathfind, payDrop, payEnter,

    public UControl(UnitControlType unitControlType, String arg1, MathData mathData) throws IOException {
        this.unitControlType = unitControlType;
        switch (unitControlType) {
            case boost, payTake -> {
                boolVar = MathematicalExpressionReader.readExpression(arg1, mathData);
                linesCount = 1 + boolVar.linesCount;
            }
            case flag -> {
                someValue = MathematicalExpressionReader.readExpression(arg1, mathData);
                linesCount = 1 + someValue.linesCount;
            }
        }
    }//boost, payTake, flag

    public UControl(UnitControlType unitControlType, String arg1, String arg2, MathData mathData) throws IOException {
        this.unitControlType = unitControlType;
        switch (unitControlType) {
            case move, pathfind, mine -> {
                x = MathematicalExpressionReader.readExpression(arg1, mathData);
                y = MathematicalExpressionReader.readExpression(arg2, mathData);
                linesCount = 1 + x.linesCount + y.linesCount;
            }
            case itemDrop -> {
                blockVar = arg1;
                amount = MathematicalExpressionReader.readExpression(arg2, mathData);
                linesCount = 1 + amount.linesCount;
            }
            case targetp -> {
                unitVar = arg1;
                boolVar = MathematicalExpressionReader.readExpression(arg2, mathData);
                linesCount = 1 + boolVar.linesCount;
            }
        }
    }//move, pathfind, mine, itemDrop, targetp

    public UControl(UnitControlType unitControlType, String arg1, String arg2, String arg3, MathData mathData) throws IOException {
        this.unitControlType = unitControlType;
        switch (unitControlType) {
            case approach, target -> {
                x = MathematicalExpressionReader.readExpression(arg1, mathData);
                y = MathematicalExpressionReader.readExpression(arg2, mathData);
                someValue = MathematicalExpressionReader.readExpression(arg3, mathData);
                linesCount = 1 + x.linesCount + y.linesCount + someValue.linesCount;
            }
            case itemTake -> {
                this.blockVar = arg1;
                this.itemVar = arg2;
                amount = MathematicalExpressionReader.readExpression(arg3, mathData);
                linesCount = 1 + amount.linesCount;
            }
        }
    }//approach, target, itemTake

    public UControl(UnitControlType unitControlType, String arg1, String arg2, String arg3, String arg4, MathData mathData) throws IOException {
        this.unitControlType = unitControlType;
        x = MathematicalExpressionReader.readExpression(arg1, mathData);
        y = MathematicalExpressionReader.readExpression(arg2, mathData);
        someValue = MathematicalExpressionReader.readExpression(arg3, mathData);
        this.returnVarName = arg4;
        linesCount = 1 + x.linesCount + y.linesCount + someValue.linesCount;
    }//within

    public UControl(UnitControlType unitControlType, String arg1, String arg2, String arg3, String arg4, String arg5, MathData mathData) throws IOException {
        this.unitControlType = unitControlType;
        switch (unitControlType) {
            case build -> {
                x = MathematicalExpressionReader.readExpression(arg1, mathData);
                y = MathematicalExpressionReader.readExpression(arg2, mathData);
                this.blockVar = arg3;
                someValue = MathematicalExpressionReader.readExpression(arg4, mathData);
                this.config = arg5;
                linesCount = 1 + x.linesCount + y.linesCount + someValue.linesCount;
            }
            case getBlock -> {
                x = MathematicalExpressionReader.readExpression(arg1, mathData);
                y = MathematicalExpressionReader.readExpression(arg2, mathData);
                this.returnVarName = arg3;
                this.returnVarName2 = arg4;
                this.returnVarName3 = arg5;
                linesCount = 1 + x.linesCount + y.linesCount;
            }
        }
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        switch (unitControlType){
            case idle, stop, payDrop, payEnter, unbind, autoPathfind -> {return String.format("ucontrol %s", unitControlType.name()) + "\n";}
            case payTake, boost -> {
                allCycleCodeParts.add(boolVar);
                return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                        String.format("ucontrol %s %s",
                                unitControlType.name(),
                                NameSpacesMethods.getVarNameWithPrefix(boolVar.finalVarName, nameSpaceIndex, uncompiledCode)
                        ) + "\n";}
            case flag -> {
                allCycleCodeParts.add(someValue);
                return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                        String.format("ucontrol %s %s",
                                unitControlType.name(),
                                NameSpacesMethods.getVarNameWithPrefix(someValue.finalVarName, nameSpaceIndex, uncompiledCode)
                        ) + "\n";}
            case move, pathfind, mine -> {
                allCycleCodeParts.add(x);
                allCycleCodeParts.add(y);
                return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                        String.format("ucontrol %s %s %s",
                                unitControlType.name(),
                                NameSpacesMethods.getVarNameWithPrefix(x.finalVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(y.finalVarName, nameSpaceIndex, uncompiledCode)
                        ) + "\n";}
            case itemDrop -> {
                allCycleCodeParts.add(amount);
                return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                        String.format("ucontrol %s %s %s",
                                unitControlType.name(),
                                NameSpacesMethods.getVarNameWithPrefix(blockVar, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(amount.finalVarName, nameSpaceIndex, uncompiledCode)
                        ) + "\n";}
            case itemTake -> {
                allCycleCodeParts.add(amount);
                return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                        String.format("ucontrol itemTake %s %s %s",
                                NameSpacesMethods.getVarNameWithPrefix(blockVar, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(itemVar, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(amount.finalVarName, nameSpaceIndex, uncompiledCode)
                        ) + "\n";}
            case targetp -> {
                allCycleCodeParts.add(boolVar);
                return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                        String.format("ucontrol targetp %s %s",
                                NameSpacesMethods.getVarNameWithPrefix(unitVar, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(boolVar.finalVarName, nameSpaceIndex, uncompiledCode)
                        ) + "\n";}
            case target, approach -> {
                allCycleCodeParts.add(x);
                allCycleCodeParts.add(y);
                allCycleCodeParts.add(someValue);
                return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                        String.format("ucontrol %s %s %s %s",
                                unitControlType.name(),
                                NameSpacesMethods.getVarNameWithPrefix(x.finalVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(y.finalVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(someValue.finalVarName, nameSpaceIndex, uncompiledCode)
                        ) + "\n";}
            case within -> {
                allCycleCodeParts.add(x);
                allCycleCodeParts.add(y);
                allCycleCodeParts.add(someValue);
                return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode)+
                        String.format("ucontrol within %s %s %s %s",
                                NameSpacesMethods.getVarNameWithPrefix(x.finalVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(y.finalVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(someValue.finalVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(returnVarName, nameSpaceIndex, uncompiledCode)
                        ) + "\n";}
            case build -> {
                allCycleCodeParts.add(x);
                allCycleCodeParts.add(y);
                allCycleCodeParts.add(someValue);
                return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                        String.format("ucontrol build %s %s %s %s %s",
                                NameSpacesMethods.getVarNameWithPrefix(x.finalVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(y.finalVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(blockVar, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(someValue.finalVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(config, nameSpaceIndex, uncompiledCode)
                        ) + "\n";}
            case getBlock -> {
                allCycleCodeParts.add(x);
                allCycleCodeParts.add(y);
                return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                        String.format("ucontrol getBlock %s %s %s %s %s",
                                NameSpacesMethods.getVarNameWithPrefix(x.finalVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(y.finalVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(returnVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(returnVarName2, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(returnVarName3, nameSpaceIndex, uncompiledCode)
                        ) + "\n";}
        }
        return null;
    }
}
