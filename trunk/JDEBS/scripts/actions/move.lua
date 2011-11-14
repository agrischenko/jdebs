movePoint = action:getMapPoint();

objectPoint = object:getMapPoint();

speed = object:getProperty("speed");

if speed == nil then
	Error("Object does not contains property 'speed'");
	return;
end

-- ��������� ���� � �������, � ������� ����� ������������ ������
path = dnd:MapPath(map, objectPoint, movePoint);

if path == nil then
	Error("No path avalible to square("..movePoint:toString()..")");
	return;
end

-- ����������� ������� � ������� ����������� �� ���� �� ���������� ������ �������� �������
map:moveObject(path:getMapPointByDistance(speed), object);