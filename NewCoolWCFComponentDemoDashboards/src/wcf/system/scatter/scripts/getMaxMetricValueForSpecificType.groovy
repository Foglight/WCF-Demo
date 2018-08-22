package system._scatter.scripts;

dataService = server.DataService;
def vmMetrricsMapping = ["cpuReady":"percentReadyTime", "cpucoStopPercent":"coStopPercent", "memoryConsumed":"consumed", "memoryAllocated":"allocated","cpuUtilization":"utilization", "memoryUtilization":"utilization"];


objs = functionHelper.invokeFunction("system:scatter.getTopologyObjFromSpecificVC", selectedTopologyType, selectedVc);
def maxColorValue = 0;

for (obj in objs) {
    def colorMetricValue;
    if (selectedTopologyType =="VMWDatastore") {
        colorMetricValue = dataService.retrieveLatestValue(obj, colorMetric)?.getValue()?.getAvg(); 
    } else {
        if (colorMetric== "cpuUtilization") {
            hostCpu = obj?.cpus?.hostCPUs;
            colorMetricValue = dataService.retrieveLatestValue(hostCpu , "utilization")?.getValue()?.getAvg();
        } else if (colorMetric == "memoryUtilization") {
            hostMemory = obj?.memory?.hostMemory;
            colorMetricValue = dataService.retrieveLatestValue(hostMemory, "utilization")?.getValue()?.getAvg();
        } else if (colorMetric.indexOf("cpu") >= 0) {
            vmCpu = obj?.cpus;
            colorMetricValue = dataService.retrieveLatestValue(vmCpu, vmMetrricsMapping.get(colorMetric))?.getValue()?.getAvg();
        } else {
            vmMemory = obj?.memory;
            colorMetricValue = dataService.retrieveLatestValue(vmMemory , vmMetrricsMapping.get(colorMetric))?.getValue()?.getAvg();
        }
    }
   
    if (colorMetricValue > maxColorValue) {
        maxColorValue = colorMetricValue;
    }
}

return maxColorValue;