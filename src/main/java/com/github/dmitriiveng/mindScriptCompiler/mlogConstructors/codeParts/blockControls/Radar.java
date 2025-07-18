package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.blockControls;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.ComplexCodePart;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathematicalExpressionReader;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.ComplexOperation;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathData;

import java.io.IOException;

public class Radar extends ComplexCodePart {
    public enum RadarFilterType {
        any,
        enemy,
        ally,
        player,
        attacker,
        flying,
        boss,
        ground
    }
    RadarFilterType filter1;
    RadarFilterType filter2 = RadarFilterType.any;
    RadarFilterType filter3 = RadarFilterType.any;
    public enum RadarSortType
    {
        distance,
        health,
        shield,
        armor,
        maxHealth
    }

    RadarSortType radarSortType;
    String blockVarName;
    ComplexOperation order; // 1 - normal / 0 - reverse
    String returnVarName;

    public Radar(RadarFilterType filter1, RadarSortType radarSortType, String blockVarName, String orderExpression, String returnVarName, MathData mathData) throws IOException {
        this.filter1 = filter1;
        this.radarSortType = radarSortType;
        this.blockVarName = blockVarName;
        order = MathematicalExpressionReader.readExpression(orderExpression, mathData);
        this.returnVarName = returnVarName;
        linesCount = 1 + order.linesCount;
    } //one filter
    public Radar(RadarFilterType filter1, RadarFilterType filter2, RadarSortType radarSortType, String blockVarName, String orderExpression, String returnVarName, MathData mathData) throws IOException {
        this.filter1 = filter1;
        this.filter2 = filter2;
        this.radarSortType = radarSortType;
        this.blockVarName = blockVarName;
        order = MathematicalExpressionReader.readExpression(orderExpression, mathData);
        this.returnVarName = returnVarName;
        linesCount = 1 + order.linesCount;
    } //two filters
    public Radar(RadarFilterType filter1, RadarFilterType filter2, RadarFilterType filter3, RadarSortType radarSortType, String blockVarName, String orderExpression, String returnVarName, MathData mathData) throws IOException {
        this.filter1 = filter1;
        this.filter2 = filter2;
        this.filter3 = filter3;
        this.radarSortType = radarSortType;
        this.blockVarName = blockVarName;
        order = MathematicalExpressionReader.readExpression(orderExpression, mathData);
        this.returnVarName = returnVarName;
        linesCount = 1 + order.linesCount;
    } //three filters

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        allCycleCodeParts.add(order);
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode) +
                String.format("radar %s %s %s %s %s %s %s",
                        filter1.name(),
                        filter2.name(),
                        filter3.name(),
                        radarSortType.name(),
                        NameSpacesMethods.getVarNameWithPrefix(blockVarName, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(order.finalVarName, nameSpaceIndex, uncompiledCode),
                        NameSpacesMethods.getVarNameWithPrefix(returnVarName, nameSpaceIndex, uncompiledCode)
                ) + "\n";
    }
}
