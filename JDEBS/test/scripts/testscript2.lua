x = me:getMapPoint():getX();
y = me:getMapPoint():getY();

x = x + 1

action:setType("move");
action:setMapPoint(MapPoint(x, y));