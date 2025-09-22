CREATE TABLE `roles`
(
    `role_id`   bigint                          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `role_name` enum('ROLE_ADMIN','ROLE_USER')  DEFAULT NULL
)