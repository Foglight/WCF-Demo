package system._scatter.scripts;


def TOPOLOGYTO__METRIC_MAP = ["VMWDatastore":["spaceAvailable", "spaceUsed", "totalSpace", "capacityUsed", "capacityAvailable"], "VMWVirtualMachine":["cpuUtilization","cpuReady","cpucoStopPercent", "memoryUtilization","memoryConsumed","memoryAllocated"]];

return TOPOLOGYTO__METRIC_MAP.get(topologyType); 