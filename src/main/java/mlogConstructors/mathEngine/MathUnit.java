package mlogConstructors.mathEngine;

import mlogConstructors.codeParts.operations.Operation;

import java.util.Objects;

public class MathUnit {
    public enum mathUnitType{
        wordOrValue,
        operator
    }

    protected mathUnitType type;
    protected String stringValue;
    protected Operation.operatorType operatorType;
    protected Operation.operatorArgAmount operatorArgAmount;
    protected Operation.operationIsAssignment operationIsAssignment;
    protected int operatorPriority;

    public MathUnit(String stringValue){
        this.stringValue = stringValue;
        this.type = mathUnitType.wordOrValue;
    }
    public MathUnit(String stringValue, Operation.operatorType operatorType, Operation.operatorArgAmount operatorArgAmount, Operation.operationIsAssignment operationIsAssignment, int operatorPriority){
        this.type = mathUnitType.operator;
        this.stringValue = stringValue;
        this.operatorType = operatorType;
        this.operatorArgAmount = operatorArgAmount;
        this.operationIsAssignment = operationIsAssignment;
        this.operatorPriority = operatorPriority;
    }
    public MathUnit(MathUnit mathUnit){
        this.type = mathUnit.type;
        this.stringValue = mathUnit.stringValue;
        this.operatorType = mathUnit.operatorType;
        this.operatorArgAmount = mathUnit.operatorArgAmount;
        this.operationIsAssignment = mathUnit.operationIsAssignment;
        this.operatorPriority = mathUnit.operatorPriority;
    }

    public boolean equals(MathUnit mathUnit){
        return (this.type == mathUnit.type &&
                Objects.equals(this.stringValue, mathUnit.stringValue) &&
                Objects.equals(this.operatorType, mathUnit.operatorType) &&
                Objects.equals(this.operatorArgAmount, mathUnit.operatorArgAmount) &&
                Objects.equals(this.operationIsAssignment, mathUnit.operationIsAssignment) &&
                Objects.equals(this.operatorPriority, mathUnit.operatorPriority)
                );
    }
}
