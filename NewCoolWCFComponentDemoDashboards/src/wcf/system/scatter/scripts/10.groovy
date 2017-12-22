package system._scatter.scripts;

if(selectedType.equals("all")){
	return VMWDatastores
} else{
	return VMWDatastores.findAll{
		it?.datastoreType == selectedType;
	}
}