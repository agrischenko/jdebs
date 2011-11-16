point = action:getMapPoint();
id = action:getId();

if point == nil and id == nil then
	Error("Action must have point or id");
	return;
end

objectPoint = object:getMapPoint();

speed = object:getProperty("speed");
if speed == nil then
	Error("Object does not contains property 'speed'");
	return;
end

-- Получение пути в квадрат, в который хочет переместится объект (либо к объекту к которому хочет переместится объект)
if id == nil then
	path = dnd:MapPath(map, objectPoint, point);
	if path == nil then
		Error("No path avalible to square ("..point:toString()..")");
		return;
	end
	
else
	moveObject = map:getGameObject(id);
	if moveObject == nil then
		Error("Map does not have object with id = '"..id.."'");
		return;
	end
	path = dnd:MapPath(map, objectPoint, moveObject);
	if path == nil then
		Error("No path avalible to object with id = '"..id.."' ("..moveObject:getMapPoint():toString()..")");
		return;
	end
end

--print("path = "..path:toString().." move to ("..path:getMapPointByDistance(speed):toString()..") speed "..speed);

-- Перемещение объекта в квадрат находящийся на пути на расстоянии равной скорости объекта
map:moveObject(path:getMapPointByDistance(speed), object);