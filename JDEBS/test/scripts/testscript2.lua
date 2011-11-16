enemy = me:getNearestEnemy();
if (enemy == nil) then
	return
end 

action:setType("attack");
action:setId(enemy:getId());