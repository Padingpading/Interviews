CREATE TABLE `transaction_task_log`
(
    `id`                      bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `task_id`                 varchar(64) COLLATE utf8_bin  NOT NULL,
    `task_type`               varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `task_class_name`         varchar(128) COLLATE utf8_bin NOT NULL,
    `transaction_type`        varchar(32) COLLATE utf8_bin  NOT NULL,
    `status`                  varchar(32) COLLATE utf8_bin  NOT NULL,
    `retry_status`            varchar(32) COLLATE utf8_bin   DEFAULT NULL,
    `reversal_status`         varchar(32) COLLATE utf8_bin   DEFAULT NULL,
    `error_code`              varchar(64) COLLATE utf8_bin   DEFAULT NULL,
    `error_message`           varchar(256) COLLATE utf8_bin  DEFAULT NULL,
    `times`                   int(10) unsigned DEFAULT NULL,
    `update_time`             datetime                      NOT NULL,
    `create_time`             datetime                      NOT NULL,
    `next_execute_time`       datetime                       DEFAULT NULL,
    `request_additional_info` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    `result_additional_info`  varchar(4000) COLLATE utf8_bin DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_id_type` (`task_id`,`task_type`),
    KEY                       `idx_update_time` (`update_time`,`transaction_type`),
    KEY                       `idx_next_execute_time` (`next_execute_time`,`transaction_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;