package com.github.dmitriiveng.mindScriptCompiler.compilers.mindScriptCode;

import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.blockControls.Radar.RadarFilterType;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.blockControls.Radar.RadarSortType;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.operations.Lookup.LookupType;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.unitControls.ULocate.GroupType;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.unitControls.ULocate.ObjectToFindType;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.unitControls.URadar;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.unitControls.UControl.UnitControlType;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.mathEngine.MathData;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.blockControls.Control;
import com.github.dmitriiveng.mindScriptCompiler.mlogConstructors.codeParts.inputOutput.Draw;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MindScriptData {
    static final MathData mathData = new MathData();
    static final String ifConstructionName = "if";
    static final String elseConstructionName = "else";
    static final String forCycleName = "for";
    static final String whileCycleName = "while";
    static final String loopCycleName = "loop";
    static final List<String> cyclesAndConstructions = List.of(
            ifConstructionName,
            elseConstructionName,
            forCycleName,
            whileCycleName,
            loopCycleName
    );
    static final String newMethodKeyWord = "func";
    static final String methodReturnValueKeyWord = "return";

    enum BuiltInMethods{
        read, write, draw, print,
        drawflush, printflush, getlink, control, radar, sensor,
        lookup, packcolor,
        wait, stop,
        ubind, ucontrol, uradar, ulocate
    }

    static final List<String> builtInMethods = List.of(
            "read", "write", "draw", "print",// ввод / вывод
            "drawflush", "printflush", "getlink", "control", "radar", "sensor", // управление блоками
            "lookup", "packcolor", //операции
            "wait", "stop", //управление последовательностью
            "ubind", "ucontrol", "uradar", "ulocate" // управление юнитами
    );
    static final Map<String, BuiltInMethods> builtInMethodsMap = Stream.of(new Object[][] {
            {"read", BuiltInMethods.read},
            {"write", BuiltInMethods.write},
            {"draw", BuiltInMethods.draw},
            {"print", BuiltInMethods.print},
            {"drawflush", BuiltInMethods.drawflush},
            {"printflush", BuiltInMethods.printflush},
            {"getlink", BuiltInMethods.getlink},
            {"control", BuiltInMethods.control},
            {"radar", BuiltInMethods.radar},
            {"sensor", BuiltInMethods.sensor},
            {"lookup", BuiltInMethods.lookup},
            {"packcolor", BuiltInMethods.packcolor},
            {"wait", BuiltInMethods.wait},
            {"stop", BuiltInMethods.stop},
            {"ubind", BuiltInMethods.ubind},
            {"ucontrol", BuiltInMethods.ucontrol},
            {"uradar", BuiltInMethods.uradar},
            {"ulocate", BuiltInMethods.ulocate}
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (BuiltInMethods) data[1]));
    public static BuiltInMethods getBuiltInMethod(String name){
        name = name.replace(" ", "");
        return builtInMethodsMap.get(name);
    }
    static final Map<String, Draw.DrawType> drawTypeMap = Stream.of(new Object[][] {
            {"clear", Draw.DrawType.clear},
            {"color", Draw.DrawType.color},
            {"col", Draw.DrawType.col},
            {"stroke", Draw.DrawType.stroke},
            {"line", Draw.DrawType.line},
            {"rect", Draw.DrawType.rect},
            {"lineRect", Draw.DrawType.lineRect},
            {"poly", Draw.DrawType.poly},
            {"linePoly", Draw.DrawType.linePoly},
            {"triangle", Draw.DrawType.triangle},
            {"image", Draw.DrawType.image}
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Draw.DrawType) data[1]));
    public static Draw.DrawType getDrawType(String name){
        name = name.replace(" ", "");
        return drawTypeMap.get(name);
    }
    static final Map<String, Control.ControlType> blockControlTypeMap = Stream.of(new Object[][] {
            {"enabled", Control.ControlType.enabled},
            {"shoot", Control.ControlType.shoot},
            {"shootp", Control.ControlType.shootp},
            {"config", Control.ControlType.config},
            {"color", Control.ControlType.color}
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Control.ControlType) data[1]));
    public static Control.ControlType getControlType(String name){
        name = name.replace(" ", "");
        return blockControlTypeMap.get(name);
    }
    static final Map<String, RadarFilterType> blockRadarFilterTypeMap = Stream.of(new Object[][] {
            {"any", RadarFilterType.any},
            {"enemy", RadarFilterType.enemy},
            {"ally", RadarFilterType.ally},
            {"player", RadarFilterType.player},
            {"attacker", RadarFilterType.attacker},
            {"flying", RadarFilterType.flying},
            {"boss", RadarFilterType.boss},
            {"ground", RadarFilterType.ground}
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (RadarFilterType) data[1]));
    public static RadarFilterType getRadarFilterType(String name){
        name = name.replace(" ", "");
        return blockRadarFilterTypeMap.get(name);
    }
    static final Map<String, RadarSortType> blockRadarSortTypeMap = Stream.of(new Object[][] {
            {"distance", RadarSortType.distance},
            {"health", RadarSortType.health},
            {"shield", RadarSortType.shield},
            {"armor", RadarSortType.armor},
            {"maxHealth", RadarSortType.maxHealth}
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (RadarSortType) data[1]));
    public static RadarSortType getRadarSortType(String name){
        name = name.replace(" ", "");
        return blockRadarSortTypeMap.get(name);
    }
    static final Map<String, UnitControlType> unitControlTypeMap = Stream.of(new Object[][] {
            {"idle", UnitControlType.idle},
            {"stop", UnitControlType.stop},
            {"move", UnitControlType.move},
            {"approach", UnitControlType.approach},
            {"pathfind", UnitControlType.pathfind},
            {"autoPathfind", UnitControlType.autoPathfind},
            {"boost", UnitControlType.boost},
            {"target", UnitControlType.target},
            {"targetp", UnitControlType.targetp},
            {"itemDrop", UnitControlType.itemDrop},
            {"itemTake", UnitControlType.itemTake},
            {"payDrop", UnitControlType.payDrop},
            {"payTake", UnitControlType.payTake},
            {"payEnter", UnitControlType.payEnter},
            {"mine", UnitControlType.mine},
            {"flag", UnitControlType.flag},
            {"build", UnitControlType.build},
            {"getBlock", UnitControlType.getBlock},
            {"within", UnitControlType.within},
            {"unbind", UnitControlType.unbind}
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (UnitControlType) data[1]));
    public static UnitControlType getUnitControlType(String name){
        name = name.replace(" ", "");
        return unitControlTypeMap.get(name);
    }
    static final Map<String, ObjectToFindType> uLocateObjectToFindTypeMap = Stream.of(new Object[][] {
            {"building", ObjectToFindType.building},
            {"ore", ObjectToFindType.ore},
            {"spawn", ObjectToFindType.spawn},
            {"damaged", ObjectToFindType.damaged}
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (ObjectToFindType) data[1]));
    public static ObjectToFindType getObjectToFindType(String name){
        name = name.replace(" ", "");
        return uLocateObjectToFindTypeMap.get(name);
    }
    static final Map<String, GroupType> uLocateGroupTypeMap = Stream.of(new Object[][] {
            {"core", GroupType.core},
            {"storage", GroupType.storage},
            {"generator", GroupType.generator},
            {"turret", GroupType.turret},
            {"factory", GroupType.factory},
            {"repair", GroupType.repair},
            {"battery", GroupType.battery},
            {"reactor", GroupType.reactor}
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (GroupType) data[1]));
    public static GroupType getGroupType(String name){
        name = name.replace(" ", "");
        return uLocateGroupTypeMap.get(name);
    }
    static final Map<String, LookupType> lookupTypeTypeMap = Stream.of(new Object[][] {
            {"block", LookupType.block},
            {"unit", LookupType.unit},
            {"item", LookupType.item},
            {"liquid", LookupType.liquid}
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (LookupType) data[1]));
    public static LookupType getLookupType(String name){
        name = name.replace(" ", "");
        return lookupTypeTypeMap.get(name);
    }
    static final Map<String, URadar.URadarFilterType> unitRadarFilterTypeMap = Stream.of(new Object[][] {
            {"any", URadar.URadarFilterType.any},
            {"enemy", URadar.URadarFilterType.enemy},
            {"ally", URadar.URadarFilterType.ally},
            {"player", URadar.URadarFilterType.player},
            {"attacker", URadar.URadarFilterType.attacker},
            {"flying", URadar.URadarFilterType.flying},
            {"boss", URadar.URadarFilterType.boss},
            {"ground", URadar.URadarFilterType.ground}
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (URadar.URadarFilterType) data[1]));
    public static URadar.URadarFilterType getURadarFilterType(String name){
        name = name.replace(" ", "");
        return unitRadarFilterTypeMap.get(name);
    }
    static final Map<String, URadar.URadarSortType> unitRadarSortTypeMap = Stream.of(new Object[][] {
            {"distance", URadar.URadarSortType.distance},
            {"health", URadar.URadarSortType.health},
            {"shield", URadar.URadarSortType.shield},
            {"armor", URadar.URadarSortType.armor},
            {"maxHealth", URadar.URadarSortType.maxHealth}
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (URadar.URadarSortType) data[1]));
    public static URadar.URadarSortType getURadarSortType(String name){
        name = name.replace(" ", "");
        return unitRadarSortTypeMap.get(name);
    }
}
