CREATE TABLE `task` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) DEFAULT NULL,
  `is_active` TINYINT(1) DEFAULT NULL,
  `creating_date` date DEFAULT NULL,
  `users_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8m7jv856pcaiv7secvei5r7fyrtask` (`users_id`),
  CONSTRAINT `FK8m7jv856pcaiv7secvei5r7fyrtask` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;