id = action:getId();

if id == nil then
	Error("Action must have id");
	return;
end

attackObject = map:getGameObject(id);
if attackObject == nil then
	Error("Map does not have object with id = '"..id.."'");
	return;
end

objectPoint = object:getMapPoint();

speed = object:getProperty("speed");
if speed == nil then
	Error("Object ("..object:getId()..") does not contains property 'speed'");
	return;
end

weapon = object:getWeapon();
if weapon == nil then
	Error("Object ("..object:getId()..") does not have weapon");
	return;
end

weaponRange = weapon:getRange();

-- Получение ближайшей клетки с которой объект может атаковать и до которой может дойти
targetPoint = dnd:NearestAttackMapPoint(map, objectPoint, attackObject:getMapPoint(), speed, weaponRange);

-- Если такой клетки нет (она далеко, или до нее нельзя построить путь), то просто движемся к объекту
-- TODO: перемещять объект по пути к ближайшей клетки с которой можно атаковать
if targetPoint == nil then
	
	path = dnd:MapPath(map, objectPoint, attackObject:getMapPoint());

	if path == nil then
		Error("No path avalible to point ("..targetPoint:toString()..")");
		return;
	end

	map:moveObject(path:getMapPointByDistance(speed), object);
	
	return;
	
end

-- Перемещаем объект в клетку с которой он может атаковать
map:moveObject(targetPoint, object);

-- Вычисление атаки объекта
attackBase = object:getProperty("attack");
if attackBase == nil then
	Error("Object ("..object:getId()..") does not contains property 'attack'");
	return;
end
attackRoll = dnd:d(20);
attack = attackBase + attackRoll;

ac = attackObject:getProperty("ac");
if ac == nil then
	Error("Object ("..attackObject:getId()..") does not contains property 'ac'");
	return;
end

-- Проверка перебросил ли атакующий AC цели (при броске 20 - автопопадение, при броске 1 - автопромах)
if (ac > attack and attackRoll ~= 20) or attackRoll == 1 then
	return;
end

strengthModifier = dnd:AttributeModifier(object, "strength");
damage = weapon:getDamage() + strengthModifier;

-- Критическое пападение
if (weapon:inCriticalRange(attackRoll)) then
	critRoll = dnd:d(20);
	critAttack = critRoll + attackBase;
	if critAttack >= ac then
		 i = 1;
		 max = weapon:getCriticalKoef();
		 while i < max do
		 	damage = damage + weapon:getDamage() + strengthModifier;
		 end
	end
end

-- Уменьшение hp у атакуемого юнита (если )
hp = attackObject:getHp();
hp = hp - damage;

if hp <= 0 then
	map:removeObject(attackObject);
else
	attackObject:setHp(hp);
end