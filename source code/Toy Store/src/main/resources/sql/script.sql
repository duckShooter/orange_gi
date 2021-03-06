INSERT INTO category (name) VALUES ("Toy Weapons");
SET @id := LAST_INSERT_ID();
INSERT INTO product (name, description, vendor, price, category_id) VALUES ("Ultimax 100", "Mk.2 machine gun with 30-round M16 magazine and sheathed bayonet", "Chartered Industries", "89999.00", @id);
INSERT INTO product (name, description, vendor, price, category_id) VALUES ("Barrett M82", "a semi-automatic anti-material rifle, designed to destroy sensitive enemy equipment at long distances", "Barrett Firearms", "100000.00", @id);

INSERT INTO category (name) VALUES ("Games & Puzzels");
SET @id := LAST_INSERT_ID();
INSERT INTO product (name, description, vendor, price, category_id) VALUES ("3D Puzzle Eiffel Tower (82 Pieces)", "sort em, built em, laugh and destroy em!", "Cubic Fun", "599.00", @id);
INSERT INTO product (name, description, vendor, price, category_id) VALUES ("UNO Card Game", "UNO ! The classic card game of matching colors and numbers", "Mattel", "9.99", @id);

INSERT INTO category (name) VALUES ("Action Figures");
SET @id := LAST_INSERT_ID();
INSERT INTO product (name, description, vendor, price, category_id) VALUES ("Fortnite Squad Mode 4 Figure Pack", "Edgy toiz for edgy boiz", "Spider-Man", "12.78", @id);
INSERT INTO product (name, description, vendor, price, category_id) VALUES ("DC Justice League True-Moves Series Batman Figure", "Edgy toiz for edgy boiz", "Mattel", "17.00", @id);

INSERT INTO category (name) VALUES ("Vehicles, Trains, RC");
SET @id := LAST_INSERT_ID();
INSERT INTO product (name, description, vendor, price, category_id) VALUES ("Leopard 2A7", "armed with a 120 mm smoothbore cannon, and is powered by a V-12 twin-turbo diesel engine", "Krauss-Maffei Wegmann", "5000.00", @id);
INSERT INTO product (name, description, vendor, price, category_id) VALUES ("Cheerwing Mini RC Helicopter", "with 3-channel, infrared control and colorful flashing light", "Cheerwing", "19.98", @id);