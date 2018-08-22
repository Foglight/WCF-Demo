package system._test20tree20map.scripts;


def TOPOLOGYTO__METRIC_MAP = ["VMWDatastore":["spaceAvailable", "spaceUsed", "totalSpace", "weeklyGrowthRate", "readLatency", "writeLatency", "averageLatency", "alarmSeverity"], "VMWVirtualMachine":["cpuUtilization", "memoryUtilization", "cpuContentionScore", "memBalloonScore", "maxTotalDiskLatency", "alarmSeverity"]];

return TOPOLOGYTO__METRIC_MAP.get(topologyType); 