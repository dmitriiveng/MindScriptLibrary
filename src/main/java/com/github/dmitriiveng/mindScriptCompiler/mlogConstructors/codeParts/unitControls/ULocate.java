package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.unitControls;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.Settings;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.ComplexCodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathematicalExpressionReader;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.ComplexOperation;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathData;

import java.io.IOException;

public class ULocate extends ComplexCodePart {
    public enum ObjectToFindType {
        building, ore, spawn, damaged
    }
    public enum GroupType {
        core, storage, generator, turret, factory, repair, battery, reactor
    }

    ObjectToFindType objectToFind;
    GroupType group = GroupType.core;
    ComplexOperation enemy;
    String ore = "@copper";
    String outXVar;
    String outYVar;
    String foundVar;
    String returnedBuildingVar = Settings.returnGarbageName;
    public ULocate(
            ObjectToFindType objectToFind,
            GroupType group,
            String isEnemyExpression,
            String outXVar, String outYVar,
            String foundVar,
            String returnedBuildingVar,
            MathData mathData) throws IOException {
        this.objectToFind = objectToFind;
        this.group = group;
        this.enemy = MathematicalExpressionReader.readExpression(isEnemyExpression, mathData);
        this.outXVar = outXVar;
        this.outYVar = outYVar;
        this.foundVar = foundVar;
        this.returnedBuildingVar = returnedBuildingVar;
        linesCount = 1 + enemy.linesCount;
    }//case objectToFind is building
    public ULocate(
            ObjectToFindType objectToFind,
            String arg1,
            String arg2, String arg3,
            String arg4,
            MathData mathData) throws IOException {
        this.objectToFind = objectToFind;
        switch (objectToFind){
            case ore -> {
                this.ore = arg1;
                this.enemy = MathematicalExpressionReader.readExpression("true", mathData);
                this.outXVar = arg2;
                this.outYVar = arg3;
                this.foundVar = arg4;
            }
            case spawn, damaged -> {
                this.enemy = MathematicalExpressionReader.readExpression("true", mathData);
                this.outXVar = arg1;
                this.outYVar = arg2;
                this.foundVar = arg3;
                this.returnedBuildingVar = arg4;
            }
        }
        linesCount = 1 + enemy.linesCount;
    }//other cases
    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        allCycleCodeParts.add(enemy);
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                String.format("ulocate %s %s %s %s %s %s %s %s",
                        objectToFind,
                        group,
                        NameSpacesMethods.getVarNameWithPrefix(enemy.finalVarName, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(ore, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(outXVar, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(outYVar, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(foundVar, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(returnedBuildingVar, nameSpaceIndex, uncompiledCode)
                ) + "\n";
    }
}
