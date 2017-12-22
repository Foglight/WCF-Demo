package system._test20tree20map.scripts;

def ts = server["TopologyService"];
dataService = server.DataService;

dsType = ts.getType("VMWDatastore");
datastores = ts.getObjectsOfType(dsType);

datastores = datastores.findAll {
    it.virtualCenter.uniqueId == selectedVc.uniqueId;
}

return datastores;
