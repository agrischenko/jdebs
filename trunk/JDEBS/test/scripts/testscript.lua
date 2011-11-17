enemy = me:getNearestEnemy();

if (enemy ~= nil) then
	action:setType("attack");
	action:setId(enemy:getId());
	return
end 

dy = me:getProperty("dy");

if dy == nil then
	dy = -2;
end

x = me:getMapPoint():getX();
y = me:getMapPoint():getY();

y = y + dy;

if not map:passable(MapPoint(x, y)) then
	dy = - dy;
	y = y + 2 * dy;
end

me:setProperty("dy", dy);

action:setType("move");
action:setMapPoint(MapPoint(x, y));