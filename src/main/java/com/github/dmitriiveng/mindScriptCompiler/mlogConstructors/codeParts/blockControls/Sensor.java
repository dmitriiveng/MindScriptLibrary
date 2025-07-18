package com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.blockControls;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.UncompiledCode;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.NameSpacesMethods;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.SingleLineCodePart;

public class Sensor extends SingleLineCodePart {
    String returnVarName;
    String blockVarName;
    String sensorType;

    public Sensor(String returnVarName, String blockVarName, String sensorType)
    {
        this.returnVarName = returnVarName;
        this.blockVarName = blockVarName;
        this.sensorType = sensorType;
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode){
        return String.format("sensor %s %s %s",
                NameSpacesMethods.getVarNameWithPrefix(returnVarName, nameSpaceIndex, uncompiledCode),
                NameSpacesMethods.getVarNameWithPrefix(blockVarName, nameSpaceIndex, uncompiledCode),
                sensorType
        ) + "\n";
    }
}
