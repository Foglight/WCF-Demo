package system._test20tree20map.scripts;
import com.quest.nitro.service.sl.interfaces.data.ObservationQuery.RetrievalType;
import com.quest.wcf.core.util.ColorUtils;
import java.awt.Color;

ds = server.DataService;
ts = server["TopologyService"];
dataService = server.DataService;
endTime = specificTimeRange.getEnd().getTime();
startTime = endTime - 30 * 60 * 1000;


objType = ts.getType(topologyType);
objs = ts.getObjectsOfType(objType);

objs = objs.findAll {
    it?.virtualCenter?.uniqueId == selectedVc?.uniqueId;
}

if (topologyType == "VMWVirtualMachine") {
    def stateResult = getVMStateUseByBatchQuery(objs);
    objs = objs.findAll { vm ->;
        def values = stateResult.getLastNValues(vm, "vmState");
        return values && values.size() > 0 && values.get(0).getValue() == "poweredOn";        
    }
}
def maxColorValue = 0;
def colorValueToObj = [:];

def allObjects = [];

for (obj in objs) {    
    def metricValue; 
    def colorMetricValue;
    if (topologyType == "VMWVirtualMachine") {
        metricValue = getVirtualMachineMetricValue(metricName, obj);
        colorMetricValue = getVirtualMachineMetricValue(colorMetricName, obj);
    } else {
        metricValue = getDataStoreMetricValue(metricName, obj);
        colorMetricValue = getDataStoreMetricValue(colorMetricName, obj);   
    }
    
   
    if (colorMetricValue && colorMetricValue > maxColorValue ) {
        maxColorValue = colorMetricValue;
    }

    if (metricValue && metricValue > 0) {
        if (topologyType != "VMWVirtualMachine" && metricName != "aggregateAlarms") {
	    metricValue= new BigDecimal(metricValue/1024).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    } else {
        continue;
    }

    
    dsTreeMapObj = functionHelper.createDataObject("test20tree20map:TreeMapObj", "none", null);
    dsTreeMapObj.store("name", obj.name, null);
    dsTreeMapObj.store("id", obj.uniqueId, null);
    dsTreeMapObj.store("count", metricValue, null);
    dsTreeMapObj.store("ds", obj, null);
   
    colorValueToObj.put(dsTreeMapObj, colorMetricValue);
    allObjects.add(dsTreeMapObj);
}

colorValueToObj.each {
    dsTreeMapObj = it.key;
    colorMetricValue = it.value;
    if (!colorMetricValue) colorMetricValue = 0;
    def rnd = new Random()
    def h = new BigDecimal(colorMetricValue/maxColorValue).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;

    colors = functionHelper.invokeFunction("system:test20tree20map.colorPatternMapping", selectColorPattern );
    
    //def saturation = (((colorMetricValue / maxColorValue)*0.6) + 0.4) as float;
    //def brightness = 1- (((colorMetricValue / maxColorValue)*0.2) + 0.2) as float;
    //rgb = Color.HSBtoRGB(h, saturation, brightness);

    //def color = calculateColorByValue(h);
    //println("@@@@h is: " + h)

    int colorIndex =h / colors.size() as int;
    if (colorIndex  == colors.size()) {
        colorIndex  -= 1;
    }
    def colorHgb = colors.get(colorIndex);

    def color =  Color.decode(colorHgb);
    dsTreeMapObj.store("fillColor", color, null); 
}

return allObjects;

def getVMStateUseByBatchQuery(vms) {
    query = ds.createObservationQuery();
    query.setStartTime(startTime);
    query.setEndTime(endTime);
    query.setRetrievalType(RetrievalType.LAST_N);
    query.setNumberOfValues(1);
    query.include(vms as Set, "vmState");
    return ds.performQuery(query);
}


def getVirtualMachineMetricValue(metricName, obj) {
    def metricValue; 
    if (metricName == "aggregateAlarms") {
             def alarms = obj?.aggregateAlarms?.findAll{ it.isCleared == false };
             metricValue = alarms.size();
    } else if  (colorMetricName == "alarmSeverity") { 
             metricValue = getHighestSeverityFromObj(obj);
    } else if (metricName == "cpuUtilization") {
            vmCpus = obj?.cpus?.hostCPUs;
            metricValue= dataService.retrieveLatestValue(vmCpus , "utilization")?.getValue()?.getAvg();
    } else if (metricName == "memoryUtilization") {
            vmMemory = obj?.memory?.hostMemory;
            metricValue= dataService.retrieveLatestValue(vmMemory , "utilization")?.getValue()?.getAvg();
    } else {
            metricValue= dataService.retrieveLatestValue(obj, metricName)?.getValue()?.getAvg();
    }
    return metricValue; 
}

def getDataStoreMetricValue(metricName, obj) {
    def metricValue; 
    if (metricName == "aggregateAlarms") {
             def alarms = obj?.aggregateAlarms?.findAll{ it.isCleared == false };
             metricValue = alarms.size();
    } else if  (colorMetricName == "alarmSeverity") { 
             metricValue = getHighestSeverityFromObj(obj);
    } else {
           metricValue = dataService.retrieveLatestValue(obj, metricName)?.getValue()?.getAvg();  
    }
    return metricValue; 
}

def getHighestSeverityFromObj(obj) {
    if (obj?.alarmAggregateFatalCount > 0) {
        return 4;
    } else if (obj?.alarmAggregateCriticalCount > 0) {
        return 3;
    } else if (obj?.alarmAggregateWarningCount> 0) {
        return 2;
    } else {
        return 1;
    }
}

/*
def calculateColorByValue(val) {
        float one = (255 + 255) / 60;
        int r=0,g=0,b=0;
        if (val < 30) {
            r = (int)(one * val);
            g = 205;
        } else if (val >= 30 && val < 60)  {
            r = 255;
            g = 255 - (int)((val - 30) * one);
        } else {
            r = 205;
        }
        return new Color(r, g, b);
}


def calculateColorByValue(value) {
    int oldR = 50 ,oldG =205,oldB =50;
    int newR = 205 ,newG =51, newB =51;
    Color oldColor = new Color(oldR,oldG,oldB);  
    Color newColor = new Color(newR,newG,newB);

    int r = oldColor.getRed() + (newColor.getRed() - oldColor.getRed())*value/100;
    int g = oldColor.getGreen() + (newColor.getGreen() - oldColor.getGreen())*value/100;
    int b = oldColor.getBlue() + (newColor.getBlue() - oldColor.getBlue())*value/100;
    return new Color(r,g,b);
}
*/