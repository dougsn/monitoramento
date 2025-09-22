CREATE TABLE `users`
(
    `user_id`                 bigint        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `username`                varchar(20)   NOT NULL,
    `email`                   varchar(50)   NOT NULL UNIQUE,
    `sign_up_method`          varchar(255)  DEFAULT NULL,
    `is_two_factor_enabled`   bit(1)        NOT NULL,
    `created_date`            datetime(6)   DEFAULT NULL,
    `credentials_expiry_date` date          DEFAULT NULL,
    `credentials_non_expired` bit(1)        NOT NULL,
    `enabled`                 bit(1)        NOT NULL,
    `password`                varchar(120)  DEFAULT NULL,
    `two_factor_secret`       varchar(255)  DEFAULT NULL,
    `account_expiry_date`     date          DEFAULT NULL,
    `account_non_expired`     bit(1)        NOT NULL,
    `account_non_locked`      bit(1)        NOT NULL,
    `updated_date`            datetime(6)   DEFAULT NULL,
    `fk_role_id`              BIGINT        NOT NULL NULL,
    FOREIGN KEY (fk_role_id)
        REFERENCES roles (role_id)
)