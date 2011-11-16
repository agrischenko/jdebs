id = action:getId();

if id == nil then
	Error("Action must have id");
	return;
end

print("1");

attackObject = map:getGameObject(id);
if attackObject == nil then
	Error("Map does not have object with id = '"..id.."'");
	return;
end

objectPoint = object:getMapPoint();

print("2");

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

print("3");

weaponRange = weapon:getRange();

print("3.1");

-- Получение ближайшей клетки с которой объект может атаковать и до которой может дойти
targetPoint = dnd:NearestAttackMapPoint(map, objectPoint, attackObject:getMapPoint(), speed, weaponRange);

print("4");

-- Если такой клетки нет (она далеко, или до нее нельзя построить путь), то просто движемся к объекту
-- TODO: перемещять объект по пути к ближайшей клетки с которой можно атаковать
if targetPoint == nil then
	
	path = dnd:MapPath(map, objectPoint, attackObject);

	if path == nil then
		Error("No path avalible to object ("..attackObject:getId()..")");
		return;
	end

	map:moveObject(path:getMapPointByDistance(speed), object);
	
	return;
	
end

print("5");

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

print("6");

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

print("7");

-- Уменьшение hp у атакуемого юнита (если hp<0 удаляем юнита с карты)
hp = attackObject:getHp();
hp = hp - damage;

if hp <= 0 then
	map:removeObject(attackObject);
else
	attackObject:setHp(hp);
end