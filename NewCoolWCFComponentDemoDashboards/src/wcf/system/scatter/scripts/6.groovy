package system._scatter.scripts;

def ts = server["TopologyService"];
dataService = server.DataService;
vcType = ts.getType("VMWVirtualCenter");
vcs = ts.getObjectsOfType(vcType);

return vcs;