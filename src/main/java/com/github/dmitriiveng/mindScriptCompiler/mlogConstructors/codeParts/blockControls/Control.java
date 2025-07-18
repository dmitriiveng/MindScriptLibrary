package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.blockControls;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.ComplexCodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathematicalExpressionReader;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.ComplexOperation;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathData;

import java.io.IOException;

public class Control extends ComplexCodePart {
    public enum ControlType {
        enabled,
        shoot,
        shootp,
        config,
        color
    }
    ControlType type;
    String blockVarName;
    String color;
    String config;
    ComplexOperation enabled;
    ComplexOperation x;
    ComplexOperation y;
    String unitVarName;
    ComplexOperation shoot;
    public Control(ControlType type, String arg1, String arg2, MathData mathData) throws IOException {
        this.type = type;
        switch (type){
            case color -> {
                this.blockVarName = arg1;
                this.color = arg2;
                linesCount = 1;
            }
            case config -> {
                this.blockVarName = arg1;
                this.config = arg2;
                linesCount = 1;
            }
            case enabled -> {
                this.blockVarName = arg1;
                this.enabled = MathematicalExpressionReader.readExpression(arg2, mathData);
                linesCount = 1 + enabled.linesCount;
            }
        }
    }//config, color, enabled
    public Control(ControlType type, String blockVarName, String xExpression, String yExpression, String shootExpression, MathData mathData) throws IOException {
        this.type = type;
        this.blockVarName = blockVarName;
        this.x = MathematicalExpressionReader.readExpression(xExpression, mathData);
        this.y = MathematicalExpressionReader.readExpression(yExpression, mathData);
        this.shoot = MathematicalExpressionReader.readExpression(shootExpression, mathData);
        linesCount = 1 + x.linesCount + y.linesCount + shoot.linesCount;
    }//shoot
    public Control(ControlType type, String blockVarName, String unitVarName, String shootExpression, MathData mathData) throws IOException {
        this.type = type;
        this.blockVarName = blockVarName;
        this.unitVarName = unitVarName;
        this.shoot = MathematicalExpressionReader.readExpression(shootExpression, mathData);
        linesCount = 1 + shoot.linesCount;
    }//shootp

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        switch (type){
            case enabled -> {
                allCycleCodeParts.add(enabled);
                return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                        String.format("control enabled %s %s", blockVarName, NameSpacesMethods.getVarNameWithPrefix(enabled.finalVarName, nameSpaceIndex, uncompiledCode)) + "\n";
            }
            case config -> {
                return String.format("control config %s %s", blockVarName, NameSpacesMethods.getVarNameWithPrefix(config, nameSpaceIndex, uncompiledCode)) + "\n";
            }
            case color -> {
                return String.format("control color %s %s", blockVarName, NameSpacesMethods.getVarNameWithPrefix(color, nameSpaceIndex, uncompiledCode)) + "\n";
            }
            case shoot -> {
                allCycleCodeParts.add(x);
                allCycleCodeParts.add(y);
                allCycleCodeParts.add(shoot);
                return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                        String.format("control shoot %s %s %s %s",
                                NameSpacesMethods.getVarNameWithPrefix(blockVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(x.finalVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(y.finalVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(shoot.finalVarName, nameSpaceIndex, uncompiledCode)
                        ) + "\n";
            }
            case shootp -> {
                allCycleCodeParts.add(shoot);
                return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                        String.format("control shootp %s %s %s",
                                NameSpacesMethods.getVarNameWithPrefix(blockVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(unitVarName, nameSpaceIndex, uncompiledCode),
                                NameSpacesMethods.getVarNameWithPrefix(shoot.finalVarName, nameSpaceIndex, uncompiledCode)
                        ) + "\n";
            }
        }
        return null;
    }
}
