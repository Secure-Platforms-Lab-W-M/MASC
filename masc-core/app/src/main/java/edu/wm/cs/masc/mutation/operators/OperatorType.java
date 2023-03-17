package edu.wm.cs.masc.mutation.operators;

/**
 * Describes the different types of mutations related to restrictive and
 * flexible crypto APIs
 */
public enum OperatorType {
    StringDifferentCase,
    StringNoiseReplace,
    StringSafeReplaceWithUnsafe,
    StringUnsafeReplaceWithUnsafe,
    StringStringCaseTransform,
    StringValueInVariable,
    DifferentClass,
    ValueOfChar,
    ByteLoop,
    ByteCurrentTime,
    Interproc,
    InterprocAddition,
    InterprocConditional,
    InterprocBaseCaseSeperateClass,
    InterprocBaseCase,
    InterprocNestedConditional,
    AIOEmptyFromAbstractType,
    AIOEmptyAbstractClassExtendsAbstractClass,
    AIOEmptyAbstractClassImplementsInterface,
    AIOEmptyInterfaceExtendsInterface,
    AIOGenericAbstractClassExtendsAbstractClass,
    AIOGenericAbstractClassImplementsInterface,
    AIOGenericInterfaceExtendsInterface,
    AIOSpecificAbstractClassExtendsAbstractClass,
    AIOSpecificAbstractClassImplementsInterface,
    AIOSpecificInterfaceExtendsInterface,
    IntValueInVariable,
    IntArithmetic,
    IntValueInVariableArithmetic,
    IntIterationMultipleCall,
    IntWhileLoopAccumulation,
    IntRoundValue,
    IntAbsoluteValue,
    IntNestedClass,
    IntSquareThenRoot,
    IntFromString,
    Overflow,
}
