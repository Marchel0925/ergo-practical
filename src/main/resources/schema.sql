DROP TABLE IF EXISTS `people`;
DROP TABLE IF EXISTS `logs`;

CREATE TABLE people (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(50) NOT NULL,
    `last_name` VARCHAR(50) NOT NULL,
    `gender` VARCHAR(20) DEFAULT 'other',
    `birth_day` TIMESTAMP NOT NULL,
    `phone_number` VARCHAR(12) DEFAULT NULL,
    `email` VARCHAR(120) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE logs (
     `id` INTEGER NOT NULL AUTO_INCREMENT,
     `message` VARCHAR(255) NOT NULL,
     `type` VARCHAR(10) NOT NULL,
     `created` TIMESTAMP NOT NULL,
     PRIMARY KEY (`id`)
);