package system._test20tree20map.scripts;


def ts = server["TopologyService"];
dataService = server.DataService;

vmType = ts.getType("VMWVirtualMachine");
vms = ts.getObjectsOfType(vmType);

vms= vms.findAll {
    it.virtualCenter.uniqueId == selectedVc.uniqueId;
}

return vms;