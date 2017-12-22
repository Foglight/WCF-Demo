package system._scatter.scripts;

if (!selectedObject || selectedObject.size() < 0) return null;

if (selectedObject.get(0).get("topologyTypeName") == "VMWVirtualMachine") {
    return selectedObject;
}

return [];