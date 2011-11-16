enemy = me:getNearestEnemy();
if (enemy == nil) then
	return
end 

action:setType("move");
action:setId(enemy:getId());