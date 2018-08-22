package system._test20tree20map.scripts;
import com.quest.nitro.service.sl.interfaces.data.ObservationQuery.RetrievalType;

def ts = server["TopologyService"];
ds = server.DataService;
def poweredOnVMs = []
dataService = server.DataService;
endTime = specificTimeRange.getEnd().getTime();
startTime = endTime - 30 * 60 * 1000;

vmType = ts.getType("VMWVirtualMachine");
vms = ts.getObjectsOfType(vmType);

vms= vms.findAll {
    it.virtualCenter.uniqueId == selectedVc.uniqueId;
}

def stateResult = getVMStateUseByBatchQuery();

vms.each { vm ->;
    def values = stateResult.getLastNValues(vm, "vmState");
    if (values && values.size() > 0 && values.get(0).getValue() == "poweredOn") {
        poweredOnVMs.add(vm);
    }
}

return poweredOnVMs;

def getVMStateUseByBatchQuery() {
    query = ds.createObservationQuery();
    query.setStartTime(startTime);
    query.setEndTime(endTime);
    query.setRetrievalType(RetrievalType.LAST_N);
    query.setNumberOfValues(1);
    query.include(vms as Set, "vmState");
    return ds.performQuery(query);
}