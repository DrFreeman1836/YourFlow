CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `chat_id` bigint DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `id_telegram` bigint DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_4qiu74kvaj4x84eq5yaopoemp` (`chat_id`),
  UNIQUE KEY `UK_im3mool1sx53iv3w11khvewxv` (`id_telegram`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;