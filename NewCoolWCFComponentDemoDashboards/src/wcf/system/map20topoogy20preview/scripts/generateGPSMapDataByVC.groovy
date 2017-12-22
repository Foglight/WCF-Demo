package system._map20topoogy20preview.scripts;

def ts = server["TopologyService"];
dataService = server.DataService;

objType = ts.getType(topologyType);
objs = ts.getObjectsOfType(objType);

objs = objs.findAll {
    it.virtualCenter.uniqueId == selectedVc.uniqueId;
}


def allObjects = [];

for (obj in objs) { 
    def metricValue; 
    
    if (topologyType == "VMWVirtualMachine") {
        if (metricName == "cpuUtilization") {
            vmCpus = obj?.cpus?.hostCPUs;
            metricValue= dataService.retrieveLatestValue(vmCpus , "utilization")?.getValue()?.getAvg();
        } else {
            vmMemory = obj?.memory?.hostMemory;
            metricValue= dataService.retrieveLatestValue(vmMemory , "utilization")?.getValue()?.getAvg();
        }
    } else {
         metricValue= dataService.retrieveLatestValue(obj, metricName)?.getValue()?.getAvg();     
    }
   

    if (metricValue) {
        if (topologyType != "VMWVirtualMachine") {
	    metricValue= new BigDecimal(metricValue/1024).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    } else {
        continue;
    }

    random = new Random();
    item = functionHelper.createDataObject("map20topoogy20preview:GPSMapData", "none", null);
    item.store("latitude", Math.random() *90 as Double, null);
    item.store("longitude", Math.random()*180 as Double, null);
    item.store("name", obj?.name, null);
    item.store("obj", obj, null);
    item.store("value", metricValue, null);
    allObjects.add(item);
}

return allObjects;