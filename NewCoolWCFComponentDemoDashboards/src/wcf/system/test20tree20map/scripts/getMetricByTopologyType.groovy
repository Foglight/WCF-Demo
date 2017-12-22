package system._test20tree20map.scripts;


def TOPOLOGYTO__METRIC_MAP = ["VMWDatastore":["spaceAvailable", "spaceUsed", "totalSpace"], "VMWVirtualMachine":["cpuUtilization", "memoryUtilization"]];

return TOPOLOGYTO__METRIC_MAP.get(topologyType); 