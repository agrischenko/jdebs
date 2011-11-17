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

-- ��������� ��������� ������ � ������� ������ ����� ��������� � �� ������� ����� �����
targetPoint = dnd:NearestAttackMapPoint(map, objectPoint, attackObject:getMapPoint(), speed, weaponRange);

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
Print("attack: "..attack.." (roll: "..attackRoll.." base: "..attackBase..")");

ac = attackObject:getProperty("ac");
if ac == nil then
	Error("Object ("..attackObject:getId()..") does not contains property 'ac'");
	return;
end

-- �������� ���������� �� ��������� AC ���� (��� ������ 20 - �������������, ��� ������ 1 - ����������)
if (ac > attack and attackRoll ~= 20) or attackRoll == 1 then
	if attackRoll == 1 then
		Print("miss (automiss attack roll is 1)");
	else
		Print("miss (ac: "..ac..")");
	end
	return;
end

Print("hit (ac: "..ac..")");

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
		 	i = i + 1
		 end
		 Print("damage: "..damage.." (critical confirmed)");
	else
		Print("damage: "..damage.." (critical not confirmed)");
	end
else
	Print("damage: "..damage);
end

-- ���������� hp � ���������� ����� (���� hp<0 ������� ����� � �����)
hp = attackObject:getHp();
hp = hp - damage;

if hp <= 0 then
	Print("target is dead");
	map:removeObject(attackObject);
else
	Print("target hp: "..hp.." (lost: "..damage..")");
	attackObject:setHp(hp);
end