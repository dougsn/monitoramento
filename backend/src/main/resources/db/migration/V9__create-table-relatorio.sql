CREATE TABLE `relatorio`
(
    `id`                          BIGINT                  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `dia`                         DATE                    NOT NULL,
    `descricao`                   LONGTEXT                DEFAULT NULL,
    `created_by`                  VARCHAR(200)            DEFAULT NULL,
    `updated_by`                  VARCHAR(200)            DEFAULT NULL,
    `created_at`                  DATETIME(6)             DEFAULT NULL,
    `updated_at`                  DATETIME(6)             DEFAULT NULL
)