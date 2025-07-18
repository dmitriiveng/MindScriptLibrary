package mlogConstructors.codeParts.unitControls;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.ComplexCodePart;
import mlogConstructors.codeParts.operations.ComplexOperation;
import mlogConstructors.mathEngine.MathData;

import java.io.IOException;

import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;
import static mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

public class URadar extends ComplexCodePart {
    public enum URadarFilterType {
        any,
        enemy,
        ally,
        player,
        attacker,
        flying,
        boss,
        ground
    }
    URadarFilterType filter1 = URadarFilterType.any;
    URadarFilterType filter2 = URadarFilterType.any;
    URadarFilterType filter3 = URadarFilterType.any;
    public enum URadarSortType
    {
        distance,
        health,
        shield,
        armor,
        maxHealth
    }

    URadarSortType uRadarSortType;
    ComplexOperation sortMode; // 1 - normal / 0 - reverse
    String returnVarName;
    public URadar(URadarFilterType filter1, URadarSortType uRadarSortType, String sortModeExpression, String returnVarName, MathData mathData) throws IOException {
        this.filter1 = filter1;
        this.uRadarSortType = uRadarSortType;
        sortMode = readExpression(sortModeExpression, mathData);
        this.returnVarName = returnVarName;
        linesCount = 1 + sortMode.linesCount;
    }
    public URadar(URadarFilterType filter1, URadarFilterType filter2, URadarSortType uRadarSortType, String sortModeExpression, String returnVarName, MathData mathData) throws IOException {
        this.filter1 = filter1;
        this.filter2 = filter2;
        this.uRadarSortType = uRadarSortType;
        sortMode = readExpression(sortModeExpression, mathData);
        this.returnVarName = returnVarName;
        linesCount = 1 + sortMode.linesCount;
    }
    public URadar(URadarFilterType filter1, URadarFilterType filter2, URadarFilterType filter3, URadarSortType uRadarSortType, String sortModeExpression, String returnVarName, MathData mathData) throws IOException {
        this.filter1 = filter1;
        this.filter2 = filter2;
        this.filter3 = filter3;
        this.uRadarSortType = uRadarSortType;
        sortMode = readExpression(sortModeExpression, mathData);
        this.returnVarName = returnVarName;
        linesCount = 1 + sortMode.linesCount;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        allCycleCodeParts.add(sortMode);
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                String.format("uradar %s %s %s %s 0 %s %s",
                        filter1,
                        filter2,
                        filter3,
                        uRadarSortType,
                        getVarNameWithPrefix(sortMode.finalVarName, nameSpaceIndex, uncompiledCode),
                        getVarNameWithPrefix(returnVarName, nameSpaceIndex, uncompiledCode)
                ) + "\n";
    }
}
