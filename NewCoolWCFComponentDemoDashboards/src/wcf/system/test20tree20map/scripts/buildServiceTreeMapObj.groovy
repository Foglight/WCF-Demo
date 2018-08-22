package system._test20tree20map.scripts;
import com.quest.nitro.service.sl.interfaces.data.ObservationQuery.RetrievalType;
import com.quest.wcf.core.util.ColorUtils;
import java.awt.Color;

ds = server.DataService;
ts = server["TopologyService"];
dataService = server.DataService;
endTime = specificTimeRange.getEnd().getTime();
startTime = endTime - 30 * 60 * 1000;

objs  = functionHelper.invokeQuery("system:test20tree20map.queryAllServices");

def maxColorValue = 0;
def colorValueToObj = [:];
def allObjects = [];

for (obj in objs) {    
    def metricValue; 
    def colorMetricValue;
   
    metricValue = getMetricValue(metricName, obj);
    colorMetricValue = getMetricValue(colorMetricName, obj);
    
    
   
    if (colorMetricValue && colorMetricValue > maxColorValue ) {
        maxColorValue = colorMetricValue;
    }

    if (!metricValue || metricValue <= 0) {
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



def getMetricValue(metricName, obj) {
    def metricValue; 
    if (metricName == "aggregateAlarms") {
             def alarms = obj?.aggregateAlarms?.findAll{ it.isCleared == false };
             metricValue = alarms.size();
    } else if  (metricName == "alarmSeverity") { 
             metricValue = getHighestSeverityFromObj(obj);
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
