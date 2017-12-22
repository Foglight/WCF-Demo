package system._scatter.scripts;

def ts = server["TopologyService"];
dataService = server.DataService;

objType = ts.getType(selectedTopologyType);
objs = ts.getObjectsOfType(objType);

objs = objs.findAll {
    it.virtualCenter.uniqueId == selectedVc.uniqueId;
}

return objs;