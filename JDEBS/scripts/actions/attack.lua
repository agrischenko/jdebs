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

-- ��������� ��������� ������ � ������� ������ ����� ��������� � �� ������� ����� �����
targetPoint = dnd:NearestAttackMapPoint(map, objectPoint, attackObject:getMapPoint(), speed, weaponRange);

print("4");

-- ���� ����� ������ ��� (��� ������, ��� �� ��� ������ ��������� ����), �� ������ �������� � �������
-- TODO: ���������� ������ �� ���� � ��������� ������ � ������� ����� ���������
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

-- ���������� ������ � ������ � ������� �� ����� ���������
map:moveObject(targetPoint, object);

-- ���������� ����� �������
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

-- �������� ���������� �� ��������� AC ���� (��� ������ 20 - �������������, ��� ������ 1 - ����������)
if (ac > attack and attackRoll ~= 20) or attackRoll == 1 then
	return;
end

strengthModifier = dnd:AttributeModifier(object, "strength");
damage = weapon:getDamage() + strengthModifier;

-- ����������� ���������
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

-- ���������� hp � ���������� ����� (���� hp<0 ������� ����� � �����)
hp = attackObject:getHp();
hp = hp - damage;

if hp <= 0 then
	map:removeObject(attackObject);
else
	attackObject:setHp(hp);
end