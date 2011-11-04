meX = me:getMapPoint():getX();
meY = me:getMapPoint():getY();

enemy = me:getNearestEnemy();
if (enemy == nil) then
	return
end 
enemyMapPoint = enemy:getMapPoint();

enemyX = enemyMapPoint:getX();
enemyY = enemyMapPoint:getY();

--print("enemy ("..enemyX..":"..enemyY..")");

x = enemyX;
y = enemyY;

if meX < enemyX then
	x = x - 1;
elseif meX > enemyX then
	x = x + 1;
end

if meY < enemyY then
	y = y - 1;
elseif meY > enemyY then
	y = y + 1;
end

--print("move ("..x..":"..y..")");

action:setType("move");
action:setMapPoint(MapPoint(x, y));