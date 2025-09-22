CREATE TABLE `dvr`
(
    `id`                          BIGINT                  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `nome`                        VARCHAR(100)            NOT NULL,
    `created_by`                  VARCHAR(200)            DEFAULT NULL,
    `updated_by`                  VARCHAR(200)            DEFAULT NULL,
    `created_at`                  DATETIME(6)             DEFAULT NULL,
    `updated_at`                  DATETIME(6)             DEFAULT NULL,
    `fk_status_id`                BIGINT,
    FOREIGN KEY(fk_status_id)
        REFERENCES status(id)
)