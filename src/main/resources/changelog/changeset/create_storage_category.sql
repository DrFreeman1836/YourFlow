CREATE TABLE `storage_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `users_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8m7jv856pcaiv7secvei5r7fy` (`users_id`),
  CONSTRAINT `FK8m7jv856pcaiv7secvei5r7fy` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;