x = me:getMapPoint():getX();
y = me:getMapPoint():getY();

y = y + 1

action:setType("move");
action:setMapPoint(MapPoint(x, y));