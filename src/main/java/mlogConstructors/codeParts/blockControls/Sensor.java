package mlogConstructors.codeParts.blockControls;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.SingleLineCodePart;

import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;

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
                getVarNameWithPrefix(returnVarName, nameSpaceIndex, uncompiledCode),
                getVarNameWithPrefix(blockVarName, nameSpaceIndex, uncompiledCode),
                sensorType
        ) + "\n";
    }
}
