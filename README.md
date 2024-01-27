# gra-w-go
## Komendy w bazie (mariadb):
```
CREATE DATABASE GOgames;
CREATE USER 'goplayer'@'localhost' IDENTIFIED BY 'superhaslo';
GRANT ALL PRIVILEGES ON GOgames.* TO 'goplayer'@'localhost';
CREATE TABLE IF NOT EXISTS `GOgames`.`games` (
    `game_id` INT NOT NULL,
     `size` INT NOT NULL,
     `black_points` INT NOT NULL,
     `white_points` INT NOT NULL,
     PRIMARY KEY (`game_id`)
     );
CREATE TABLE IF NOT EXISTS `GOgames`.`boards` (
     `board_id` INT NOT NULL AUTO_INCREMENT,
     `game_id` INT NOT NULL,
     `turn` INT NOT NULL,
     `state` MEDIUMTEXT NOT NULL,
     PRIMARY KEY (`board_id`),
     FOREIGN KEY (`game_id`) REFERENCES `GOgames`.`games`(`game_id`)
     );
```
### przykladowe zapytania:
```
INSERT INTO `GOgames`.`games` (`size`, `black_points`, `white_points`) VALUES ('9', '0', '0');
INSERT INTO `GOgames`.`boards` (`game_id`, `state`) VALUES ('1','*********|*BWB*BWBW*|*WB**WB*B|*BWBBWBW*|*WBWBWWB*|*BBWBWBW*|WBWBWBWB*|*BWBWBWBW|WBWBWBWB*|*********|');
```