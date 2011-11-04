movePoint = action:getMapPoint();

objectPoint = object:getMapPoint();

speed = object:getProperty("speed");

if speed == nil then
	Error("Object does not contains property 'speed'");
	return;
end

distance = dnd:MapDistance(map, movePoint, objectPoint);

if distance > speed then
	Error("The object is trying to move a distance greater than its speed");
	return;
end

x = movePoint:getX();
y = movePoint:getY();

if x < 0 or x >= map:getWidth() or y < 0 or y >= map:getHeight() then
	Error("Then object tries to go beyond the map ("..movePoint:toString()..")");
	return;
end

map:moveObject(movePoint, object);