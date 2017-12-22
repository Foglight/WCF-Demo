package system._scatter.scripts;

def topologyType = obj.get("topologyTypeName");
def vmMetrricsMapping = ["cpuReady":"percentReadyTime", "cpucoStopPercent":"coStopPercent", "memoryConsumed":"consumed", "memoryAllocated":"allocated","cpuUtilization":"utilization", "memoryUtilization":"utilization"];
def metric;

if (topologyType =="VMWDatastore") {
    metric = obj?.get(metricName,specificTimeRange);
} else {
    if (metricName == "cpuUtilization") {
        hostCpu = obj?.cpus?.hostCPUs;
        metric = hostCpu?.get(vmMetrricsMapping.get(metricName),specificTimeRange);
    } else if (metricName == "memoryUtilization") {
        hostMemory = obj?.memory?.hostMemory;
        metric = hostMemory?.get(vmMetrricsMapping.get(metricName),specificTimeRange);
    } else if (metricName.indexOf("cpu") >= 0) {
        vmCpu = obj?.cpus;
        metric = vmCpu?.get(vmMetrricsMapping.get(metricName),specificTimeRange); 
    } else {
        vmMemory = obj?.memory;
        metric = vmMemory?.get(vmMetrricsMapping.get(metricName),specificTimeRange); 
     }
}
return metric;