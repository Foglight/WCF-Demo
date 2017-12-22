package system._map20topoogy20preview.scripts;
def items = [];
100.times{
    random = new Random();
    item = functionHelper.createDataObject("map20topoogy20preview:GPSMapData", "none", null);
    item.store("latitude", Math.random() *90 as Double, null);
    item.store("longitude", Math.random()*180 as Double, null);
    item.store("value",it, null);
            items << item;
}
return items