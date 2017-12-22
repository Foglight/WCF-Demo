package system._test20tree20map.scripts;

def ts = server["TopologyService"];
dataService = server.DataService;
vcType = ts.getType("VMWVirtualCenter");
vcs = ts.getObjectsOfType(vcType);

return vcs;