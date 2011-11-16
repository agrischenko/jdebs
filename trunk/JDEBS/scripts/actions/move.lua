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

-- ��������� ���� � �������, � ������� ����� ������������ ������ (���� � ������� � �������� ����� ������������ ������)
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

-- ����������� ������� � ������� ����������� �� ���� �� ���������� ������ �������� �������
map:moveObject(path:getMapPointByDistance(speed), object);