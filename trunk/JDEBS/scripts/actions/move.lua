movePoint = action:getMapPoint();

objectPoint = object:getMapPoint();

speed = object:getProperty("speed");

if speed == nil then
	Error("Object does not contains property 'speed'");
	return;
end

-- Получение пути в квадрат, в который хочет переместится объект
path = dnd:MapPath(map, objectPoint, movePoint);

if path == nil then
	Error("No path avalible to square("..movePoint:toString()..")");
	return;
end

-- Перемещение объекта в квадрат находящийся на пути на расстоянии равной скорости объекта
map:moveObject(path:getMapPointByDistance(speed), object);