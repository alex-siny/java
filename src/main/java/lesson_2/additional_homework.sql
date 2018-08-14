-- Структура таблицы для дополнительного домашнего задания
CREATE TABLE IF NOT EXISTS `auth_logs` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`login`	TEXT NOT NULL,
	`date`	TEXT NOT NULL
);
-- Очищаем таблицу на тот случай если она уже существовала в БД
DELETE FROM `auth_logs`;
-- Заполняем таблицу данными
INSERT INTO `auth_logs` (`login`, `date`)
VALUES
('Bob1', '12.08.18'),
('Bob2', '05.01.16'),
('Bob3', '02.05.15'),
('Bob4', '02.02.18'),
('Bob3', '13.03.18'),
('Bob2', '07.11.16'),
('Bob1', '08.06.18'),
('Bob3', '05.05.18'),
('Bob4', '03.09.17'),
('Bob1', '05.05.17'),
('Bob2', '25.10.17');
-- Решение (достаем дату последней авторизации каждого пользователя)
SELECT
  `login`,
  strftime('%d.%m.%Y', MAX(DATE(20 || substr(`date`, 7, 2) || '-' || substr(`date`, 4, 2) || '-' ||  substr(`date`, 1, 2)))) AS `date`
FROM
  `auth_logs`
GROUP BY
	`login`
ORDER BY
  `login` ASC
-- Примечание к решению
-- При написании запроса я предполагаю, что формат даты DD.MM.YY, а также, что все даты лежат в диапазоне от 2000 года
-- до 2099 года, так как я не могу сказать однозначно, к примеру, к какому году принадлежит дата 05.05.18, к 05.05.2018
-- или 05.05.1918, а может это и вовсе 3918 год =).
-- По моему мнению более правильным было бы хранить дату в формате YYYY-MM-DD, во первых, это избавило бы от ограничений
-- по годам, во вторых, отпала бы необходимость использовать "костыли" на подобии этого:
-- DATE(20 || substr(`date`, 7, 2) || '-' || substr(`date`, 4, 2) || '-' ||  substr(`date`, 1, 2)).