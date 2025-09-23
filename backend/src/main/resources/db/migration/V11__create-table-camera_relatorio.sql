CREATE TABLE `camera_relatorio`
(
    `id`                          BIGINT                  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `dia`                         DATE                    NOT NULL,
    `created_by`                  VARCHAR(200)            DEFAULT NULL,
    `updated_by`                  VARCHAR(200)            DEFAULT NULL,
    `created_at`                  DATETIME(6)             DEFAULT NULL,
    `updated_at`                  DATETIME(6)             DEFAULT NULL,
    `fk_status_id`                BIGINT,
    FOREIGN KEY(fk_status_id)
        REFERENCES status(id),
    `fk_camera_id`                BIGINT,
    FOREIGN KEY(fk_camera_id)
        REFERENCES camera(id),
    `fk_relatorio_id`                BIGINT,
    FOREIGN KEY(fk_relatorio_id)
        REFERENCES relatorio(id)
)