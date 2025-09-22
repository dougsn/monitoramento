CREATE TABLE `password_reset_token`
(
    `id`          bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `expiry_date` datetime(6)   NOT NULL,
    `token`       varchar(255)  NOT NULL UNIQUE,
    `used`        bit(1)        NOT NULL,
    `user_id`     bigint        DEFAULT NULL,
    `fk_user_id`  BIGINT        DEFAULT NULL,
    FOREIGN KEY (fk_user_id)
        REFERENCES users (user_id)
)