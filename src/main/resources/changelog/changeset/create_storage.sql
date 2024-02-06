CREATE TABLE `storage` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `preview` varchar(255) DEFAULT NULL,
  `content` text DEFAULT NULL,
  `storage_category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8m7jv856pcaiv7secvei5r7fyyyy` (`storage_category_id`),
  CONSTRAINT `FK8m7jv856pcaiv7secvei5r7fyyyy` FOREIGN KEY (`storage_category_id`) REFERENCES `storage_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;