package mlogConstructors.codeParts.unitControls;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.ComplexCodePart;
import mlogConstructors.codeParts.operations.ComplexOperation;
import mlogConstructors.mathEngine.MathData;

import java.io.IOException;

import static mlogConstructors.Settings.returnGarbageName;
import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;
import static mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

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
    String returnedBuildingVar = returnGarbageName;
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
        this.enemy = readExpression(isEnemyExpression, mathData);
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
                this.enemy = readExpression("true", mathData);
                this.outXVar = arg2;
                this.outYVar = arg3;
                this.foundVar = arg4;
            }
            case spawn, damaged -> {
                this.enemy = readExpression("true", mathData);
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
                        getVarNameWithPrefix(enemy.finalVarName, nameSpaceIndex, uncompiledCode),
                        getVarNameWithPrefix(ore, nameSpaceIndex, uncompiledCode),
                        getVarNameWithPrefix(outXVar, nameSpaceIndex, uncompiledCode),
                        getVarNameWithPrefix(outYVar, nameSpaceIndex, uncompiledCode),
                        getVarNameWithPrefix(foundVar, nameSpaceIndex, uncompiledCode),
                        getVarNameWithPrefix(returnedBuildingVar, nameSpaceIndex, uncompiledCode)
                ) + "\n";
    }
}
