package mlogConstructors.codeParts.methods;

import mlogConstructors.UncompiledCode;
import mlogConstructors.codeParts.ComplexCodePart;
import mlogConstructors.codeParts.operations.ComplexOperation;
import mlogConstructors.codeParts.operations.Operation;
import mlogConstructors.codeParts.operations.Set;
import mlogConstructors.mathEngine.MathData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static mlogConstructors.Settings.methodReturnLineString;
import static mlogConstructors.Settings.methodReturnVarNameString;
import static mlogConstructors.codeParts.NameSpacesMethods.getVarNameWithPrefix;
import static mlogConstructors.codeParts.operations.Operation.operatorType;
import static mlogConstructors.mathEngine.MathematicalExpressionReader.readExpression;

public class CallMethod extends ComplexCodePart {
    String returnVarName;
    public String methodName;
    int methodIndex;
    List<ComplexOperation> ComplexOperationInArgs = new ArrayList<>();
    boolean returnsSomething;
    public CallMethod(String methodName, String returnVarName, List<String> ComplexOperationInArgsExpressions, MathData mathData) throws IOException {
        returnsSomething = true;
        this.methodName = methodName;
        for(String expression: ComplexOperationInArgsExpressions){
            ComplexOperationInArgs.add(readExpression(expression, mathData));
        }
        this.returnVarName = returnVarName;
        for(ComplexOperation complexOperation: ComplexOperationInArgs){
            linesCount += complexOperation.linesCount;
            linesCount += 1; // строчка чтобы приравнять аргумент к итоговому значению
        }
        linesCount += 3; // еще 2 строчки для вызова метода и 1 для смены имени итоговой переменной на returnVarName
    }
    public CallMethod(String methodName, List<String> ComplexOperationInArgsExpressions, MathData mathData) throws IOException {
        returnsSomething = false;
        this.methodName = methodName;
        for(String expression: ComplexOperationInArgsExpressions){
            ComplexOperationInArgs.add(readExpression(expression, mathData));
        }
        for(ComplexOperation complexOperation: ComplexOperationInArgs){
            linesCount += complexOperation.linesCount;
            linesCount += 1; // строчка чтобы приравнять аргумент к итоговому значению
        }
        linesCount += 2; // еще 2 строчки для вызова метода
    }

    @Override
    public String getAsCompiledCode(int previousCPLastLineIndex, int nameSpaceIndex, UncompiledCode uncompiledCode) throws IOException {
        Method methodToCall = uncompiledCode.getMethodByName(methodName);
        if(ComplexOperationInArgs.size() != methodToCall.args.size()){
            System.out.println(methodToCall.args.size() + " needed, provided " + ComplexOperationInArgs.size() + " for method, Named: " + methodName);
        }
        allCycleCodeParts.addAll(ComplexOperationInArgs);
        for(int i = 0; i < ComplexOperationInArgs.size(); i++){
            ComplexOperation complexOperation = ComplexOperationInArgs.get(i);
            allCycleCodeParts.add(new Set(getVarNameWithPrefix(methodToCall.args.get(i), methodToCall.methodNameSpace, uncompiledCode), complexOperation.finalVarName));
        }
        allCycleCodeParts.add(new Operation(
                methodReturnLineString + methodName,
                "@counter",
                operatorType.add,
                "1"
        ));
        allCycleCodeParts.add(new Set("@counter", String.valueOf(methodToCall.firstLine)));
        if(returnsSomething) {
            allCycleCodeParts.add(new Set(returnVarName, methodReturnVarNameString + methodName));
        }
        return getAllCycleCodePartsAsCompiledCode(previousCPLastLineIndex, nameSpaceIndex, uncompiledCode);
    }
}
