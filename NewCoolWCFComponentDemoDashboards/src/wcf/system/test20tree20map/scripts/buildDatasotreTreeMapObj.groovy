package system._test20tree20map.scripts;

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

    dsTreeMapObj = functionHelper.createDataObject("test20tree20map:TreeMapObj", "none", null);
    dsTreeMapObj.store("name", obj.name, null);
    dsTreeMapObj.store("id", obj.uniqueId, null);
    dsTreeMapObj.store("count", metricValue, null);
    dsTreeMapObj.store("ds", obj, null);
    allObjects.add(dsTreeMapObj);
}

return allObjects;
