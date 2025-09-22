CREATE TABLE `log_status_dvr`
(
    `id`                          BIGINT                  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `dia`                         DATE                    NOT NULL,
    `acao`                        VARCHAR(10)             NOT NULL,
    `status_novo`                 VARCHAR(100)            DEFAULT NULL,
    `status_antigo`               VARCHAR(100)            DEFAULT NULL,
    `nome_novo`                   VARCHAR(100)            DEFAULT NULL,
    `nome_antigo`                 VARCHAR(100)            DEFAULT NULL,
    `created_by`                  VARCHAR(200)            DEFAULT NULL,
    `updated_by`                  VARCHAR(200)            DEFAULT NULL,
    `created_at`                  DATETIME(6)             DEFAULT NULL,
    `updated_at`                  DATETIME(6)             DEFAULT NULL,
    `fk_dvr_id`                   BIGINT,
    FOREIGN KEY(fk_dvr_id)
        REFERENCES dvr(id)
)