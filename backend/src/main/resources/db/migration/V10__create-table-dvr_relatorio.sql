CREATE TABLE `dvr_relatorio`
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
    `fk_dvr_id`                BIGINT,
    FOREIGN KEY(fk_dvr_id)
        REFERENCES dvr(id),
    `fk_relatorio_id`                BIGINT,
    FOREIGN KEY(fk_relatorio_id)
        REFERENCES relatorio(id)
)